package com.trinity.trinity.global.webSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinity.trinity.global.dto.ClientSession;
import com.trinity.trinity.domain.control.dto.response.*;
import com.trinity.trinity.domain.control.enums.UserStatus;
import com.trinity.trinity.domain.logic.dto.GameRoom;
import com.trinity.trinity.domain.logic.service.GameRoomService;
import com.trinity.trinity.global.dto.ClientUserId;
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

    static Gson gson = new Gson();

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

            if (requestType.equals("matching")) {
                String userId = jsonObject.get("userId").getAsString();
                String clientId = ctx.channel().id().toString();

                ClientSession clientSession = redisService.getClientSession(userId);
                if (clientSession == null) {
                    clientSession = new ClientSession(userId, clientId);
                    ClientUserId clientUserId = new ClientUserId(clientId, userId);
                    // 클라이언트 세션을 Redis에 저장
                    redisService.saveClient(clientSession);
                    redisService.saveUserId(clientUserId);
                }

                redisService.saveData(userId, String.valueOf(UserStatus.WAITING));

                sendDataToClient(redisService.getClientId(userId), "Connecting SUCCESS!!");

            } else if (requestType.equals("roundEnd")) {
                String gameRoomId = jsonObject.get("gameRoomId").getAsString();
                String roomNum = jsonObject.get("roomNum").getAsString();

                GameRoom gameRoom = gameRoomRedisService.getGameRoom(gameRoomId);
                // 채널 연결확인
                if (userLeaveProcess(gameRoom)) return;

                gameRoomRedisService.updateRoom(gson, jsonObject, roomNum, gameRoomId);

                //3 사람의 모든 데이터가 들어온 경우 true를 반환
                boolean complete = redisService.checkGameRoomAllClear(gameRoomId, roomNum);

                // 모든 데이터를 받았을 경우
                if (complete) {
                    GameRoom beforeGameRoom = gameRoomRedisService.getGameRoom(gameRoomId);

                    // 소행성 충돌 여부 데이터 뽑기
                    boolean asteroidConflict = false;
                    int barrierStatus = beforeGameRoom.getThirdRoom().getBarrierStatus();
                    boolean asteroidStatus = beforeGameRoom.getThirdRoom().isAsteroidStatus();
                    boolean asteroidDestroy = beforeGameRoom.getThirdRoom().isAsteroidDestroyTry();

                    if (asteroidStatus && barrierStatus < 2 && !asteroidDestroy) asteroidConflict = true;

                    // 1. 13일차 - 위에서 checkEndGame으로 체크
                    // 2. 중간에 게임에서 실패
                    // 3. 누군가 게임에서 나간 경우
                    // --> gameOver = true
                    String gameOver = gameRoomService.gameLogic(beforeGameRoom);

                    // gameOver가 아닌 경우
                    if (gameOver.equals("alive")) {

                        if (gameRoomService.checkEndGame(beforeGameRoom)) {
                            gameVictoryProcess(beforeGameRoom);
                            return;
                        }

                        GameRoom morningRoom = gameRoomRedisService.getGameRoom(gameRoomId);

                        gameRoomService.morningGameLogic(morningRoom);

                        //채널 연결 확인
                        if (userLeaveProcess(morningRoom)) return;

                        String[] users = getUsers(morningRoom);

                        String[] clients = redisService.getClientIdList(users);

                        String[] rooms = getMorningRoom(asteroidConflict, morningRoom);

                        for (int i = 0; i < 3; i++) sendDataToClient(clients[i], rooms[i]);

                    } else {
                        // 게임 오버 true인 경우
                        log.info("게임 패배");
                        gameDefeatedProcess(beforeGameRoom, gameOver);
                    }
                }
            } else if(requestType.equals("ping")) {
                sendDataToClient(ctx.channel().id().toString(), "pong");
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
                log.info(ctx.channel().id().toString() + " channel response None");
                String userId = redisService.getUserId(ctx.channel().id().toString());
                //상태 변경, 채널 삭제
                if(userId == null) return;
                if(redisService.getData(userId).equals("WAITING")) {
                    redisService.removeMatching(userId); // 유저 상태 변경 --> LOBBY로
                    redisService.removeUserId(ctx.channel().id().toString()); // clientId : userId 삭제
                    channelManager.removeChannel(ctx.channel().id().toString());
                }
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                log.info("send request notice alive");
                ctx.writeAndFlush(new PingMessage());
            }
        }
    }

    private String[] getMorningRoom(boolean asteroidConflict, GameRoom morningRoom) {
        String[] rooms = new String[3];

        CommonDataDto commonDataDto = CommonDataDto.builder()
                .conflictAsteroid(asteroidConflict)
                .build();

        commonDataDto.setCommonDto(morningRoom);

        //각 방정보를 뽑아와서 각 플레이어에게 보내기
        FirstRoomResponseDto firstRoomResponseDto = FirstRoomResponseDto.builder()
                .type("nextRound")
                .build();
        firstRoomResponseDto.modifyFirstRoomResponseDto(commonDataDto, morningRoom);
        rooms[0] = gson.toJson(firstRoomResponseDto);

        SecondRoomResponseDto secondRoomResponseDto = SecondRoomResponseDto.builder()
                .type("nextRound")
                .build();
        secondRoomResponseDto.modifySecondRoomResponseDto(commonDataDto, morningRoom);
        rooms[1] = gson.toJson(secondRoomResponseDto);

        ThirdRoomResponseDto thirdRoomResponseDto = ThirdRoomResponseDto.builder()
                .type("nextRound")
                .build();
        thirdRoomResponseDto.modifyThirdRoomResponseDto(commonDataDto, morningRoom);
        rooms[2] = gson.toJson(thirdRoomResponseDto);

        return rooms;
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

    private boolean ChannelAlive(String clientId) {
        Channel channel = channelManager.getChannel(clientId);
        if (channel.isActive()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean userLeaveProcess(GameRoom gameRoom) {
        String gameRoomId = gameRoom.getGameRoomId();

        boolean checkActiveAll = true;

        String[] users = getUsers(gameRoom);

        String[] clients = redisService.getClientIdList(users);

        for (String client : clients) {
            if (!ChannelAlive(client)) {
                checkActiveAll = false;
                break;
            }
        }

        if (!checkActiveAll) {

            GameOverDto userLeaveGameOverDto = GameOverDto.builder()
                    .status("userLeave")
                    .build();

            String data = gson.toJson(userLeaveGameOverDto);

            for (String client : clients) sendDataToClient(client, data);

            gameRoomService.endGame(gameRoomId);

            //채널 닫기
            channelClose(clients);

            //채널 삭제
            removeChannels(clients);

            //clientSession 삭제
            redisService.removeClientListSession(users);

            redisService.backToLooby(users);

            return true;
        } else {
            return false;
        }
    }

    private String[] getUsers(GameRoom gameRoom) {
        String[] users = new String[3];
        users[0] = gameRoom.getFirstRoom().getUserId();
        users[1] = gameRoom.getSecondRoom().getUserId();
        users[2] = gameRoom.getThirdRoom().getUserId();

        return users;
    }

    private void gameVictoryProcess(GameRoom gameRoom) {
        String[] users = getUsers(gameRoom);

        String[] clients = redisService.getClientIdList(users);

        GameOverDto gameVictoryOverDto = GameOverDto.builder()
                .status("VICTORY")
                .build();

        String data = gson.toJson(gameVictoryOverDto);

        for (String client : clients) sendDataToClient(client, data);

        //채널 닫기
        channelClose(clients);

        //채널 삭제
        removeChannels(clients);

        //clientSession 삭제
        redisService.removeClientListSession(users);

        gameRoomService.endGame(gameRoom.getGameRoomId());

        redisService.backToLooby(users);
    }

    private void gameDefeatedProcess(GameRoom gameRoom, String reason) {
        String[] users = getUsers(gameRoom);

        String[] clients = redisService.getClientIdList(users);

        GameOverDto gameDefeatedOverDto = GameOverDto.builder()
                .status("DEFEATED")
                .reason(reason)
                .build();

        String data = gson.toJson(gameDefeatedOverDto);

        for (String client : clients) sendDataToClient(client, data);

        //채널 닫기
        channelClose(clients);

        //채널 삭제
        removeChannels(clients);

        //clientSession 삭제
        redisService.removeClientListSession(users);

        gameRoomService.endGame(gameRoom.getGameRoomId());

        redisService.backToLooby(users);

    }

    private void channelClose(String[] clientIds) {
        for (int i = 0; i < clientIds.length; i++) {
            Channel channel = channelManager.getChannel(clientIds[i]);
            if (channel.isActive()) channel.close();
        }
    }

    private void removeChannels(String[] clients) {
        for (int i = 0; i < clients.length; i++) {
            channelManager.removeChannel(clients[i]);
        }
    }


}
