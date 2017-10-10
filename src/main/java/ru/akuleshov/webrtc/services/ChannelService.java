package ru.akuleshov.webrtc.services;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;
import ru.akuleshov.webrtc.entity.Channel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChannelService {

    private final List<Channel> channels = new CopyOnWriteArrayList<>();

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> list) {
        channels.clear();
        channels.addAll(list);
    }

    public void addChannel(Channel chan) {
        if (!channels.contains(chan)) {
            channels.add(chan);
        }
    }

    public Channel addChannel(String chanName) {

        Channel chan = channels.stream()
                .filter(v -> chanName.equalsIgnoreCase(v.getName()))
                .findFirst()
                .orElse(null);

        if (chan != null) return chan;

        chan = new Channel();
        chan.setName(StringEscapeUtils.escapeHtml(chanName));
        channels.add(chan);

        return chan;
    }
}
