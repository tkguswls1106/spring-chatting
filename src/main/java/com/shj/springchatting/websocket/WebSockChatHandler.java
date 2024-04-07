package com.shj.springchatting.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shj.springchatting.domain.chat.ChatMessage;
import com.shj.springchatting.domain.chat.ChatRoom;
import com.shj.springchatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSockChatHandler extends TextWebSocketHandler {
    // 채팅을 위해서는 텍스트가 적합하므로 BinaryWebSocketHandler 대신 TextWebSocketHandler를 상속받는 핸들러를 작성해줌.

    private final ObjectMapper objectMapper;
    private final ChatService chatService;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload: {}", payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    }
}