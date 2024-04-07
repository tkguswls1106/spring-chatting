package com.shj.springchatting.service;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.user.User;

public interface RoomService {
    Room createRoom(String roomName, User user);
}
