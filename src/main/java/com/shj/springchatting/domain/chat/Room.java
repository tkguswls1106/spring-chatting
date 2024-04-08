package com.shj.springchatting.domain.chat;

import com.shj.springchatting.domain.mapping.UserRoom;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor

@Table(name = "room")
@Entity
public class Room implements Serializable {  // 웹소켓 위에 Stomp를 얹어서 사용하게되면, 이전에 웹소켓만 사용할때와는 다르게 session을 가질 필요가 없다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    private String roomName;  // 채팅방 이름

    // (읽기 전용 필드) mappedBy만 사용으로, 조회 용도로만 가능. JPA는 insert나 update할 때 읽기 전용 필드를 아예 보지 않아서, 값을 넣어도 아무일도 일어나지않음.
    @OneToMany(mappedBy = "room")  // Room-UserRoom 양방향매핑
    private List<UserRoom> userRoomList = new ArrayList<>();


    @Builder(builderClassName = "RoomSaveBuilder", builderMethodName = "RoomSaveBuilder")
    public Room(String roomName) {
        // 이 빌더는 채팅방 생성때만 사용할 용도
        this.roomName = roomName;
    }
}
