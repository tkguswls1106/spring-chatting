package com.shj.springchatting.controller;

import com.shj.springchatting.dto.chat.ChatDto;
import com.shj.springchatting.dto.chat.ChatRequestDto;
import com.shj.springchatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;


    @MessageMapping("/send.message")  // 프론트엔드의 '/pub/send.message' 요청을 백엔드에서 받음.
    public void handleIncomeMessage(@Payload ChatDto chatDto) {
        log.info("Message: {}", chatDto.getMessage());

        ChatDto responseChatDto = chatService.createChat(chatDto);

        messagingTemplate.convertAndSend("/queue/" + chatDto.getRoomId(), responseChatDto);
    }






    private final static String CHAT_QUEUE_NAME = "chat.queue";
    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

    // 프론트엔드 주소: /sub/chat/Room/
    // 백엔드 주소: /pub/enterUser


    // @MessageMapping로 웹소켓 메시지를 처리.
    @MessageMapping("chat.enter.{chatRoomId}")  // 프론트엔드에서 '/pub/chat.enter.{chatRoomId}'로 호출시 이 브로커에서 처리.
    public void enterUser(@DestinationVariable String chatRoomId, @Payload ChatDTO chat) {
        chat.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"))));
        chat.setMessage(chat.getSender() + " 님 입장!!");
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "Room." + chatRoomId, chat);  // '/exchange/chat.exchange/Room.{roomId}' 이 클라이언트 주소로 상시 켜져(구독되어)있는 스톰프(웹소켓) 프론트엔드에 메세지 전달.
    }

    // @MessageMapping로 웹소켓 메시지를 처리.
    @MessageMapping("chat.message.{chatRoomId}")  // 프론트엔드에서 '/pub/chat.message.{chatRoomId}'로 호출시 이 브로커에서 처리.
    public void sendMessage(@DestinationVariable String chatRoomId, @Payload ChatDTO chat) {
        chat.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"))));
        chat.setMessage(chat.getMessage());
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "Room." + chatRoomId, chat);  // '/exchange/chat.exchange/Room.{roomId}' 이 클라이언트 주소로 상시 켜져(구독되어)있는 스톰프(웹소켓) 프론트엔드에 메세지 전달.
    }


    // RabbicConfig에서 미리 chat.queue를 만들어두고 root.*을 라우팅키로 사용하여 exchange에 연결시켜 놓았기 때문에,
    // exchange로 들어오는 모든 채팅방의 메시지를 receive()를 통해서 처리할수있다.
    @RabbitListener(queues = CHAT_QUEUE_NAME)  // 기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
    public void receive(ChatDTO chatDTO){
        System.out.println("receive: " + chatDTO.getSender() + " / " + chatDTO.getMessage());
    }
}
