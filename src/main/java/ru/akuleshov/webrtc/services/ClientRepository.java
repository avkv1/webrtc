package ru.akuleshov.webrtc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import ru.akuleshov.webrtc.controllers.ApiController;
import ru.akuleshov.webrtc.entity.Channel;
import ru.akuleshov.webrtc.entity.Client;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ClientRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRepository.class);

    private final Map<Client, WebSocketSession> clients = new ConcurrentHashMap<>();

    public Map<Client, WebSocketSession> findAllByChannel(Channel channel) {
        return clients.entrySet().stream()
                .filter(map -> channel.equals(map.getKey().getChannel()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public Map<Client, WebSocketSession> findAll() {
        return clients;
    }

    public Client findClientByUsername(String username) {
        Optional<Map.Entry<Client, WebSocketSession>> clientEntry = clients.entrySet().stream()
                .filter(map -> username.equalsIgnoreCase(map.getKey().getUsername()))
                .findFirst();

        if (!clientEntry.isPresent()) return null;

        return clientEntry.get().getKey();
    }

    public WebSocketSession findWebSocketSessionByUsername(String username) {

        Optional<Map.Entry<Client, WebSocketSession>> clientEntry = clients.entrySet().stream()
                .filter(map -> username.equalsIgnoreCase(map.getKey().getUsername()))
                .findFirst();

        if (!clientEntry.isPresent()) return null;

        return clientEntry.get().getValue();
    }

    public Client findClientBySessionId(String sessionId) {
        Optional<Map.Entry<Client, WebSocketSession>> clientEntry = clients.entrySet().stream()
                .filter(map -> sessionId.equalsIgnoreCase(map.getValue().getId()))
                .findFirst();

        if (!clientEntry.isPresent()) return null;

        return clientEntry.get().getKey();
    }


    public void insert(Client client, WebSocketSession session) {
        clients.put(client, session);
    }

    public boolean updateSession(String username, WebSocketSession session) {

        Optional<Map.Entry<Client, WebSocketSession>> clientEntry = clients.entrySet().stream()
                .filter(map -> username.equalsIgnoreCase(map.getKey().getUsername()))
                .findFirst();

        if (!clientEntry.isPresent()) return true;

        clientEntry.get().setValue(session);

        return true;
    }
}
