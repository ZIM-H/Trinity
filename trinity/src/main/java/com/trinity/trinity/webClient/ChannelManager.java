package com.trinity.trinity.webClient;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class ChannelManager {
//    private final ChannelManager INSTANCE = new ChannelManager();
    private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    public void addChannel(String id, Channel channel) {
        System.out.println("Channel Manager 안 : " + channel);
        channels.put(id, channel);
        for (String clientId : channels.keySet()) {
            Channel eachChannel = channels.get(clientId);
            System.out.println("for문의 안쪽이다!!!!!!!!!!!!!!!!! :" + eachChannel);
            // do something with id and channel...
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

    // other methods to manipulate channels...
}