package com.trinity.trinity.webSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinity.trinity.DTO.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.DTO.request.ThirdRoomPlayerRequestDto;
import com.trinity.trinity.DTO.response.FirstRoomResponseDto;
import com.trinity.trinity.DTO.response.SecondRoomResponseDto;
import com.trinity.trinity.DTO.response.ThirdRoomResponseDto;
import com.trinity.trinity.client.ClientSession;
import com.trinity.trinity.gameRoom.dto.GameRoom;
import com.trinity.trinity.gameRoom.service.GameRoomService;
import com.trinity.trinity.redisUtil.GameRoomRedisService;
import com.trinity.trinity.redisUtil.RedisService;
import com.trinity.trinity.webClient.ChannelManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
@DependsOn("redisService")
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final RedisService redisService;
    private final GameRoomRedisService gameRoomRedisService;
    private final GameRoomService gameRoomService;
    private final ChannelManager channelManager;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();

            JsonObject jsonObject = new JsonParser().parse(text).getAsJsonObject();
            String requestType = jsonObject.get("type").getAsString();
            if(requestType.equals("matching")){
                String userId = jsonObject.get("userId").getAsString();
                String clientId = ctx.channel().id().toString();
                System.out.println(clientId);
                String clientAddress = ctx.channel().remoteAddress().toString();
                ClientSession clientSession = redisService.getClientSession(userId);
                if(clientSession == null) {
                    clientSession = new ClientSession(userId, clientId, clientAddress);
                    // 클라이언트 세션을 Redis에 저장
                    redisService.saveClient(clientSession);
                }
                //1 로그인 : clientId, channel 저장 --> channelManager를 통해(이건 webclient패키지안에 보면 있음)
                //2 "metching" 타입의 websocket을 보낸다
                //3 요청에 담긴 userId를 통해 redisService에서 clientId를 찾아오고
                //4 그 clientId와 보낼 데이터(connecting success!!)를 함께 sendDataToClient메소드에 넣어주면 잘간다.
                //5 sendDataToClient 메소드 안에서는 받아온 clientId를 channelManager.getChannel 메소드에 넣어서 channel을 찾아온다
                //6 찾아온 채널로 connecting success!!를 보내면 잘간다.

                //7 webclient를 이용해 매칭 서버에서 3명의 사람 리스트를 받아온다.
                //8 받아온 3명을 기반으로 방을 만든다.
                //9 방에 대한 정보 중 userId를 뽑아온다.
                //10 뽑아온 userId를 이용해 redisService의 getClientId를 써서 clientId를 각각 구한다.
                //11 clientId를 websocketFrameHandler.sendDataToClient(clientId, firstRoom) 이렇게 보낸다.
                //12 sendDataToClient에서는 받아온 clientId를 이용해 channel을 찾는다
                //13 아까와는 다르게 channel을 못찾는다. "channel is null!"

                //14 다시 "metching"을 타입으로 하는 websocket 메세지를 보낸다.
                //15 같이 보낸 userId를 이용해 clientId를 찾는다.
                //16 sendDataToClient를 사용해 clientId, 와 "connecting success!!"를 보낸다.
                //17 성공했고 아까 보냈을 때 출력된 clientId와 비교해 본다. 똑같다
                //18 같이 출력된 channel 정보도 출력해 본다 똑같다.

                try {
                    // 2초 동안 일시 중지
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                sendDataToClient(redisService.getClientId(userId), "Connecting SUCCESS!!");
                System.out.println("clientId는 ??????????????? : " + redisService.getClientId(userId));
                System.out.println("처음 보냈을 때 확인용 ChannelRead 안 : "+ channelManager.getChannel(clientId));
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
                }
            }else if(requestType.equals("test")){
                if(ctx.channel() == channelManager.getChannel(ctx.channel().id().toString())) {

                    sendDataToClient(ctx.channel().id().toString(), "같은데요");
                } else {
                    sendDataToClient(ctx.channel().id().toString(), "다른데요");
                    System.out.println("달라요");
                }
            }
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println("저장될때!!!!!!!!!!!!!!!!!!!!!!!!! : " + ctx.channel().id().toString());
            channelManager.addChannel(ctx.channel().id().toString(), ctx.channel());
        }
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            System.out.println("끊어질 수 있다.");
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new PingMessage());
            }
        }
    }

    public void sendDataToClient(String clientId, String data) {
//        if(activeChannels.containsKey(clientId)) System.out.println("key 존재 --> 초기화 안됐음");
        if(channelManager.CheckContainKey(clientId)){
            System.out.println("있습니다.");
        } else {
            System.out.println("없습니다.");
        }
        Channel channel = channelManager.getChannel(clientId);
        if (channel != null) {
            TextWebSocketFrame textFrame = new TextWebSocketFrame(data);
            channel.writeAndFlush(textFrame);
            System.out.println("첫 메세지 전송 후 유지가 되는가 : " + channel);
        } else {
            System.out.println("channel is null!");
        }
    }
}
