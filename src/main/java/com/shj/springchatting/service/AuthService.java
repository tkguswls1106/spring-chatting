package com.shj.springchatting.service;

import com.shj.springchatting.dto.auth.SignupResponseDto;
import com.shj.springchatting.dto.user.UserSignupRequestDto;

public interface AuthService {

    SignupResponseDto signup(UserSignupRequestDto userSignupRequestDto);
}
