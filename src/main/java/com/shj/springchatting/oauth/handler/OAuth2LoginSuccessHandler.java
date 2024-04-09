package com.shj.springchatting.oauth.handler;

import com.shj.springchatting.domain.user.Role;
import com.shj.springchatting.dto.auth.TokenDto;
import com.shj.springchatting.jwt.TokenProvider;
import com.shj.springchatting.oauth.CustomOAuth2User;
import com.shj.springchatting.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            Long userId = oAuth2User.getUserId();
            Role role = oAuth2User.getRole();

            TokenDto tokenDto = tokenProvider.generateTokenDto(userId, role);  // Access & Refresh 토큰 발행.
            String accessToken = tokenDto.getAccessToken();
            log.info("발급된 Access Token : {}", accessToken);
            String refreshToken = tokenDto.getRefreshToken();
            log.info("발급된 Refresh Token : {}", refreshToken);

            // 로그인에 성공했으므로, 사용자 DB에 Refresh Token 저장(있다면 업데이트).
            tokenService.updateRefreshToken(userId, refreshToken);

            String redirectUrl;
            if(oAuth2User.getRole().equals(Role.ROLE_GUEST)) {  // User의 Role이 GUEST일 경우, 처음 요청한 회원이므로, 회원가입 페이지로 리다이렉트 시켜야함을 프론트에 전달.
                redirectUrl = makeRedirectUrl(tokenDto, true);
                log.info("신규 회원 입니다. JWT 헤더를 가진채로, 추가정보 입력을 위한 회원가입 페이지로 리다이렉트 시켜주세요.");  // 리다이렉트(프론트엔드 url)는 백엔드에서 시키고, 헤더에 jwt 다는건 프론트엔드에서.
            }
            else {  // 이미 한 번 이상 OAuth2 로그인했던 유저일 때 (즉, 이미 회원가입 추가정보를 입력해두었던 유저일때)
                redirectUrl = makeRedirectUrl(tokenDto, false);
                log.info("기존 회원 입니다. JWT 헤더를 가진채로, 메인 페이지로 리다이렉트 시켜주세요.");  // 리다이렉트(프론트엔드 url)는 백엔드에서 시키고, 헤더에 jwt 다는건 프론트엔드에서.
            }

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            throw e;
        }
    }

    public String makeRedirectUrl(TokenDto tokenDto, boolean isNewUser) {
        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/loginwait")  // 프론트엔드 url로 작성할것.
                .queryParam("grantType", tokenDto.getGrantType())
                .queryParam("accessToken", tokenDto.getAccessToken())
                .queryParam("accessTokenExpiresIn", tokenDto.getAccessTokenExpiresIn())
                .queryParam("refreshToken", tokenDto.getRefreshToken())
                .queryParam("isNewUser", isNewUser)
                .build().toUriString();
        return redirectUrl;
    }
}