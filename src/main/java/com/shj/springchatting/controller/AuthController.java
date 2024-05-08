package com.shj.springchatting.controller;

import com.shj.springchatting.dto.auth.ReissueRequestDto;
import com.shj.springchatting.dto.auth.SignupResponseDto;
import com.shj.springchatting.dto.auth.TokenDto;
import com.shj.springchatting.dto.user.UserSignupRequestDto;
import com.shj.springchatting.response.ResponseCode;
import com.shj.springchatting.response.ResponseData;
import com.shj.springchatting.service.AuthService;
import com.shj.springchatting.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin(origins = "*", allowedHeaders = "*")  // SecurityConfig에 대신 만들어주었음.
@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;


    // < OAuth2 방식 - 소셜 로그인 및 회원가입(추가정보입력) 로직 과정 >
    // - 과정 1. 처음 OAuth 소셜 로그인이 성공한다면, 일단 프론트에서 헤더에 Access 토큰을 지니게한뒤에, OAuth2LoginSuccessHandler에서 반환되는 ResponseEntity 내부의 isNewUser 필드의 boolean 결과에 따라 판단함.
    // - 과정 true-1. 만약 isNewUser=true인 경우, 프론트에서 추가정보 입력을 위한 회원가입 페이지로 안내함. 그리고 입력한 정보를 백엔드로 "/oauth2/signup" 경로로 api 요청을 보냄.
    // - 과정 true-2. 백엔드에서 회원가입 절차 성공 시, 권한이 ROLE_GUEST->ROLE_USER로 변경된 새로운 Access 토큰에 대한 TokenDto(리프레시 토큰은 변함X)을 프론트로 반환해줌. 프론트에서는 이 토큰을 헤더에 지니게함.
    // - 과정 true-3. 이제 프론트에서 원하는 메인 페이지로 이동해서 서비스를 이용하면 됨.
    // - 과정 false-1. 만약 isNewUser=false인 경우, 이미 기존 회원이므로 추가정보 입력을 위한 회원가입 절차 없이, 바로 프론트에서 원하는 메인 페이지로 이동해서 서비스를 이용하면 됨.
    @PostMapping("/oauth2/signup")  // 이 api는 헤더에 JWT토큰이 반드시 필요하다.
    public ResponseEntity signup(@RequestBody UserSignupRequestDto userSignupRequestDto) {  // 여기서 Role을 USER로 교체해주지 않으면 다른 로그인 필수 api를 사용하지 못한다.
        SignupResponseDto signupResponseDto = authService.signup(userSignupRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_USER, signupResponseDto);  // 이 reponseDto 내에 새로운 JWT access 토큰이 들어있다. 이후 앞으로는 이걸로 헤더에 장착해야함.
    }

    /*
    < Access Token 만료시, 이를 Refresh Token으로 재발급 받는 과정 >
    1. 프론트에서 로그인하면, 백엔드에서 Access 토큰과 Refresh 토큰을 발급해서 프론트에 전달한다. Refresh 토큰은 DB에도 저장해둔다.
    2. 프론트에서는 백엔드에 api 요청을 보낼 때마다 헤더에 Access 토큰을 담아서 보낸다.
    3. Access 토큰이 만료되었다는 에러응답을 백엔드로부터 받았다면, 기존의 Access 토큰과 Refresh 토큰을 dto에 담아 백엔드에게 보내서 토큰 재발급을 요청한다. (이때 헤더에 토큰은 필요없다.)
    4. 전달받은 Refresh 토큰의 유효성을 검사한다.
    5. 전달받은 Access 토큰에서 userId를 꺼내서 DB에 사용자를 검색하고, 해당 사용자의 Refresh 토큰이 전달받은 Refresh 토큰과 일치함을 검사한다.
    6-1. 만약 위의 두 검사가 모두 통과된다면, Access 토큰을 재발급 해준다.
    6-2. 만약 위의 두 검사 중에서 하나라도 통과되지 못한다면, 재발급이 안되고 재로그인을 해야한다.
    */
    @PostMapping("/reissue")  // 이 api는 헤더에 JWT토큰이 필요없다.
    public ResponseEntity reissue(@RequestBody ReissueRequestDto reissueRequestDto) {  // 여기서 Role을 USER로 교체해주지 않으면 다른 로그인 필수 api를 사용하지 못한다.
        TokenDto tokenDto = tokenService.reissue(reissueRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.REISSUE_SUCCESS, tokenDto);
    }
}