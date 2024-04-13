package com.shj.springchatting.controller;

import com.shj.springchatting.config.RabbitConfig;
import com.shj.springchatting.dto.chat.ChatRequestDto;
import com.shj.springchatting.dto.chat.ChatResponseDto;
import com.shj.springchatting.dto.other.OtherRequestDto;
import com.shj.springchatting.dto.other.OtherResponseDto;
import com.shj.springchatting.response.ResponseCode;
import com.shj.springchatting.response.ResponseData;
import com.shj.springchatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// @CrossOrigin(origins = "*", allowedHeaders = "*")  // SecurityConfig에 대신 만들어주었음.
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final RabbitTemplate rabbitTemplate;
    private final ChatService chatService;

    // - 프론트엔드 주소: /exchange/chat.exchange/room.{roomId}
    // - 백엔드 주소: /pub/chat.message
    // 참고로 RabbitMQ에서는 네이밍을 /sub/chat.exchange/room.{roomId} 로는 불가능함.

    // - 프론트엔드 주소: /exchange/other.exchange/other.{roomId}
    // - 백엔드 주소: /pub/other.doing.{otherId}


    // @MessageMapping로 웹소켓 메시지를 처리.
    @MessageMapping("chat.message")  // 프론트엔드에서 '/pub/chat.message'로 호출시 이 브로커에서 처리.
    public void sendMessage(@Payload ChatRequestDto chatRequestDto) {
        ChatResponseDto chatResponseDto = chatService.createChat(chatRequestDto);
        rabbitTemplate.convertAndSend(RabbitConfig.CHAT_EXCHANGE_NAME, "room." + chatResponseDto.getRoomId(), chatResponseDto);  // '/exchange/chat.exchange/room.{roomId}' 이 클라이언트 주소로 상시 켜져(구독되어)있는 스톰프(웹소켓) 프론트엔드에 메세지 전달.
    }

    @GetMapping("/rooms/{roomId}/chats")
    public ResponseEntity findChatsByRoom(@PathVariable Long roomId) {  // 차후 페이지네이션으로 리팩토링해서 수정할것. & 로그인 사용자 관련 접근예외처리는 차후에 추가할것.
        List<ChatResponseDto> chatResponseDtoList = chatService.findChatsByRoom(roomId);
        return ResponseData.toResponseEntity(ResponseCode.READ_CHATLIST, chatResponseDtoList);
    }


    // 예시 용도
    @MessageMapping("other.doing.{otherId}")  // 프론트엔드에서 '/pub/other.doing.{otherId}'로 호출시 이 브로커에서 처리.
    public void sendOtherData(@DestinationVariable String otherId, @Payload OtherRequestDto otherRequestDto) {
        String data = "'" + otherRequestDto.getSenderName() + "'님의 데이터: '" + otherRequestDto.getData() + "'";  //  (차후 수정할 코드줄임.)
        LocalDateTime createdTime = LocalDateTime.now();
        OtherResponseDto otherResponseDto = new OtherResponseDto(otherRequestDto, Long.valueOf(otherId), data, createdTime);
        rabbitTemplate.convertAndSend(RabbitConfig.OTHER_EXCHANGE_NAME, "other." + otherResponseDto.getOtherId(), otherResponseDto);  // '/exchange/other.exchange/other.{otherId}' 이 클라이언트 주소로 상시 켜져(구독되어)있는 스톰프(웹소켓) 프론트엔드에 메세지 전달.
    }


//    // RabbicConfig에서 미리 chat.queue를 만들어두고 root.*을 라우팅키로 사용하여 exchange에 연결시켜 놓았기 때문에,
//    // exchange로 들어오는 모든 채팅방의 메시지를 receive()를 통해서 처리할수있다.
//    // 이는 기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리.
//    @RabbitListener(queues = RabbitConfig.CHAT_QUEUE_NAME)  // 이를 사용하면, 메시지가 프로듀서(백엔드)에서 컨슈머(프론트엔드)로 이동하는 과정에서, 큐에 도착할 때 메소드가 자동 호출되도록 할 수 있다. (즉, 백엔드에서 메세지 발행된 후 호출됨.)
//    public void receive(ChatResponseDto chatResponseDto){
//        System.out.println("queue receive messageType & message: " + chatResponseDto.getMessageType() + " / " + chatResponseDto.getMessage());
//    }
}
