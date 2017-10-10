package ru.akuleshov.webrtc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;
import ru.akuleshov.webrtc.entity.Channel;
import ru.akuleshov.webrtc.entity.ChannelStatus;
import ru.akuleshov.webrtc.entity.Client;
import ru.akuleshov.webrtc.services.ChannelService;
import ru.akuleshov.webrtc.services.ClientRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    ChannelService channelService;
    @Autowired
    ClientRepository clientRepository;

    @RequestMapping(value = "/api/channels", method = RequestMethod.GET)
    public List<String> getChannels() {
        return channelService.getChannels().stream()
                .map(map -> map.getName())
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/channel/{channel}", method = RequestMethod.POST)
    public void addChannel(@PathVariable String channel) {
        channelService.addChannel(channel);
    }

    @RequestMapping(value = "/api/status/{channel}", method = RequestMethod.GET)
    public ChannelStatus getStatus(@PathVariable String channel) {
        Channel channelEntity = channelService.getChannels().stream()
                .filter(v -> channel.equalsIgnoreCase(v.getName()))
                .findFirst()
                .orElse(null);

        if (channelEntity == null) throw new ResourceNotFoundException();

        Map<Client, WebSocketSession> clientsMap = clientRepository.findAllByChannel(channelEntity);
        long online = clientsMap.entrySet().stream()
                .filter(map -> map.getValue().isOpen())
                .count();

        List<String> clientsList = clientRepository.findAllByChannel(channelEntity).entrySet().stream()
                .filter(map -> map.getValue().isOpen())
                .map(map -> map.getKey().getUsername())
                .collect(Collectors.toList());


        ChannelStatus status = new ChannelStatus();
        status.setName(channelEntity.getName());
        status.setOnline(online);
        status.setClients(clientsList);

        return status;
    }


}