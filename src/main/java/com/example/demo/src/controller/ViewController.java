package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.view.MailboxViewRes;
import com.example.demo.src.data.dto.view.MainViewRes;
import com.example.demo.src.data.dto.view.NoticeViewRes;
import com.example.demo.src.service.ViewService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/view")
@Tag(name = "VIEW", description = "뷰 API")
public class ViewController {
    @Autowired
    private final ViewService viewService;
    @Autowired
    private final TokenService tokenService;

    public ViewController(ViewService viewService, TokenService tokenService) {
        this.viewService = viewService;
        this.tokenService = tokenService;
    }

    /**
     * 메인 뷰 API
     * [GET] /view/main
     * return : 닉네임, 상대방 닉네임, dday, 일기장 data
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 홈 화면의 모든 데이터를 조회하는 api입니다. "+
                    "닉네임, 연인 닉네임, dDay, 일기장 상태(일기장이 생성되지 않았으면 0 / " +
                    "일기장이 나에게 있으면 1 / " +
                    "일기장이 오는 중이면 2 / " +
                    "일기장이 가는 중이면 3 / " +
                    "일기장이 상대한테 있으면 4), 일기장 정보(일기장 상태가 0이면 NULL, writeFlag로 일기 작성 여부 판별)",
            tags = "VIEW", summary = "메인 뷰 조회 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "3010", description = "dday 계산에 실패하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/main")
    public BaseResponse<MainViewRes> mainView(){
        try{
            int userIdByJwt = tokenService.getUserId();
            MainViewRes mainViewRes = viewService.mainView(userIdByJwt);
            return new BaseResponse<>(mainViewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 우편함 뷰 API
     * [GET] /views/mailbox
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 우편함 화면의 데이터를 조회하는 api입니다. "+
                    "커플 id(coupleId), 우편함 이름(mailboxName), 읽지 않은 편지 중 제일 최근 편지 id(newLetterId) : 새로운 편지가 없으면 0",
            tags = "VIEW", summary = "우편함 뷰 조회 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/mailbox")
    public BaseResponse<MailboxViewRes> mailboxView(){
        try{
            int userIdByJwt = tokenService.getUserId();
            MailboxViewRes mailboxViewRes = viewService.mailboxView(userIdByJwt);
            return new BaseResponse<>(mailboxViewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 프롬 수 조회 API
     * [GET] /views/fromCount
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 프롬 수를 조회하는 api입니다. ",
            tags = "VIEW", summary = "프롬 수 조회 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/fromCount")
    public BaseResponse<Integer> getFrom(){
        try{
            int userIdByJwt = tokenService.getUserId();
            int fromCount = viewService.getFrom(userIdByJwt);
            return new BaseResponse<>(fromCount);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 우표 수 조회 API
     * [GET] /views/stampCount
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 우표 수를 조회하는 api입니다. 1번부터 6번까지 우표의 개수를 순서대로 return합니다. ",
            tags = "VIEW", summary = "우표 수 조회 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/stampCount")
    public BaseResponse<List<Integer>> getStamp(){
        try{
            int userIdByJwt = tokenService.getUserId();
            List<Integer> stampViewRes = viewService.getStamp(userIdByJwt);
            return new BaseResponse<>(stampViewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 알림 조회 API
     * [GET] /views/notice
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 받은 알림을 조회하는 api입니다.",
            tags = "VIEW", summary = "알림 조회 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/notice")
    public BaseResponse<List<NoticeViewRes>> getNotice(){
        try{
            int userIdByJwt = tokenService.getUserId();
            List<NoticeViewRes> noticeViewRes = viewService.getNotice(userIdByJwt);
            return new BaseResponse<>(noticeViewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
