package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.push.PushMsgReq;
import com.example.demo.src.data.dto.push.PushMsgRes;
import com.example.demo.src.service.CoupleService;
import com.example.demo.src.service.PushService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.NOT_EXIST_DEVICE_TOKEN;

@RestController
@RequestMapping("/app/push")
@Tag(name = "PUSH", description = "푸시 알림(쿡찌르기) API")
public class PushController {
    @Autowired
    private final PushService pushService;
    @Autowired
    private final CoupleService coupleService;
    @Autowired
    private final TokenService tokenService;

    public PushController(PushService pushService, CoupleService coupleService, TokenService tokenService){
        this.pushService = pushService;
        this.coupleService = coupleService;
        this.tokenService = tokenService;
    }

    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 상대방에게 푸시 알람을 주는 api입니다.",
            tags = "PUSH", summary = "푸시 알람 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "3050", description = "푸시 알람 전송을 실패하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PushMsgRes> pushMessage(@RequestBody PushMsgReq pushMsgReq){
        try{
            int userIdByJwt = tokenService.getUserId();
            String title = "띵동-!";
            String body = pushService.getPushMessage(userIdByJwt);
            if(body == null || body.equals("")) {
                body = "오늘 너의 하루가 궁금해 :)";
            }
            pushService.sendMessageTo(
                    pushMsgReq.getTargetToken(),
                    title,
                    body);
            return new BaseResponse<>(new PushMsgRes(userIdByJwt, title, body));
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 상대방에게 푸시 알람을 주는 api입니다.",
            tags = "PUSH", summary = "쿡찌르기 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "3050", description = "푸시 알람 전송을 실패하였습니다."),
            @ApiResponse(responseCode = "3051", description = "device token이 존재하지 않습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/partner")
    public BaseResponse<PushMsgRes> pushMessage(){
        try{
            int userIdByJwt = tokenService.getUserId();
            String title = "띵동-!";
            String body = pushService.getPushMessage(userIdByJwt);
            if(body == null || body.equals("")) {
                body = "오늘 너의 하루가 궁금해 :)";
            }
            pushService.sendMessageToPartner(userIdByJwt, title, body);
            return new BaseResponse<>(new PushMsgRes(userIdByJwt, title, body));
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
