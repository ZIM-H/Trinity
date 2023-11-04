package com.trinity.trinity.webSocket;

import com.trinity.trinity.gameRoom.service.GameRoomService;
import com.trinity.trinity.redisUtil.GameRoomRedisService;
import com.trinity.trinity.redisUtil.RedisService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;

@DependsOn("webSocketFrameHandler")
@RequiredArgsConstructor
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final String WEBSOCKET_PATH = "/websocket";
    private final SslContext sslCtx;
    private final RedisService redisService;
    private final GameRoomService gameRoomService;
    private final GameRoomRedisService gameRoomRedisService;

    @Override
    public void initChannel(SocketChannel ch){
        ChannelPipeline pipeline = ch.pipeline();
        if(sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new WebSocketFrameHandler(redisService, gameRoomRedisService, gameRoomService));
    }
}