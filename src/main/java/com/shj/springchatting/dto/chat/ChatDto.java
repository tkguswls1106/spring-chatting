package com.shj.springchatting.dto.chat;

import com.shj.springchatting.domain.chat.Chat;
import com.shj.springchatting.domain.chat.MessageType;
import com.shj.springchatting.domain.chat.Room;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChatDto {

    // cannot NULL
    @NotNull(message = "ERROR - senderId cannot be NULL")
    private Long senderId;
    @NotNull(message = "ERROR - roomId cannot be NULL")
    private Long roomId;
    @NotNull(message = "ERROR - messageType cannot be NULL")
    private MessageType messageType;

    // can NULL
    @Setter
    private String message;
    @Setter
    private String senderName;
    @Setter
    private String createdDate;

    public Chat toEntity(Room room) {
        return Chat.ChatSaveBuilder()
                .room(room)
                .senderId(senderId)
                .senderName(senderName)
                .message(message)
                .messageType(messageType)
                .createdDate(createdDate)
                .build();
    }
}

/*
< if(messageType == "TALK") RequestDTO >
{
    "senderId": 1,
    "roomId": 1,
    "messageType": "TALK",

    "message": "hello everyone!",
    "senderName": null,
    "createdDate": null
}

< if(messageType == "ENTER" or "LEAVE") RequestDTO >
{
    "senderId": 1,
    "roomId": 1,
    "messageType": "ENTER" or "LEAVE",

    "message": null,
    "senderName": null,
    "createdDate": null
}
 */
