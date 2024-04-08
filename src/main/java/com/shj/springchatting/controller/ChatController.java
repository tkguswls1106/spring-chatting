package com.shj.springchatting.controller;

import com.shj.springchatting.dto.chat.ChatDto;
import com.shj.springchatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final RabbitTemplate rabbitTemplate;
    private final ChatService chatService;

    private final static String CHAT_QUEUE_NAME = "chat.queue";
    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

    // 프론트엔드 주소: /exchange/chat.exchange/room.{roomId}
    // 백엔드 주소: /pub/chat.message


    // @MessageMapping로 웹소켓 메시지를 처리.
    @MessageMapping("chat.message")  // 프론트엔드에서 '/pub/chat.message.{roomId}'로 호출시 이 브로커에서 처리.
    public void sendMessage(@Payload ChatDto chatDto) {
        ChatDto responseChatDto = chatService.createChat(chatDto);
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + responseChatDto.getRoomId(), responseChatDto);  // '/exchange/chat.exchange/room.{roomId}' 이 클라이언트 주소로 상시 켜져(구독되어)있는 스톰프(웹소켓) 프론트엔드에 메세지 전달.
    }


    // RabbicConfig에서 미리 chat.queue를 만들어두고 root.*을 라우팅키로 사용하여 exchange에 연결시켜 놓았기 때문에,
    // exchange로 들어오는 모든 채팅방의 메시지를 receive()를 통해서 처리할수있다.
    @RabbitListener(queues = CHAT_QUEUE_NAME)  // 기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
    public void receive(ChatDto chatDto){
        System.out.println("receive message: " + chatDto.getMessage());
    }
}
