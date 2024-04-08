package com.shj.springchatting.domain.chat;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

@Table(name = "chat")
@Entity
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Room-Chat 양방향매핑
    @JoinColumn(name = "room_id")
    private Room room;

    private Long senderId;

    private String senderName;

    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;  // 실시간으로 chat 넘겨주는 시간과 DB에 저장되는 시간을 완전히 같게 하기위해서, 수동으로 직접 현재시각으로 저장하도록 구현하였음.


    @Builder(builderClassName = "ChatSaveBuilder", builderMethodName = "ChatSaveBuilder")
    public Chat(Room room, Long senderId, String senderName, String message, MessageType messageType, LocalDateTime createdTime) {
        // 이 빌더는 Chat 생성때만 사용할 용도
        this.room = room;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.messageType = messageType;
        this.createdTime = createdTime;
    }
}
