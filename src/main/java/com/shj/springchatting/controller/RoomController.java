package com.shj.springchatting.controller;

import com.shj.springchatting.dto.chat.RoomSaveRequestDto;
import com.shj.springchatting.dto.chat.RoomSaveResponseDto;
import com.shj.springchatting.response.ResponseCode;
import com.shj.springchatting.response.ResponseData;
import com.shj.springchatting.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin(origins = "*", allowedHeaders = "*")  // SecurityConfig에 대신 만들어주었음.
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @PostMapping("/rooms")
    public ResponseEntity createRoom(@RequestBody RoomSaveRequestDto roomSaveRequestDto) {
        RoomSaveResponseDto roomSaveResponseDto = roomService.createRoom(roomSaveRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_ROOM, roomSaveResponseDto);
    }
}
