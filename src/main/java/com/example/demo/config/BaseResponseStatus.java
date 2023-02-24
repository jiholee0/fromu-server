package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    EMPTY_JWT(false, 2000, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2001, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2002,"권한이 없는 유저의 접근입니다."),
    EMPTY_ACCESS_TOKEN(false, 2003, "ACCESS_TOKEN을 입력해주세요."),


    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2010, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2011, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2012,"이미 가입한 이메일입니다."),
    POST_USERS_EMPTY_NICKNAME(false, 2013, "닉네임을 입력해주세요."),
    POST_USERS_INVALID_NICKNAME(false, 2014, "닉네임 형식을 확인해주세요."),
    POST_USERS_EMPTY_BIRTHDAY(false, 2015, "생일을 입력해주세요."),
    POST_USERS_INVALID_BIRTHDAY(false, 2016, "생일 형식을 확인해주세요."),
    POST_USERS_EMPTY_GENDER(false, 2017, "성별을 입력해주세요."),
    POST_USERS_INVALID_GENDER(false, 2018, "성별 형식을 확인해주세요."),
    POST_USERS_EMPTY_FIRSTMETDAY(false, 2019, "만난 날을 입력해주세요."),
    POST_USERS_INVALID_FIRSTMETDAY(false, 2020, "만난 날 형식을 확인해주세요."),


    /**
     * 3000 : Response 오류
     */
    // [POST] /users
    NOT_EXIST_EMAIL(false, 3000, "존재하지 않는 이메일입니다."),
    FAIL_TO_GET_EMAIL(false, 3001, "이메일 정보를 가져오는데 실패하였습니다."),
    FAIL_TO_RESPONSE_KAKAO(false, 3002, "카카오 api 호출 응답 정보를 불러오는데 실패하였습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
