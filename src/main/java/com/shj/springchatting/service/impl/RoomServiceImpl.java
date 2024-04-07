package com.shj.springchatting.service.impl;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.repository.RoomRepository;
import com.shj.springchatting.service.RoomService;
import com.shj.springchatting.service.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRoomService userRoomService;


    @Transactional(readOnly = true)
    public Room findRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방은 존재하지 않습니다."));
        return room;
    }

    @Transactional
    @Override
    public Room createRoom(String roomName, User user) {
        Room room = Room.RoomSaveBuilder()
                .roomName(roomName)
                .build();
        Room newRoom = roomRepository.save(room);

        userRoomService.createUserRoom(user, newRoom);

        return newRoom;
    }
}
