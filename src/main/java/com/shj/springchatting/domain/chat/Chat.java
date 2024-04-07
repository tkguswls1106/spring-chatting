package com.shj.springchatting.domain.chat;

import com.shj.springchatting.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@NoArgsConstructor

@Table(name = "chat")
@Entity
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private Long roomId;

    private Long senderId;

    private String message;

    private String createdDate;

    @ManyToOne(fetch = FetchType.LAZY)  // Room-Chat 양방향매핑
    @JoinColumn(name = "room_id")
    private Room room;


    @Builder(builderClassName = "ChatSaveBuilder", builderMethodName = "ChatSaveBuilder")
    public Chat(MessageType messageType, Long roomId, Long senderId, String message, String createdDate) {
        // 이 빌더는 Chat 생성때만 사용할 용도
        this.messageType = messageType;
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.createdDate = createdDate;
    }
}
