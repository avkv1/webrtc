package ru.akuleshov.webrtc.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.akuleshov.webrtc.entity.ChannelStatus;
import ru.akuleshov.webrtc.services.ChannelService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class ApiControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ChannelService channelService;

    private final String testChannelName = UUID.randomUUID().toString();

    @Before
    public void init() {
        channelService.addChannel(testChannelName);
    }


    @Test
    public void getChannelsMock() throws Exception {
        ResponseEntity<String[]> testEntity = this.restTemplate.getForEntity( "/api/channels", String[].class);
        assertTrue(testEntity.getStatusCodeValue() == 200 && testEntity.getBody() != null);
    }


    @Test
    public void getChannels() throws Exception {
        ResponseEntity<String[]> testEntity = this.restTemplate.getForEntity( "/api/channels", String[].class);
        assertTrue(testEntity.getStatusCodeValue() == 200 && testEntity.getBody() != null);
    }

    @Test
    public void addChannel() throws Exception {
        String addChannelName = UUID.randomUUID().toString();
        this.restTemplate.postForEntity( "/api/channel/"+addChannelName, null, null);

        List<String> channels = channelService.getChannels().stream()
                .map(v -> v.getName())
                .collect(Collectors.toList());

        assertTrue(channels.contains(addChannelName));
    }

    @Test
    public void getStatus() throws Exception {
        ResponseEntity<ChannelStatus> testEntity = this.restTemplate.getForEntity( "/api/status/"+testChannelName, ChannelStatus.class);
        assertTrue(testEntity.getStatusCodeValue() == 200 && testEntity.getBody() != null);
    }

}