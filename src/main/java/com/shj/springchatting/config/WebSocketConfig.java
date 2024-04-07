package com.shj.springchatting.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;  // 참고로 WebSockChatHandler가 아니니 헷갈리지 말자!


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(webSocketHandler, "/ws")  // 핸들러를 등록할때 소켓에 접속하기위한 경로 ("/ws")를 함께 설정.
                .setAllowedOrigins("*");  // 다른곳에서 접속이 가능하도록 setAllowedOrigins("*")을 붙여 cors문제를 해결.
    }
}