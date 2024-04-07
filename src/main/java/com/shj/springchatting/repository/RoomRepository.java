package com.shj.springchatting.repository;

import com.shj.springchatting.domain.chat.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
