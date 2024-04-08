package com.shj.springchatting.service;

import com.shj.springchatting.dto.chat.ChatDto;

import java.util.List;

public interface ChatService {
    ChatDto createChat(ChatDto chatDto);
    List<ChatDto> findChatsByRoom(Long roomId);  // 차후 페이지네이션으로 리팩토링해서 수정할것.
}
