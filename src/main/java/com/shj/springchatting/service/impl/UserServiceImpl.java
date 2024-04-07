package com.shj.springchatting.service.impl;

import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.repository.UserRepository;
import com.shj.springchatting.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public User findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자는 존재하지 않습니다."));
        return user;
    }
}
