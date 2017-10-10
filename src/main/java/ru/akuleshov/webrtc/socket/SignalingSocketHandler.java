package ru.akuleshov.webrtc.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.akuleshov.webrtc.entity.SignalMessage;
import ru.akuleshov.webrtc.services.ClientRepository;

import java.io.IOException;

@Component
public class SignalingSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignalingSocketHandler.class);

    public static final String LOGIN_TYPE = "login";
    public static final String RTC_TYPE = "rtc";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ClientRepository clientRepository;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.debug("HandleTextMessage : {}", message.getPayload());

        SignalMessage signalMessage = objectMapper.readValue(message.getPayload(), SignalMessage.class);

        switch (signalMessage.getType()){
            case LOGIN_TYPE:
                handleLogin(session, (String) signalMessage.getData());
                break;
            case RTC_TYPE:
                handleRTC(session.getId(), signalMessage.getDest(), signalMessage.getData());
                break;
            default:
                throw new UnsupportedOperationException();
        }

    }

    private void handleRTC(String sessionId, String destUsername, Object data) throws IOException {
        WebSocketSession destSocket = clientRepository.findWebSocketSessionByUsername(destUsername);
        if (destSocket != null && destSocket.isOpen()) {

            SignalMessage out = new SignalMessage();
            out.setType(RTC_TYPE);
            out.setDest(clientRepository.findClientBySessionId(sessionId).getUsername());
            out.setData(data);

            String stringifiedJSON = objectMapper.writeValueAsString(out);

            LOGGER.debug("Send message {}", stringifiedJSON);

            destSocket.sendMessage(new TextMessage(stringifiedJSON));
        }
    }

    private void handleLogin(WebSocketSession session, String username) {

        WebSocketSession client = clientRepository.findWebSocketSessionByUsername(username);

        if (client == null || !client.isOpen()) {
            LOGGER.debug("Login (new) {} : OK", username);
            clientRepository.updateSession(username, session);
        } else {
            LOGGER.debug("Login {} : OK", username);
        }

    }
}