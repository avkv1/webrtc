package ru.akuleshov.webrtc.entity;

public class Channel {

    private String name;

    public Channel() {
    }

    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
