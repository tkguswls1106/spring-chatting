package com.shj.springchatting.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {  // 이 인터셉터는 메세지가 서버에 도착해서 발행이 되기 전에 동작하는 인터셉터이다.

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand()) || StompCommand.SEND.equals(headerAccessor.getCommand())) {  // 웹소켓 connect와 send 경우에 토큰 검사 실시.
            // 헤더 토큰 얻기
            List<String> authorizationHeaderList = headerAccessor.getNativeHeader(AUTHORIZATION_HEADER);  // List<String> 형태로 헤더 값을 가져온 후 첫 번째 값을 사용
            String bearerToken = (authorizationHeaderList != null && !authorizationHeaderList.isEmpty()) ? authorizationHeaderList.get(0) : null;
            String jwt = resolveTokenInStomp(bearerToken);  // 토큰값 문자열 리턴

            if (StringUtils.hasText(jwt) && tokenProvider.isExpiredToken(jwt) == true) {  // 토큰값이 null이 아닌가 && 해당 Access Token이 만료되었다면
                throw new MessageDeliveryException("토큰 만료 - ExpiredJwtException");
            }

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {  // 토큰값이 null이 아닌가 && 토큰의 서명이 일치하고 유효한가 (JWT 유효성 검사)
                Authentication authentication = tokenProvider.getAuthentication(jwt);  // JWT 토큰을 사용하여 사용자를 인증함.
                SecurityContextHolder.getContext().setAuthentication(authentication);  // 그 다음으로, Spring Security의 SecurityContextHolder에 인증 정보를 설정함.
                // (여기서 사실 이 로직은 굳이 필요할지 의문이긴 하다. 그 이유는 어차피 메세지 관련 서비스 클래스 내에서는 Security Context가 바뀌어 해당 jwt 내의 유저 정보를 활용할 수 없기 때문이다.)
            } else {
                throw new MessageDeliveryException("잘못된 토큰 - JwtException");
            }
        }

        return message;
    }


    private String resolveTokenInStomp(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {  // 추출된 헤더값이 null이 아닌가 && "Bearer "로 시작하는가 ("Bearer " 다음에 실제 토큰이 오는 것이 관례임.)
            return bearerToken.substring(7);  // 토큰이 유효하다면, 앞부분인 "Bearer "을 제외하여 7인덱스부터 끝까지인 실제 토큰 문자열을 반환함.
        }
        return null;
    }
}