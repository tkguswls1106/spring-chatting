package com.shj.springchatting.service.impl;

import com.shj.springchatting.domain.chat.Chat;
import com.shj.springchatting.domain.chat.MessageType;
import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.dto.chat.ChatDto;
import com.shj.springchatting.repository.ChatRepository;
import com.shj.springchatting.repository.UserRoomRepository;
import com.shj.springchatting.service.ChatService;
import com.shj.springchatting.service.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRoomRepository userRoomRepository;
    private final RoomServiceImpl roomServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final UserRoomService userRoomService;


    @Transactional
    @Override
    public ChatDto createChat(ChatDto chatDto) {  // 다른 조건에 대한 여부는 차후 추가하면 됨. (ex: 방 최대인원에 대한 입장제한 or 재입장 관련 or 방에 남은 인원0명 또는 방장 퇴장의 경우 방삭제 ... 등등)
        User user = userServiceImpl.findUser(chatDto.getSenderId());
        Room room = roomServiceImpl.findRoom(chatDto.getRoomId());
        boolean isExistsUserRoom = userRoomRepository.existsByUserAndRoom(user, room);

        if(chatDto.getMessageType().equals(MessageType.ENTER)) {  // 방 입장의 경우 (방이 이미 생성되어있다는 전제하에)
            if(isExistsUserRoom == true) {  // 이미 방에 입장해있는 사용자의 경우에는, 메세지를 전송하지 않는다.
                chatDto.setMessage("__null__");  // (==> 프론트엔드와의 null 약속메세지를 '__null__'로 해두었을경우의 예시)
            }
            else {
                userRoomService.createUserRoom(user, room);
                chatDto.setMessage("'" + user.getNickname() + "'님이 방에 참가하였습니다.");
            }
        }
        else if(chatDto.getMessageType().equals(MessageType.LEAVE)) {  // 방 탈퇴의 경우
            userRoomService.deleteUserRoom(user, room);  // 어차피 여기 안에서 이미 isExistsUserRoom 검사를 해줌.
            chatDto.setMessage("'" + user.getNickname() + "'님이 방에서 퇴장하셨습니다.");
        }
        else {  // 방 채팅의 경우 (MessageType.TALK 일때)
            if(isExistsUserRoom == false) throw new RuntimeException("ERROR - 방에 참가하지않은 사용자는 채팅이 불가능합니다.");
            if(chatDto.getMessage() == null) throw new RuntimeException("ERROR - 채팅시에는 반드시 message을 함께 보내주어야합니다.");
            chatDto.setMessage("'" + user.getNickname() + "'님의 메세지: '" + chatDto.getMessage() + "'");  //  (차후 수정할 코드줄임.)
        }

        chatDto.setSenderName(user.getNickname());
        chatDto.setCreatedTime(LocalDateTime.now());  // 시간을 수동으로 직접 넣어줌. (실시간으로 chat 넘겨주는 시간과 DB에 저장되는 시간을 완전히 같게 하기위해서이다.)

        Chat chat = chatDto.toEntity(room);
        chatRepository.save(chat);

        return chatDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatDto> findChatsByRoom(Long roomId) {  // 차후 페이지네이션으로 리팩토링해서 수정할것.
        Room room = roomServiceImpl.findRoom(roomId);
        List<Chat> chatList = room.getChatList();

        return chatList.stream().map(ChatDto::new)
                .sorted(Comparator.comparing(ChatDto::getCreatedTime))  // 정렬기준: 날짜 오래된 순서 (오름차순)
                .collect(Collectors.toList());
//        return chatList.stream().map(ChatDto::new)
//                .sorted(Comparator.comparing(ChatDto::getCreatedTime)  // 정렬기준 우선순위1: 날짜 오래된 순서 (오름차순)
//                        .thenComparing(ChatDto::getId, Comparator.reverseOrder()))  // 우선순위1 동일시 우선순위2 부여: id 내림차순
//                .collect(Collectors.toList());
    }
}