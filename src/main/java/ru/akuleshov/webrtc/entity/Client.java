package ru.akuleshov.webrtc.entity;

import org.springframework.web.socket.WebSocketSession;

public class Client {

    private String username;
    private Channel channel;
    private long created;

    public Client() {
        this.created = System.currentTimeMillis();
    }

    public Client(String name) {
        this();
        this.username = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
