package ru.akuleshov.webrtc.services;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.akuleshov.webrtc.entity.Channel;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelServiceTest {

    @Autowired
    ChannelService channelService;

    @Test
    public void getChannels() throws Exception {
        channelService.setChannels(Lists.newArrayList(new Channel("test1"), new Channel("test2")));
        List<String> channels = channelsToStringList(channelService.getChannels());

        assertTrue(channels.contains("test1") && channels.contains("test2"));
    }

    @Test
    public void addChannelWithString() throws Exception {
        channelService.addChannel("test123");
        List<String> channels = channelsToStringList(channelService.getChannels());
        channels.contains("test123");
    }

    @Test
    public void addChannelWithChannelObject() throws Exception {
        channelService.addChannel(new Channel("test456"));
        List<String> channels = channelsToStringList(channelService.getChannels());
        channels.contains("test456");
    }

    private List<String> channelsToStringList(List<Channel> channels){
        List<String> stringList = channels.stream()
                .map(v -> v.getName())
                .collect(Collectors.toList());

        return stringList;
    }

}