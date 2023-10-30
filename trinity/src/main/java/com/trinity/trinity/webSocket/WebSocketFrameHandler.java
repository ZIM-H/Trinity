package com.trinity.trinity.webSocket;

import com.trinity.trinity.client.ClientSession;
import com.trinity.trinity.redisUtil.RedisService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;


public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final RedisService redisService = new RedisService(new RedisTemplate<String, String>());
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            System.out.println(request);
//            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
            if(request.equals("fuck you")) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("fuck you too"));
            }else if (request.equals("허준영")) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame("사과해요 나한테!"));
            } else{
                ctx.channel().writeAndFlush(new TextWebSocketFrame("###" + request + "@@@"));
            }

            String clientId = ctx.channel().id().toString();
            System.out.println(clientId);
            // 클라이언트 세션을 Redis에서 가져옵니다.

            try{
                ClientSession clientSession = redisService.getClientSession(clientId);
            } catch(NullPointerException e){
                System.out.println("error");
                ClientSession clientSession = new ClientSession(clientId);
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
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            ctx.pipeline().remove(WebSocketIndexPageHandler.class);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
