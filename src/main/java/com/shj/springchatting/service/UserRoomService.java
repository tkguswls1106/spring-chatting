package com.shj.springchatting.service;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.user.User;

public interface UserRoomService {
    void createUserRoom(User user, Room room);
    void deleteUserRoom(User user, Room room);
}
