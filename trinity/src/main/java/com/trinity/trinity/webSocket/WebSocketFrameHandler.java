package com.trinity.trinity.webSocket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinity.trinity.DTO.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.ThirdRoomPlayerRequestDto;
import com.trinity.trinity.DTO.response.FirstRoomPlayerResponseDto;
import com.trinity.trinity.DTO.response.FirstRoomResponseDto;
import com.trinity.trinity.DTO.response.SecondRoomResponseDto;
import com.trinity.trinity.DTO.response.ThirdRoomResponseDto;
import com.trinity.trinity.client.ClientSession;
import com.trinity.trinity.gameRoom.dto.GameRoom;
import com.trinity.trinity.gameRoom.service.GameRoomService;
import com.trinity.trinity.redisUtil.GameRoomRedisService;
import com.trinity.trinity.redisUtil.RedisService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
@DependsOn("redisService")
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final RedisService redisService;
    private final GameRoomRedisService gameRoomRedisService;
    private final GameRoomService gameRoomService;
    private ConcurrentMap<String, Channel> activeChannels = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();

            JsonObject jsonObject = new JsonParser().parse(text).getAsJsonObject();
            String requestType = jsonObject.get("type").getAsString();
            if(requestType.equals("matching")){
                String userId = jsonObject.get("userId").getAsString();
                System.out.println("useRId : " + userId);
                String clientId = ctx.channel().id().toString();
                System.out.println(clientId);
                String clientAddress = ctx.channel().remoteAddress().toString();
                System.out.println(clientAddress);
                ClientSession clientSession = redisService.getClientSession(userId);
                if(clientSession == null) {
                    clientSession = new ClientSession(userId, clientId, clientAddress);
                    // 클라이언트 세션을 Redis에 저장
                    redisService.saveClient(clientSession);
                }
                sendDataToClient(userId, "Connecting SUCCESS!!");
            } else if (requestType.equals("roundEnd")) {
                String gameRoomId = jsonObject.get("gameRoomId").getAsString();
                String roomNum = jsonObject.get("roomNum").getAsString();


                if(roomNum.equals("first")) {
                    Gson gson = new Gson();
                    FirstRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("FirstRoomPlayerRequestDto").getAsString(), FirstRoomPlayerRequestDto.class);
                    gameRoomService.updateFirstRoom(dto);
                } else if(roomNum.equals("second")) {
                    Gson gson = new Gson();
                    SecondRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("SecondRoomPlayerRequestDto").getAsString(), SecondRoomPlayerRequestDto.class);
                    gameRoomService.updateSecondRoom(dto);
                } else {
                    Gson gson = new Gson();
                    ThirdRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("ThirdRoomPlayerRequestDto").getAsString(), ThirdRoomPlayerRequestDto.class);
                    gameRoomService.updateThridRoom(dto);
                }

                boolean complete = redisService.checkGameRoomAllClear(gameRoomId, roomNum);
                if(complete) {
                    boolean gameOver = gameRoomService.gameLogic(gameRoomId);
                    if (!gameOver) {
                        gameRoomService.morningGameLogic(gameRoomId);

                        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);

                        //각 방정보를 뽑아와서 각 플레이어에게 보내기
                        Gson gson = new Gson();
                        FirstRoomResponseDto firstRoomResponseDto = FirstRoomResponseDto.builder().build();
                        firstRoomResponseDto.modifyThirdRoomResponseDto(gameRoom);
                        String firstRoom = gson.toJson(firstRoomResponseDto);
                        SecondRoomResponseDto secondRoomResponseDto = SecondRoomResponseDto.builder().build();
                        secondRoomResponseDto.modifyThirdRoomResponseDto(gameRoom);
                        String secondRoom = gson.toJson(secondRoomResponseDto);
                        ThirdRoomResponseDto thirdRoomResponseDto = ThirdRoomResponseDto.builder().build();
                        thirdRoomResponseDto.modifyThirdRoomResponseDto(gameRoom);
                        String thirdRoom = gson.toJson(thirdRoomResponseDto);

                        sendDataToClient(gameRoom.getRound().getFirstRoom().getPlayer(), firstRoom);
                        sendDataToClient(gameRoom.getRound().getSecondRoom().getPlayer(), secondRoom);
                        sendDataToClient(gameRoom.getRound().getThirdRoom().getPlayer(), thirdRoom);
                    }
                    else {

                    }
                }
            }
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println(ctx.channel().id());
            activeChannels.put(ctx.channel().id().toString(), ctx.channel());
        }
        return;
    }

    public void sendDataToClient(String userId, String data) {
        String clientId = redisService.getClientId(userId);
        Channel channel = activeChannels.get(clientId);
        if (channel != null) {
            TextWebSocketFrame textFrame = new TextWebSocketFrame(data);
            channel.writeAndFlush(textFrame);
        } else {
            System.out.println("channel is null!");
        }
    }
}
