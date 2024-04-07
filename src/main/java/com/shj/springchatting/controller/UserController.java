package com.shj.springchatting.controller;

import com.shj.springchatting.domain.user.User;
import com.shj.springchatting.repository.UserRepository;
import com.shj.springchatting.response.ResponseCode;
import com.shj.springchatting.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;


    // Test 용도
    @PostMapping("/user/{nickname}")
    public ResponseEntity createUser(@PathVariable String nickname) {
        User user = User.UserSaveBuilder()
                .nickname(nickname)
                .build();
        userRepository.save(user);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_USER, user);
    }
}
