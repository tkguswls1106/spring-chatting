package com.shj.springchatting.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");  // 메시지 구독 url (topic을 구독)
        config.setApplicationDestinationPrefixes("/pub");  // 메시지 발행 url
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // 핸들러를 등록할때 소켓에 접속하기위한 경로 ("/ws")를 함께 설정.
                .setAllowedOriginPatterns("*");  // 다른곳에서 접속이 가능하도록 setAllowedOrigins("*")을 붙여 cors문제를 해결.
        //.withSockJS();
    }
}