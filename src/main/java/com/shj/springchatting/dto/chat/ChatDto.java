package com.shj.springchatting.dto.chat;

import com.shj.springchatting.domain.chat.Chat;
import com.shj.springchatting.domain.chat.MessageType;
import com.shj.springchatting.domain.chat.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChatDto {

    private Long senderId;

    @Setter
    private Long roomId;

    private String roomName;

    @Setter
    private String message;

    private MessageType messageType;

    @Setter
    private String createdDate;


    public Chat toEntity(Room room) {
        return Chat.ChatSaveBuilder()
                .messageType(messageType)
                .room(room)
                .senderId(senderId)
                .message(message)
                .createdDate(createdDate)
                .build();
    }
}
