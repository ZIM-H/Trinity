package com.trinity.trinity.webSocket;

import com.trinity.trinity.client.ClientSession;
import com.trinity.trinity.redisUtil.RedisService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@DependsOn("redisService")
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final RedisService redisService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            if(request.equals("fuck you")) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("fuck you too"));
            }else if (request.equals("허준영")) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("사과해요 나한테!"));
            } else{
                ctx.channel().writeAndFlush(new TextWebSocketFrame("###" + request + "@@@"));
            }

            String clientId = ctx.channel().id().toString();

            ClientSession clientSession = redisService.getClientSession(clientId);
            if(clientSession == null) {
                clientSession = new ClientSession(clientId);
                // 클라이언트 세션을 Redis에 저장
                redisService.saveClient(clientId, clientSession.getClientId());
            }

        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        return;
    }
}
