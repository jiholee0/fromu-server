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
    EMPTY_ACCESS_TOKEN(false, 2003, "accessToken을 입력해주세요."),
    INVALID_REQ_PARAM(false,2004,"파라미터 값을 확인해주세요."),


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

    // [POST] /couples
    POST_COUPLES_EXISTS_USER(false,2020,"이미 커플 매칭이 완료된 유저입니다."),

    // [POST] /diaries
    POST_DIARIES_NOT_TURN(false,2030,"아직 상대방이 일기장을 작성하지 않았습니다."),
    POST_DIARIES_NOT_YET(false,2031,"아직 일기장이 오지 않았습니다."),
    POST_DIARIES_ALREADY_WRITE(false,2032,"일기를 이미 작성하였습니다."),

    // [PATCH] /users
    PATCH_USERS_INVALID_NICKNAME(false,2050, "닉네임 형식을 확인해주세요."),
    PATCH_USERS_INVALID_BIRTHDAY(false, 2051, "생일 형식을 확인해주세요."),

    // [PATCH] /couples
    PATCH_COUPLES_INVALID_FIRSTMETDAY(false, 2060, "만난 날 형식을 확인해주세요."),
    PATCH_COUPLES_INVALID_MAILBOX(false, 2061, "우편함 이름 형식을 확인해주세요."),
    PATCH_COUPLES_EXISTS_MAILBOX(false,2062,"중복된 우편함 이름입니다."),
    PATCH_DIARYBOOKS_NOT_TURN_TO_PASS(false,2070,"일기장이 해당 유저에게 없습니다."),
    PATCH_DIARYBOOKS_NOT_WRITE_DIARY(false,2071,"아직 일기를 작성하지 않아 일기장을 보낼 수 없습니다."),







    /**
     * 3000 : Response 오류
     */
    // [POST] /users
    NOT_EXIST_EMAIL(false, 3000, "존재하지 않는 이메일입니다."),
    FAIL_TO_GET_EMAIL(false, 3001, "이메일 정보를 가져오는데 실패하였습니다."),
    FAIL_TO_RESPONSE_KAKAO(false, 3002, "카카오 api 호출 응답 정보를 불러오는데 실패하였습니다."),
    FAIL_TO_RESPONSE_APPLE(false, 3003, "애플 api 호출 응답 정보를 불러오는데 실패하였습니다."),
    FAIL_TO_FIND_AVALIABLE_RSA(false,3004,"사용 가능한 공개키가 없습니다."),
    FAIL_TO_RESPONSE_GOOGLE(false, 3005, "구글 api 호출 응답 정보를 불러오는데 실패하였습니다."),
    FAIL_TO_CAL_DDAY(false,3010,"dday 계산에 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    NOT_EXIST_DATA(false, 4001, "데이터가 존재하지 않습니다."),
    NOT_EXIST_DATA_COUPLE(false, 4002, "커플이 존재하지 않습니다."),
    NOT_EXIST_DATA_DIARYBOOK(false, 4003, "일기장이 존재하지 않습니다."),


    // 5000 : 필요시 만들어서 쓰세요
    FAIL_TO_UPLOAD_FILE(false,5000,"파일 업로드에 실패했습니다.");

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
