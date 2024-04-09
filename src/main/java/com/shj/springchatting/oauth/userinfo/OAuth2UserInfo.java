package com.shj.springchatting.oauth.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;  // 추상클래스를 상속받는 클래스에서만 사용할 수 있도록 protected 제어자를 사용했음.

    public OAuth2UserInfo(Map<String, Object> attributes) {  // 각 소셜 타입별 유저 정보 클래스가 소셜 타입에 맞는 attributes를 주입받아 가지도록 하는 역할임.
        this.attributes = attributes;
    }

    public abstract String getId();  // 소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getNickname();

    public abstract String getImageUrl();

    // 각 소셜에서 제공하는 정보 중에 사용하고 싶은 정보가 있다면, 메소드를 더 추가해서 사용하면 됨.
}