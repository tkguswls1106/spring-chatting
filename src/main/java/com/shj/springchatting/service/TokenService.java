package com.shj.springchatting.service;

import com.shj.springchatting.dto.auth.ReissueRequestDto;
import com.shj.springchatting.dto.auth.TokenDto;

public interface TokenService {

    TokenDto reissue(ReissueRequestDto reissueRequestDto);
    void updateRefreshToken(Long userId, String refreshToken);
}
