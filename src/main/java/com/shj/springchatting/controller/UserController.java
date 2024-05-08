package com.shj.springchatting.controller;

import com.shj.springchatting.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin(origins = "*", allowedHeaders = "*")  // SecurityConfig에 대신 만들어주었음.
@Tag(name = "User")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;



}
