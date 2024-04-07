package com.shj.springchatting.domain.user;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.mapping.UserRoom;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor

@Table(name = "user")
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    // (읽기 전용 필드) mappedBy만 사용으로, 조회 용도로만 가능. JPA는 insert나 update할 때 읽기 전용 필드를 아예 보지 않아서, 값을 넣어도 아무일도 일어나지않음.
    @OneToMany(mappedBy = "user")  // User-UserRoom 양방향매핑
    private List<UserRoom> userRoomList = new ArrayList<>();


    @Builder(builderClassName = "UserSaveBuilder", builderMethodName = "UserSaveBuilder")
    public User(String nickname) {
        // 이 빌더는 User 생성때만 사용할 용도
        this.nickname = nickname;
    }
}
