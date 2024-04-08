package com.shj.springchatting.dto.chat;

import com.shj.springchatting.domain.chat.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomSaveResponseDto {

    private Long roomId;
    private String roomName;

    public RoomSaveResponseDto(Room entity) {
        this.roomId = entity.getRoomId();
        this.roomName = entity.getRoomName();
    }
}
