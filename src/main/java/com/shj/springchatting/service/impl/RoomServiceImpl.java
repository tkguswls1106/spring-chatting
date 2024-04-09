package com.shj.springchatting.service.impl;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.dto.chat.RoomSaveRequestDto;
import com.shj.springchatting.dto.chat.RoomSaveResponseDto;
import com.shj.springchatting.repository.RoomRepository;
import com.shj.springchatting.service.RoomService;
import com.shj.springchatting.service.UserRoomService;
import com.shj.springchatting.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRoomService userRoomService;
    private final UserServiceImpl userServiceImpl;


    @Transactional(readOnly = true)
    public Room findRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방은 존재하지 않습니다."));
        return room;
    }

    @Transactional
    @Override
    public RoomSaveResponseDto createRoom(RoomSaveRequestDto roomSaveRequestDto) {
        Room room = Room.RoomSaveBuilder()
                .roomName(roomSaveRequestDto.getRoomName())
                .build();
        roomRepository.save(room);

        User user = userServiceImpl.findUser(SecurityUtil.getCurrentMemberId());
        userRoomService.createUserRoom(user, room);

        return new RoomSaveResponseDto(room);
    }
}
