package com.shj.springchatting.domain.chat;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

    @ManyToOne(fetch = FetchType.LAZY)  // Room-Chat 양방향매핑
    @JoinColumn(name = "room_id")
    private Room room;

    private Long senderId;

    private String message;

    private String createdDate;


    @Builder(builderClassName = "ChatSaveBuilder", builderMethodName = "ChatSaveBuilder")
    public Chat(MessageType messageType, Room room, Long senderId, String message, String createdDate) {
        // 이 빌더는 Chat 생성때만 사용할 용도
        this.messageType = messageType;
        this.room = room;
        this.senderId = senderId;
        this.message = message;
        this.createdDate = createdDate;
    }
}
