package com.shj.springchatting.service.impl;

import com.shj.springchatting.domain.chat.Chat;
import com.shj.springchatting.domain.chat.MessageType;
import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.mapping.UserRoom;
import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.dto.chat.ChatDto;
import com.shj.springchatting.repository.ChatRepository;
import com.shj.springchatting.service.ChatService;
import com.shj.springchatting.service.RoomService;
import com.shj.springchatting.service.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final RoomService roomService;
    private final RoomServiceImpl roomServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final UserRoomService userRoomService;


    @Transactional
    @Override
    public ChatDto createChat(ChatDto chatDto) {
        User user = userServiceImpl.findUser(chatDto.getSenderId());
        chatDto.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. M. d. a h:mm").withLocale(Locale.forLanguageTag("ko"))));

        if(chatDto.getMessageType().equals(MessageType.ENTER)) {  // 방 입장의 경우
            if(chatDto.getRoomId() == null) {  // 방의 첫번째 입장의 경우, 방 생성을 해주어야함.
                if(chatDto.getRoomName() == null) throw new RuntimeException("ERROR - 방 생성시에는 반드시 roomName을 함께 보내주어야합니다.");
                Room newRoom = roomService.createRoom(chatDto.getRoomName(), user);
                chatDto.setRoomId(newRoom.getRoomId());
                chatDto.setMessage("'" + user.getNickname() + "'님이 방을 생성하였습니다.");  // 방 신규 생성시에는 굳이 나타내줄 필요 없긴함. (차후 제거할 코드줄임.)

                Chat chat = chatDto.toEntity(newRoom);
                chatRepository.save(chat);
                return chatDto;
            }
            else {  // 방이 이미 생성되어 있는 경우
                chatDto.setMessage("'" + user.getNickname() + "'님이 방에 참가하였습니다.");
            }
        }
        else if(chatDto.getMessageType().equals(MessageType.LEAVE)) {  // 방 탈퇴의 경우
            if(chatDto.getRoomId() == null) throw new RuntimeException("ERROR - 방 삭제시에는 반드시 roomId을 함께 보내주어야합니다.");
            Room room = roomServiceImpl.findRoom(chatDto.getRoomId());
            userRoomService.deleteUserRoom(user, room);
            chatDto.setMessage("'" + user.getNickname() + "'님이 방에서 퇴장하셨습니다.");
        }
        else {  // 방 채팅의 경우 (MessageType.TALK 일때)
            if(chatDto.getRoomId() == null) throw new RuntimeException("ERROR - 채팅시에는 반드시 roomId을 함께 보내주어야합니다.");
            if(chatDto.getMessage() == null) throw new RuntimeException("ERROR - 채팅시에는 반드시 message을 함께 보내주어야합니다.");
            chatDto.setMessage("'" + user.getNickname() + "'님의 메세지: '" + chatDto.getMessage() + "'");  //  (차후 수정할 코드줄임.)
        }

        Chat chat = chatDto.toEntity(roomServiceImpl.findRoom(chatDto.getRoomId()));
        chatRepository.save(chat);
        return chatDto;
    }
}