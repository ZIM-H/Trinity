package com.trinity.trinity.webSocket;

import com.trinity.trinity.gameRoom.service.GameRoomService;
import com.trinity.trinity.redisUtil.GameRoomRedisService;
import com.trinity.trinity.redisUtil.RedisService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class WebSocketServer {
    static final boolean SSL = System.getProperty("ssl") != null;
    private final RedisService redisService;
    private final GameRoomService gameRoomService;
    private final GameRoomRedisService gameRoomRedisService;
    private final int PORT;

    public WebSocketServer(RedisService redisService, GameRoomService gameRoomService, GameRoomRedisService gameRoomRedisService, @Value("${websocket.port}") int PORT) {
        this.redisService = redisService;
        this.gameRoomService = gameRoomService;
        this.gameRoomRedisService = gameRoomRedisService;
        this.PORT = PORT;
    }

    public void start() throws Exception {
        SslContext sslCtx = null;
        if(SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new WebSocketServerInitializer(sslCtx, redisService, gameRoomService, gameRoomRedisService));

            Channel ch = b.bind(PORT).sync().channel();

            ch.closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}