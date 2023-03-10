package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.couple.*;
import com.example.demo.src.data.entity.Couple;
import com.example.demo.src.service.CoupleService;
import com.example.demo.src.service.PushService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/couples")
@Tag(name = "COUPLE", description = "커플 등록/조회/수정/삭제 API")
public class CoupleController {
    @Autowired
    private final CoupleService coupleService;
    @Autowired
    private final TokenService tokenService;

    public CoupleController(CoupleService coupleService, TokenService tokenService) {
        this.coupleService = coupleService;
        this.tokenService = tokenService;
    }

    /**
     * 커플 생성 API
     * [POST] /couples
     * return : coupleId, 본인 닉네임, 상대방 닉네임
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
            "해당 유저와 특정 코드의 사용자를 커플로 매칭하는 api입니다.",
            tags = "COUPLE", summary = "커플 생성 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2020", description = "이미 커플 매칭이 완료된 유저입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<CoupleRes> createCouple(@RequestBody PostCoupleReq postCoupleReq) {
        try {
            int userIdByJwt = tokenService.getUserId();
            CoupleRes coupleRes = coupleService.createCouple(userIdByJwt, postCoupleReq);
            return new BaseResponse<>(coupleRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 만난 날 설정 및 수정 API
     * [PATCH] /couples/firstMetDay
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
            "만난 날을 초기 설정하거나 수정하는 api입니다.",
            tags = "COUPLE", summary = "만난 날 설정 및 수정 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2060", description = "만난 날 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/firstMetDay")
    public BaseResponse<PatchCoupleRes> modifyFirstMetDay(@RequestBody PatchCoupleFirstMetDayReq patchCoupleReq) {
        try {
            int userIdByJwt = tokenService.getUserId();
            int coupleId = coupleService.modifyFirstMetDay(userIdByJwt, patchCoupleReq.getFirstMetDay());
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, coupleId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 우편함 이름 설정 및 수정 API
     * [PATCH] /couples/mailbox
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
            "우편함 이름을 초기 설정하거나 수정하는 api입니다." ,
            tags = "COUPLE", summary = "우편함 이름 설정 및 수정 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2061", description = "우편함 이름 형식을 확인해주세요."),
            @ApiResponse(responseCode = "2062", description = "중복된 우편함 이름입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/mailbox")
    public BaseResponse<PatchCoupleRes> modifyMailbox(@RequestBody PatchCoupleMailboxNameReq patchCoupleReq) {
        try{
            int userIdByJwt = tokenService.getUserId();
            int coupleId = coupleService.modifyMailbox(userIdByJwt, patchCoupleReq.getMailboxName());
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, coupleId));
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 푸시 알람 메세지 설정 및 수정 API
     * [PATCH] /couples/pushMessage
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "푸시 알람 메세지를 수정하는 api입니다." ,
            tags = "COUPLE", summary = "푸시 알람 메세지 설정 및 수정 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/pushMessage")
    public BaseResponse<PatchCoupleRes> modifyPushMessage(@RequestBody PatchCouplePushMessageReq patchCoupleReq) {
        try{
            int userIdByJwt = tokenService.getUserId();
            int coupleId = coupleService.modifyPushMessage(userIdByJwt, patchCoupleReq.getMessage());
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, coupleId));
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 우편함 이름 중복확인 API
     * [GET] /couples/mailbox
     */
    @Operation(method = "GET",
    description = "쿼리 스트링 값이 이미 존재하는 우편함 이름인지 확인해주는 api입니다. 중복이면 true / 중복 아니면 false",
    tags = "COUPLE", summary = "우편함 이름 중복확인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2061", description = "우편함 이름 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/mailbox")
    public BaseResponse<Boolean> checkMailbox(@Parameter @RequestParam(name = "mailboxName") String mailbox) {
        try {
            return new BaseResponse<>(coupleService.checkMailbox(mailbox));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 커플 매칭 끊기 API
     * [PATCH] /couples/d
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "해당 유저의 커플 매칭을 soft delete 형식으로 끊는 api입니다.",
            tags = "COUPLE", summary = "커플 연결 끊기 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/d")
    public BaseResponse<PatchCoupleRes> deleteCouple() {
        try {
            int userIdByJwt = tokenService.getUserId();
            int coupleId = coupleService.deleteCouple(userIdByJwt);
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, coupleId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 커플 전체 조회 API
     * [GET] /couples
     */
    @Operation(method = "GET",
            description = "커플 객체 그대로 반환하는 api입니다." ,
            tags = "COUPLE", summary = "커플 전체 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<Couple>> getCouples() {
        try {
            List<Couple> couplesList = coupleService.getCouples();
            return new BaseResponse<>(couplesList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 커플 id로 커플 조회 API
     * [GET] /couples/:coupleId
     */
    @Operation(method = "GET",
            description = "커플 객체 그대로 반환하는 api입니다." ,
            tags = "COUPLE", summary = "coupleId로 커플 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/{coupleId}")
    public BaseResponse<Couple> getCoupleByCoupleID(@PathVariable("coupleId") int coupleId){
        try {
            Couple couple = coupleService.getCoupleByCoupleId(coupleId);
            return new BaseResponse<>(couple);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 id로 커플 조회 API
     * [GET] /couples/users/:userId
     */
    @Operation(method = "GET",
            description = "커플 객체 그대로 반환하는 api입니다.",
            tags = "COUPLE", summary = "userId로 커플 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/users/{userId}")
    public BaseResponse<Couple> getCoupleByUserId(@PathVariable("userId") int userId){
        try {
            Couple couple = coupleService.getCoupleByUserId(userId);
            return new BaseResponse<>(couple);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 매칭 여부 확인(새로고침) API
     * [GET] /couples/isMatch
     * return : 매칭 여부 / coupleId, 본인 닉네임, 상대방 닉네임
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
            "(매칭 여부, 커플 ID, 우편함 이름 설정 여부, 본인 닉네임, 상대방 닉네임) 을 반환하는 api입니다.",
            tags = "COUPLE", summary = "매칭 여부 확인(새로고침), 닉네임 정보 조회 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/isMatch")
    public BaseResponse<GetCoupleMatchRes> getCoupleMatch(){
        try {
            int userIdByJwt = tokenService.getUserId();
            GetCoupleMatchRes getCoupleMatchRes = coupleService.getCoupleMatch(userIdByJwt);
            return new BaseResponse<>(getCoupleMatchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 우표 구매 API
     * [PATCH] /couples/stamp/:stampNum
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "stampNum(1~6)에 해당하는 우표(3프롬 차감)를 구매하는 api입니다.",
            tags = "COUPLE", summary = "우표 구매 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2063",description = "우표 번호가 유효하지 않습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/stamp/{stampNum}")
    public BaseResponse<PatchCoupleRes> buyStamp(@PathVariable("stampNum") int stampNum){
        try {
            int userIdByJwt = tokenService.getUserId();
            int coupleId = coupleService.buyStamp(userIdByJwt, stampNum);
            return new BaseResponse<>(new PatchCoupleRes(userIdByJwt, coupleId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
