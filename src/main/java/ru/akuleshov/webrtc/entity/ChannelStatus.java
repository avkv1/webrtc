package ru.akuleshov.webrtc.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChannelStatus implements Serializable{

    private String name;
    private long online;
    private List<String> clients = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOnline() {
        return online;
    }

    public void setOnline(long online) {
        this.online = online;
    }

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "ChannelStatus{" +
                "name='" + name + '\'' +
                ", online=" + online +
                ", clients=" + clients +
                '}';
    }
}
