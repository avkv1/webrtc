package ru.akuleshov.webrtc.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.akuleshov.webrtc.socket.SignalingSocketHandler;

@Component
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    SignalingSocketHandler signalingSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//        webSocketHandlerRegistry
//                .addHandler(signalingSocketHandler(), "/signal")
//                .setAllowedOrigins("*");
        webSocketHandlerRegistry
                .addHandler(signalingSocketHandler, "/signal")
                .setAllowedOrigins("*");
    }

//    @Bean
//    public WebSocketHandler signalingSocketHandler() {
//        return new SignalingSocketHandler();
//    }
}
