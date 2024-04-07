package com.shj.springchatting.domain.mapping;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor

@Table(name = "user_room")
@Entity
public class UserRoom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // User-UserRoom 양방향매핑
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)  // Room-UserRoom 양방향매핑
    @JoinColumn(name = "room_id")
    private Room room;


    @Builder(builderClassName = "UserRoomSaveBuilder", builderMethodName = "UserRoomSaveBuilder")
    public UserRoom(User user, Room room) {
        // 이 빌더는 UserRoom 생성때만 사용할 용도
        this.user = user;
        this.room = room;
    }
}
