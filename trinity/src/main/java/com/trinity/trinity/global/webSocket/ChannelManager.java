package com.trinity.trinity.global.webSocket;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class ChannelManager {
    private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    public void addChannel(String id, Channel channel) {
        channels.put(id, channel);
    }

    public void removeChannel(String id) {
        channels.remove(id);
    }

    public Channel getChannel(String clientId) {
        Channel channel = channels.get(clientId);
        return channel;
    }
}