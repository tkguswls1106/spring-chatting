package com.shj.springchatting.dto.chat;

import com.shj.springchatting.domain.chat.Chat;
import com.shj.springchatting.domain.chat.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

    private Long roomId;
    private Long senderId;
    private String senderName;
    private String message;
    private MessageType messageType;
    private LocalDateTime createdTime;

    public ChatResponseDto(Chat entity) {
        this.roomId = entity.getRoomId();
        this.senderId = entity.getSenderId();
        this.senderName = entity.getSenderName();
        this.message = entity.getMessage();
        this.messageType = entity.getMessageType();
        this.createdTime = entity.getCreatedTime();
    }
}
