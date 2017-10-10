package ru.akuleshov.webrtc.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.akuleshov.webrtc.entity.Client;
import ru.akuleshov.webrtc.entity.SignalMessage;
import ru.akuleshov.webrtc.services.ClientRepository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpyBean(SignalingSocketHandler.class)
@SpringBootTest
public class SignalingSocketHandlerTest {

    @Autowired
    SignalingSocketHandler signalingSocketHandler;

    @MockBean
    ClientRepository clientRepository;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockCreation() {
        assertNotNull(signalingSocketHandler);
        assertNotNull(clientRepository);
    }

    @Test
    public void testLoginPassed() throws Exception {

        String username = "test";
        WebSocketSession fakeWebsocketSession = new DummyWebSocketSession();

        when(clientRepository.updateSession(username, fakeWebsocketSession))
                .thenReturn(true);
        when(clientRepository.findWebSocketSessionByUsername(username))
                .thenReturn(fakeWebsocketSession);


        SignalMessage out = new SignalMessage();
        out.setType(signalingSocketHandler.LOGIN_TYPE);
        out.setDest(username);
        out.setData(username);

        String stringifiedJSON = objectMapper.writeValueAsString(out);

        TextMessage inputMessage = new TextMessage(stringifiedJSON);

        signalingSocketHandler.handleTextMessage(fakeWebsocketSession, inputMessage);
        verify(clientRepository).findWebSocketSessionByUsername(username);
        verify(clientRepository).updateSession(username, fakeWebsocketSession);

    }


    @Test
    public void testRTCPassed() throws Exception {

        String username = "test";
        String sessionId = "12345";
        DummyWebSocketSession dummyWebSocketSession = new DummyWebSocketSession();
        dummyWebSocketSession.setId(sessionId);
        dummyWebSocketSession.setOpen(true);
        WebSocketSession fakeWebsocketSession = dummyWebSocketSession;

        when(clientRepository.findWebSocketSessionByUsername(username))
                .thenReturn(fakeWebsocketSession);
        when(clientRepository.findClientBySessionId(sessionId))
                .thenReturn(new Client(username));


        SignalMessage out = new SignalMessage();
        out.setType(signalingSocketHandler.RTC_TYPE);
        out.setDest(username);
        out.setData(null);

        String stringifiedJSON = objectMapper.writeValueAsString(out);

        TextMessage inputMessage = new TextMessage(stringifiedJSON);

        signalingSocketHandler.handleTextMessage(fakeWebsocketSession, inputMessage);
        verify(clientRepository).findWebSocketSessionByUsername(username);
        verify(clientRepository).findClientBySessionId(sessionId);

    }

}