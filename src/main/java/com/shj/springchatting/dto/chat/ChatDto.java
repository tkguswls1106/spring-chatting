package com.shj.springchatting.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shj.springchatting.domain.chat.Chat;
import com.shj.springchatting.domain.chat.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChatDto {

    private Long senderId;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // 해당 필드가 null값일때, JSON 직렬화에서 이 필드가 무시됨.
    @Setter
    private Long roomId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roomName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    private String message;

    private MessageType messageType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    private String createdDate;


    public Chat toEntity() {
        return Chat.ChatSaveBuilder()
                .messageType(messageType)
                .roomId(roomId)
                .senderId(senderId)
                .message(message)
                .createdDate(createdDate)
                .build();
    }
}
