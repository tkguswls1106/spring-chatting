package com.shj.springchatting.controller;

import com.shj.springchatting.domain.chat.ChatRoom;
import com.shj.springchatting.repository.ChatRepository;
import com.shj.springchatting.response.ResponseCode;
import com.shj.springchatting.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRepository chatRepository;


    // 채팅 리스트 화면
    @GetMapping("/")
    public ResponseEntity goChatRoom(){
        List<ChatRoom> chatRooms = chatRepository.findAllRoom();
        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS, chatRooms);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity createRoom(@RequestParam String name) {
        ChatRoom room = chatRepository.createChatRoom(name);
        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS, room);
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/userlist")
    public ResponseEntity userList(String roomId) {
        ArrayList<String> list = chatRepository.getUserList(roomId);
        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS, list);
    }
}
