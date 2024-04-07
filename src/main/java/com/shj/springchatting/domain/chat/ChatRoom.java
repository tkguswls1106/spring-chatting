package com.shj.springchatting.domain.chat;

import com.shj.springchatting.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {  // 웹소켓 위에 Stomp를 얹어서 사용하게되면, 이전에 웹소켓만 사용할때와는 다르게 session을 가질 필요가 없다.

    private String roomId;  // 채팅방 아이디
    private String roomName;  // 채팅방 이름
    private long userCount;  // 채팅방 인원수
    private HashMap<String, String> userList = new HashMap<String, String>();


    public ChatRoom create(String roomName){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;

        return chatRoom;
    }
}
