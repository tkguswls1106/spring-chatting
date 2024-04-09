package com.shj.springchatting.dto.user;

import com.shj.springchatting.domain.user.SocialType;
import com.shj.springchatting.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;

    private String nickname;
    private String imageUrl;
    private SocialType socialType;

    private LocalDateTime createdTime;

    private String moreInfo1;
    private String moreInfo2;
    private String moreInfo3;

    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.imageUrl = entity.getImageUrl();
        this.socialType = entity.getSocialType();
        this.createdTime = entity.getCreatedTime();
        this.moreInfo1 = entity.getMoreInfo1();
        this.moreInfo2 = entity.getMoreInfo2();
        this.moreInfo3 = entity.getMoreInfo3();
    }
}