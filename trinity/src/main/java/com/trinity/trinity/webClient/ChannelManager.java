package com.trinity.trinity.webClient;

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
        for (String clientId : channels.keySet()) {
            Channel eachChannel = channels.get(clientId);
        }
    }

    public void removeChannel(String id) {
        channels.remove(id);
    }

    public Channel getChannel(String clientId) {
        Channel channel = channels.get(clientId);
        return channel;
    }

    public ConcurrentMap<String, Channel> getChannels() {
        return channels;
    }

    public boolean CheckContainKey(String clientId) {
        return channels.containsKey(clientId);
    }
}