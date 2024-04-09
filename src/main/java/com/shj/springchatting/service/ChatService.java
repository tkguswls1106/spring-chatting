package com.shj.springchatting.service;

import com.shj.springchatting.dto.chat.ChatRequestDto;
import com.shj.springchatting.dto.chat.ChatResponseDto;

import java.util.List;

public interface ChatService {
    ChatResponseDto createChat(ChatRequestDto chatRequestDto);
    List<ChatResponseDto> findChatsByRoom(Long roomId);  // 차후 페이지네이션으로 리팩토링해서 수정할것. & 로그인 사용자 관련 접근예외처리는 차후에 추가할것.
}
