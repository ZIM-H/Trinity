package com.trinity.trinity.webSocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinity.trinity.client.ClientSession;
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
    private ConcurrentMap<String, Channel> activeChannels = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String text = ((TextWebSocketFrame) frame).text();
            
            JsonObject jsonObject = new JsonParser().parse(text).getAsJsonObject();
            String requestType = jsonObject.get("type").getAsString();
            if(requestType.equals("matching")){
                String userId = jsonObject.get("userId").getAsString();
                String clientId = ctx.channel().id().toString();
                String clientAddress = ctx.channel().remoteAddress().toString();
                ClientSession clientSession = redisService.getClientSession(userId);
                if(clientSession == null) {
                    clientSession = new ClientSession(userId, clientId, clientAddress);
                    // 클라이언트 세션을 Redis에 저장
                    redisService.saveClient(clientSession);
                }
                sendDataToClient(clientId, "Connecting SUCCESS!!");
            } else if (requestType.equals("roundEnd")) {
            }


        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            activeChannels.put(ctx.channel().id().toString(), ctx.channel());
        }
        return;
    }

    public void sendDataToClient(String clientId, String data) {
        Channel channel = activeChannels.get(clientId);
        if (channel != null) {
            TextWebSocketFrame textFrame = new TextWebSocketFrame(data);
            channel.writeAndFlush(textFrame);
        }
    }
}
