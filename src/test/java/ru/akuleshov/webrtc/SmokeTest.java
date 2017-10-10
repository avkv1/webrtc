package ru.akuleshov.webrtc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.akuleshov.webrtc.controllers.ApiController;
import ru.akuleshov.webrtc.controllers.MainController;
import ru.akuleshov.webrtc.services.ChannelService;
import ru.akuleshov.webrtc.services.ClientRepository;
import ru.akuleshov.webrtc.socket.SignalingSocketHandler;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    private MainController mainController;
    @Autowired
    private ApiController apiController;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SignalingSocketHandler signalingSocketHandler;


    @Test
    public void contextLoads() throws Exception {
        assertThat(mainController).isNotNull();
        assertThat(apiController).isNotNull();
        assertThat(channelService).isNotNull();
        assertThat(clientRepository).isNotNull();
        assertThat(signalingSocketHandler).isNotNull();
    }
}