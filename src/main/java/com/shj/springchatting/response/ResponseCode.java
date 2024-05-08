package com.shj.springchatting.response;

import com.shj.springchatting.response.responseitem.MessageItem;
import com.shj.springchatting.response.responseitem.StatusItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    // Swagger API 응답값 미리보기 용도
    string(StatusItem.OK, "Swagger API"),

    CREATED_USER(StatusItem.CREATED, MessageItem.CREATED_USER),
    NOT_FOUND_USER(StatusItem.NOT_FOUND, MessageItem.NOT_FOUND_USER),
    DUPLICATE_USER(StatusItem.BAD_REQUEST, MessageItem.DUPLICATE_USER),

    // 기타 성공 응답
    READ_IS_LOGIN(StatusItem.OK, MessageItem.READ_IS_LOGIN),
    LOGIN_SUCCESS(StatusItem.OK, MessageItem.LOGIN_SUCCESS),
    GET_LOGIN(StatusItem.NO_CONTENT, MessageItem.GET_LOGIN),
    UPDATE_PASSWORD(StatusItem.NO_CONTENT, MessageItem.UPDATE_PASSWORD),
    HEALTHY_SUCCESS(StatusItem.OK, MessageItem.HEALTHY_SUCCESS),
    REISSUE_SUCCESS(StatusItem.OK, MessageItem.REISSUE_SUCCESS),

    CREATED_ROOM(StatusItem.CREATED, MessageItem.CREATED_ROOM),
    READ_CHATLIST(StatusItem.OK, MessageItem.READ_CHATLIST),

    // 기타 실패 응답
    INTERNAL_SERVER_ERROR(StatusItem.INTERNAL_SERVER_ERROR, MessageItem.INTERNAL_SERVER_ERROR),
    anonymousUser_ERROR(StatusItem.INTERNAL_SERVER_ERROR, MessageItem.anonymousUser_ERROR),
    UNAUTHORIZED_ERROR(StatusItem.UNAUTHORIZED, MessageItem.UNAUTHORIZED),
    FORBIDDEN_ERROR(StatusItem.FORBIDDEN, MessageItem.FORBIDDEN),
    LOGIN_FAIL(StatusItem.BAD_REQUEST, MessageItem.LOGIN_FAIL),
    TOKEN_EXPIRED(StatusItem.UNAUTHORIZED, MessageItem.TOKEN_EXPIRED),
    TOKEN_ERROR(StatusItem.UNAUTHORIZED, MessageItem.TOKEN_ERROR),

    // Test 임시 용도
    TEST_SUCCESS(StatusItem.OK, MessageItem.TEST_SUCCESS),

    // ===================== //
    ;

    private int httpStatus;
    private String message;
}