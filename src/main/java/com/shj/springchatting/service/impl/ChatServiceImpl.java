package com.shj.springchatting.service.impl;

import com.shj.springchatting.domain.chat.Chat;
import com.shj.springchatting.domain.chat.MessageType;
import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.dto.chat.ChatRequestDto;
import com.shj.springchatting.dto.chat.ChatResponseDto;
import com.shj.springchatting.repository.ChatRepository;
import com.shj.springchatting.repository.UserRoomRepository;
import com.shj.springchatting.service.ChatService;
import com.shj.springchatting.service.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
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
    public ChatResponseDto createChat(ChatRequestDto chatRequestDto) {  // 다른 조건에 대한 여부는 차후 추가하면 됨. (ex: 방 최대인원에 대한 입장제한 or 재입장 관련 or 방에 남은 인원0명 또는 방장 퇴장의 경우 방삭제 ... 등등)
        User user = userServiceImpl.findUser(chatRequestDto.getSenderId());
        Room room = roomServiceImpl.findRoom(chatRequestDto.getRoomId());
        boolean isExistsUserRoom = userRoomRepository.existsByUserAndRoom(user, room);

        String message = null;
        LocalDateTime createdTime = LocalDateTime.now();  // 시간을 수동으로 직접 넣어줌. (실시간으로 chat 넘겨주는 시간과 DB에 저장되는 시간을 완전히 같게 하기위해서이다.)
        if(chatRequestDto.getMessageType().equals(MessageType.ENTER)) {  // 방 입장의 경우 (방이 이미 생성되어있다는 전제하에)
            if(isExistsUserRoom == true) {  // 이미 방에 입장해있는 사용자의 경우에는, 메세지를 전송하지 않는다.
                message = "__null__";  // (==> 프론트엔드와의 null 약속메세지를 '__null__'로 해두었을경우의 예시)
            }
            else {
                userRoomService.createUserRoom(user, room);
                message = "'" + user.getNickname() + "'님이 방에 참가하였습니다.";
            }
        }
        else if(chatRequestDto.getMessageType().equals(MessageType.LEAVE)) {  // 방 탈퇴의 경우
            userRoomService.deleteUserRoom(user, room);  // 어차피 여기 안에서 이미 isExistsUserRoom 검사를 해줌.
            message = "'" + user.getNickname() + "'님이 방에서 퇴장하셨습니다.";
        }
        else {  // 방 채팅의 경우 (MessageType.TALK 일때)
            if(isExistsUserRoom == false) throw new RuntimeException("ERROR - 방에 참가하지않은 사용자는 채팅이 불가능합니다.");
            if(chatRequestDto.getMessage() == null) throw new RuntimeException("ERROR - 채팅시에는 반드시 message을 함께 보내주어야합니다.");
            message = "'" + user.getNickname() + "'님의 메세지: '" + chatRequestDto.getMessage() + "'";  //  (차후 수정할 코드줄임.)
        }

        Chat chat = chatRequestDto.toEntity(user.getNickname(), message, createdTime);
        chatRepository.save(chat);

        return new ChatResponseDto(chat);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatResponseDto> findChatsByRoom(Long roomId) {  // 차후 페이지네이션으로 리팩토링해서 수정할것. & 로그인 사용자 관련 접근예외처리는 차후에 추가할것.
        List<Chat> chatList = chatRepository.findAllByRoomId(roomId);

        return chatList.stream().map(ChatResponseDto::new)
                .sorted(Comparator.comparing(ChatResponseDto::getCreatedTime))  // 정렬기준: 날짜 오래된 순서 (오름차순)
                .collect(Collectors.toList());
//        return chatList.stream().map(ChatResponseDto::new)
//                .sorted(Comparator.comparing(ChatResponseDto::getCreatedTime)  // 정렬기준 우선순위1: 날짜 오래된 순서 (오름차순)
//                        .thenComparing(ChatDto::getId, Comparator.reverseOrder()))  // 우선순위1 동일시 우선순위2 부여: id 내림차순
//                .collect(Collectors.toList());
    }
}