package com.shj.springchatting.repository;

import com.shj.springchatting.domain.chat.Room;
import com.shj.springchatting.domain.mapping.UserRoom;
import com.shj.springchatting.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    Optional<UserRoom> findByUserAndRoom(User user, Room room);
    boolean existsByUserAndRoom(User user, Room room);
}
