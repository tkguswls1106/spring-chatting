package com.shj.springchatting.controller;

import com.shj.springchatting.dto.chat.RoomSaveRequestDto;
import com.shj.springchatting.dto.chat.RoomSaveResponseDto;
import com.shj.springchatting.response.ResponseCode;
import com.shj.springchatting.response.ResponseData;
import com.shj.springchatting.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @PostMapping("/{userId}/rooms")  // 나중에 로그인 유저로 변경하면, {userId}패스파라미터 제외하기.
    public ResponseEntity createRoom(@PathVariable Long userId, @RequestBody RoomSaveRequestDto roomSaveRequestDto) {
        RoomSaveResponseDto roomSaveResponseDto = roomService.createRoom(userId, roomSaveRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.TEST_SUCCESS, roomSaveResponseDto);
    }
}
