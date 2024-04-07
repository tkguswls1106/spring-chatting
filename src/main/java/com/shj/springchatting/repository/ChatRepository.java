package com.shj.springchatting.repository;

import com.shj.springchatting.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}