package com.shj.springchatting.service;

import com.shj.springchatting.dto.chat.RoomSaveRequestDto;
import com.shj.springchatting.dto.chat.RoomSaveResponseDto;

public interface RoomService {
    RoomSaveResponseDto createRoom(RoomSaveRequestDto roomSaveRequestDto);
}
