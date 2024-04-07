package com.shj.springchatting.service.impl;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.mapping.UserRoom;
import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.repository.UserRoomRepository;
import com.shj.springchatting.service.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRoomServiceImpl implements UserRoomService {

    private final UserRoomRepository userRoomRepository;


    @Transactional(readOnly = true)
    public UserRoom findUserRoom(Long userRoomId) {
        UserRoom userRoom = userRoomRepository.findById(userRoomId)
                .orElseThrow(() -> new RuntimeException("해당 UserRoom은 존재하지 않습니다."));
        return userRoom;
    }

    @Transactional
    @Override
    public void createUserRoom(User user, Room room) {
        UserRoom userRoom = UserRoom.UserRoomSaveBuilder()
                .user(user)
                .room(room)
                .build();
        userRoomRepository.save(userRoom);
    }

    @Transactional
    @Override
    public void deleteUserRoom(User user, Room room) {
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room)
                .orElseThrow(() -> new RuntimeException("해당 UserRoom은 존재하지 않습니다."));
        userRoomRepository.delete(userRoom);
    }
}
