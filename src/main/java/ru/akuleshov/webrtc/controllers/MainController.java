package ru.akuleshov.webrtc.controllers;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.WebSocketSessionDecorator;
import ru.akuleshov.webrtc.entity.Channel;
import ru.akuleshov.webrtc.entity.Client;
import ru.akuleshov.webrtc.services.ChannelService;
import ru.akuleshov.webrtc.services.ClientRepository;
import ru.akuleshov.webrtc.socket.DummyWebSocketSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    ChannelService channelService;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHome() {
        return "/index.html";
    }

    @RequestMapping(value = "/channel/{channel}", method = RequestMethod.GET, produces = "text/html")
    public @ResponseBody
    String getChat(@PathVariable String channel) throws IOException {
        Channel chan = channelService.addChannel(channel);
        String username = UUID.randomUUID().toString();

        Client client = new Client();
        client.setChannel(chan);
        client.setUsername(username);

        clientRepository.insert(client, new DummyWebSocketSession());

        InputStream inputStream = new ClassPathResource("static/chat.html").getInputStream();
        File chatFile = File.createTempFile("chat", ".html");
        try {
            FileUtils.copyInputStreamToFile(inputStream, chatFile);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }


        String fileBody = Files.toString(chatFile, Charsets.UTF_8);

        return fileBody
                .replace("{%channel%}", StringEscapeUtils.escapeHtml(channel))
                .replace("{%username%}", StringEscapeUtils.escapeHtml(username));
    }

}
