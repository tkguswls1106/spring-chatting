package com.shj.springchatting.controller;

import com.shj.springchatting.dto.chat.ChatDTO;
import com.shj.springchatting.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository repository;
    private final SimpMessageSendingOperations template;

    // 프론트엔드 주소: /sub/chat/room/
    // 백엔드 주소: /pub/enterUser


    @MessageMapping("/enterUser")  // @MessageMapping로 웹소켓 메시지를 처리.
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {
        repository.plusUserCnt(chat.getRoomId());
        String userUUID = repository.addUser(chat.getRoomId(), chat.getSender());

        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + " 님 입장!!");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);  // "/sub/chat/room/" 이 클라이언트 주소로 상시 켜져있는 스톰프(웹소켓) 서버에 전달.
    }

    @MessageMapping("/sendMessage")  // @MessageMapping로 웹소켓 메시지를 처리.
    public void sendMessage(@Payload ChatDTO chat) {
        log.info("CHAT: {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);  // "/sub/chat/room/" 이 클라이언트 주소로 상시 켜져있는 스톰프(웹소켓) 서버에 전달.
    }
}
