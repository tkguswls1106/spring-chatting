package com.shj.springchatting.dto.auth;

import com.shj.springchatting.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {

    private UserResponseDto userResponseDto;
    private TokenDto tokenDto;
}
