package com.shj.springchatting.repository;

import com.shj.springchatting.domain.chat.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {  // MongoDB
    List<Chat> findAllByRoomId(Long roomId);
//    Page<Chat> findByRoomId(Long roomId, Pageable pageable);
}