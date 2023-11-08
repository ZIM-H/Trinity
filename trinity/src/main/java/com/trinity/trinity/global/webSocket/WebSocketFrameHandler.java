package com.trinity.trinity.global.webSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinity.trinity.domain.control.dto.request.FirstRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.SecondRoomPlayerRequestDto;
import com.trinity.trinity.domain.control.dto.request.ThirdRoomPlayerRequestDto;
import com.trinity.trinity.global.dto.ClientSession;
import com.trinity.trinity.domain.control.dto.response.*;
import com.trinity.trinity.domain.control.enums.UserStatus;
import com.trinity.trinity.domain.logic.dto.GameRoom;
import com.trinity.trinity.domain.logic.service.GameRoomService;
import com.trinity.trinity.global.redis.service.GameRoomRedisService;
import com.trinity.trinity.global.redis.service.RedisService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.PingMessage;

@Slf4j
@Component
@RequiredArgsConstructor
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

            System.out.println("type : " + requestType + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            if (requestType.equals("matching")) {
                String userId = jsonObject.get("userId").getAsString();
                String clientId = ctx.channel().id().toString();

                String clientAddress = ctx.channel().remoteAddress().toString();
                ClientSession clientSession = redisService.getClientSession(userId);
                if (clientSession == null) {
                    clientSession = new ClientSession(userId, clientId, clientAddress);
                    // 클라이언트 세션을 Redis에 저장
                    redisService.saveClient(clientSession);
                }

                sendDataToClient(redisService.getClientId(userId), "Connecting SUCCESS!!");

            } else if (requestType.equals("roundEnd")) {
                String gameRoomId = jsonObject.get("gameRoomId").getAsString();
                String roomNum = jsonObject.get("roomNum").getAsString();

                // 채널 연결확인
                if (userLeaveProcess(gameRoomId)) return;

                Gson gson = new Gson();
                // 방번호 확인하는 로직
                if (roomNum.equals("first")) {
                    FirstRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("FirstRoomPlayerRequestDto").getAsString(), FirstRoomPlayerRequestDto.class);
                    gameRoomService.updateFirstRoom(dto);
                } else if (roomNum.equals("second")) {
                    SecondRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("SecondRoomPlayerRequestDto").getAsString(), SecondRoomPlayerRequestDto.class);
                    gameRoomService.updateSecondRoom(dto);
                } else {
                    ThirdRoomPlayerRequestDto dto = gson.fromJson(jsonObject.get("ThirdRoomPlayerRequestDto").getAsString(), ThirdRoomPlayerRequestDto.class);
                    gameRoomService.updateThridRoom(dto);
                }

                System.out.println("보내 온 데이터 requestDto화 완료");

                //3 사람의 모든 데이터가 들어온 경우 true를 반환
                boolean complete = redisService.checkGameRoomAllClear(gameRoomId, roomNum);

                System.out.println("3명의 데이터가 모두 들어왔는가?");
                // 모든 데이터를 받았을 경우
                if (complete) {
                    System.out.println("3명의 데이터가 모두 들어왔다@@@@@@@@@@@@");

                    if (gameRoomService.checkEndGame(gameRoomId)) {
                        gameVictoryProcess(gameRoomId);
                    }

                    GameRoom beforeGameRoom = gameRoomRedisService.getGameRoom(gameRoomId);

                    // 소행성 충돌 여부 데이터 뽑기
                    boolean asteroidConflict = false;
                    int barrierStatus = beforeGameRoom.getRound().getThirdRoom().getBarrierStatus();
                    boolean asteroidStatus = beforeGameRoom.getRound().getThirdRoom().isAsteroidStatus();
                    boolean asteroidDestroy = beforeGameRoom.getRound().getThirdRoom().isAsteroidDestroyTry();

                    if (asteroidStatus && barrierStatus < 2 && !asteroidDestroy) asteroidConflict = true;


                    // 1. 13일차 - 위에서 checkEndGame으로 체크
                    // 2. 중간에 게임에서 실패
                    // 3. 누군가 게임에서 나간 경우
                    // --> gameOver = true
                    boolean gameOver = gameRoomService.gameLogic(gameRoomId);

                    System.out.println("gameOverCheck");

                    // gameOver가 아닌 경우
                    if (!gameOver) {

                        gameRoomService.morningGameLogic(gameRoomId);

                        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);

                        //채널 연결 확인
                        if (userLeaveProcess(gameRoomId)) return;

                        String firstId = gameRoom.getRound().getFirstRoom().getPlayer();
                        String secondId = gameRoom.getRound().getSecondRoom().getPlayer();
                        String thirdId = gameRoom.getRound().getThirdRoom().getPlayer();

                        String firstClientId = redisService.getClientId(firstId);
                        String secondClientId = redisService.getClientId(secondId);
                        String thirdClientId = redisService.getClientId(thirdId);

                        CommonDataDto commonDataDto = CommonDataDto.builder()
                                .conflictAsteroid(asteroidConflict)
                                .build();

                        commonDataDto.setCommonDto(gameRoom);

                        //각 방정보를 뽑아와서 각 플레이어에게 보내기
                        FirstRoomResponseDto firstRoomResponseDto = FirstRoomResponseDto.builder()
                                .type("nextRound")
                                .build();
                        firstRoomResponseDto.modifyFirstRoomResponseDto(commonDataDto, gameRoom);
                        String firstRoom = gson.toJson(firstRoomResponseDto);

                        SecondRoomResponseDto secondRoomResponseDto = SecondRoomResponseDto.builder()
                                .type("nextRound")
                                .build();
                        secondRoomResponseDto.modifySecondRoomResponseDto(commonDataDto, gameRoom);
                        String secondRoom = gson.toJson(secondRoomResponseDto);

                        ThirdRoomResponseDto thirdRoomResponseDto = ThirdRoomResponseDto.builder()
                                .type("nextRound")
                                .build();
                        thirdRoomResponseDto.modifyThirdRoomResponseDto(commonDataDto, gameRoom);
                        String thirdRoom = gson.toJson(thirdRoomResponseDto);

                        System.out.println("클라이언트에게 보낼 데이터 정리 완료");

                        sendDataToClient(firstClientId, firstRoom);
                        sendDataToClient(secondClientId, secondRoom);
                        sendDataToClient(thirdClientId, thirdRoom);
                    } else {
                        // 게임 오버 true인 경우
                        System.out.println("게임 패배");
                        gameDefeatedProcess(gameRoomId);
                    }
                }
            }
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            channelManager.addChannel(ctx.channel().id().toString(), ctx.channel());
        }
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(new PingMessage());
            }
        }
    }

    public void sendDataToClient(String clientId, String data) {
        Channel channel = channelManager.getChannel(clientId);
        if (channel != null) {
            TextWebSocketFrame textFrame = new TextWebSocketFrame(data);
            channel.writeAndFlush(textFrame);
        } else {
            log.info("channel is null!");
        }
    }

    public boolean ChannelAlive(String userId) {
        String clientId = redisService.getClientId(userId);
        Channel channel = channelManager.getChannel(clientId);
        if (channel.isActive()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean userLeaveProcess(String gameRoomId) {
        Gson gson = new Gson();

        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);

        boolean checkActiveAll = true;
        String firstId = gameRoom.getRound().getFirstRoom().getPlayer();
        String secondId = gameRoom.getRound().getSecondRoom().getPlayer();
        String thirdId = gameRoom.getRound().getThirdRoom().getPlayer();

        String firstClientId = redisService.getClientId(firstId);
        String secondClientId = redisService.getClientId(secondId);
        String thirdClientId = redisService.getClientId(thirdId);

        if (!ChannelAlive(firstId) || !ChannelAlive(secondId) || !ChannelAlive(thirdId)) {
            checkActiveAll = false;
        }

        if (!checkActiveAll) {

            GameOverDto userLeaveGameOverDto = GameOverDto.builder()
                    .status("userLeave")
                    .build();

            String userLeaveMessage = gson.toJson(userLeaveGameOverDto);
            String data = gson.toJson(userLeaveMessage);

            sendDataToClient(firstClientId, data);
            sendDataToClient(secondClientId, data);
            sendDataToClient(thirdClientId, data);

            gameRoomService.endGame(gameRoomId);

            String[] clientIds = {firstClientId, secondClientId, thirdClientId};

            //채널 닫기
            channelClose(clientIds);

            //채널 삭제
            removeChannels(clientIds);

            //clientSession 삭제
            redisService.removeClientSession(firstId);
            redisService.removeClientSession(secondId);
            redisService.removeClientSession(thirdId);

            redisService.saveData(firstId, String.valueOf(UserStatus.LOBBY));
            redisService.saveData(secondId, String.valueOf(UserStatus.LOBBY));
            redisService.saveData(thirdId, String.valueOf(UserStatus.LOBBY));

            return true;
        } else {
            return false;
        }
    }

    public void gameVictoryProcess(String gameRoomId) {
        Gson gson = new Gson();

        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);

        String firstId = gameRoom.getRound().getFirstRoom().getPlayer();
        String secondId = gameRoom.getRound().getSecondRoom().getPlayer();
        String thirdId = gameRoom.getRound().getThirdRoom().getPlayer();

        String firstClientId = redisService.getClientId(firstId);
        String secondClientId = redisService.getClientId(secondId);
        String thirdClientId = redisService.getClientId(thirdId);

        GameOverDto userLeaveGameOverDto = GameOverDto.builder()
                .status("VICTORY")
                .build();

        String userLeaveMessage = gson.toJson(userLeaveGameOverDto);
        String data = gson.toJson(userLeaveMessage);

        sendDataToClient(firstClientId, data);
        sendDataToClient(secondClientId, data);
        sendDataToClient(thirdClientId, data);

        //clientSession 삭제
        redisService.removeClientSession(firstId);
        redisService.removeClientSession(secondId);
        redisService.removeClientSession(thirdId);

        String[] clientIds = {firstClientId, secondClientId, thirdClientId};

        //채널 닫기
        channelClose(clientIds);

        //채널 삭제
        removeChannels(clientIds);

        gameRoomService.endGame(gameRoomId);

        redisService.saveData(firstId, String.valueOf(UserStatus.LOBBY));
        redisService.saveData(secondId, String.valueOf(UserStatus.LOBBY));
        redisService.saveData(thirdId, String.valueOf(UserStatus.LOBBY));
    }

    public void gameDefeatedProcess(String gameRoomId) {
        Gson gson = new Gson();

        GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);

        String firstId = gameRoom.getRound().getFirstRoom().getPlayer();
        String secondId = gameRoom.getRound().getSecondRoom().getPlayer();
        String thirdId = gameRoom.getRound().getThirdRoom().getPlayer();

        String firstClientId = redisService.getClientId(firstId);
        String secondClientId = redisService.getClientId(secondId);
        String thirdClientId = redisService.getClientId(thirdId);

        GameOverDto userLeaveGameOverDto = GameOverDto.builder()
                .status("DEFEATED")
                .build();

        String userLeaveMessage = gson.toJson(userLeaveGameOverDto);
        String data = gson.toJson(userLeaveMessage);

        sendDataToClient(firstClientId, data);
        sendDataToClient(secondClientId, data);
        sendDataToClient(thirdClientId, data);

        String[] clientIds = {firstClientId, secondClientId, thirdClientId};

        //채널 닫기
        channelClose(clientIds);

        //채널 삭제
        removeChannels(clientIds);

        //clientSession 삭제
        redisService.removeClientSession(firstId);
        redisService.removeClientSession(secondId);
        redisService.removeClientSession(thirdId);

        gameRoomService.endGame(gameRoomId);

        redisService.saveData(firstId, String.valueOf(UserStatus.LOBBY));
        redisService.saveData(secondId, String.valueOf(UserStatus.LOBBY));
        redisService.saveData(thirdId, String.valueOf(UserStatus.LOBBY));
    }

    public void channelClose(String[] clientIds) {
        for (int i = 0; i < clientIds.length; i++) {
            Channel channel = channelManager.getChannel(clientIds[i]);
            if (channel.isActive()) channel.close();
        }
    }

    public void removeChannels(String[] clients) {
        for (int i = 0; i < clients.length; i++) {
            channelManager.removeChannel(clients[i]);
        }
    }
}
