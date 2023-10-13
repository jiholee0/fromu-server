package com.example.demo.src.controller;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.diarybook.*;
import com.example.demo.src.data.entity.Diarybook;
import com.example.demo.src.service.DiarybookService;
import com.example.demo.src.service.PushService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/app/diarybooks")
@Tag(name = "DIARYBOOK", description = "일기장 등록/조회/수정/삭제 API")
public class DiarybookController {
    @Autowired
    private final DiarybookService diarybookService;
    @Autowired
    private final PushService pushService;
    @Autowired
    private final TokenService tokenService;

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public DiarybookController(DiarybookService diarybookService, PushService pushService, TokenService tokenService) {
        this.diarybookService = diarybookService;
        this.pushService = pushService;
        this.tokenService = tokenService;
    }

    /**
     * 일기장 등록 API
     * [POST] /diarybooks
     * [jwt] -> coupleId , 표지 num, 이름 / [jwt] 로 얻은 userId = 최근 유저 id
     * return : diarybookId
     */
    @Operation(method = "POST",
        description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 일기장 정보(표지 번호, 이름)을 입력하여 "+
                "해당 유저 커플의 일기장을 추가하는 api입니다.",
        tags = "DIARYBOOK", summary = "일기장 등록 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2021", description = "이미 일기장이 존재합니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostDiarybookRes> createDiarybook(@Parameter @RequestBody PostDiarybookReq postDiarybookReq){
        try {
            int userIdByJwt = tokenService.getUserId();
            PostDiarybookRes postDiarybookRes = diarybookService.createDiarybook(userIdByJwt, postDiarybookReq);
            return new BaseResponse<>(postDiarybookRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 일기장 표지 수정 API
     * [PATCH] /diarybooks/coverNum
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "일기장의 표지를 변경하는 api입니다.",
            tags = "DIARYBOOK", summary = "일기장 표지 변경 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/coverNum")
    public BaseResponse<PatchDiarybookRes> modifyDiarybookCoverNum(@RequestBody PatchDiarybookCoverNumReq patchDiarybookReq) {
        try {
            int userIdByJwt = tokenService.getUserId();
            int diarybookId = diarybookService.modifyDiarybookCoverNum(userIdByJwt, patchDiarybookReq);
            return new BaseResponse<>(new PatchDiarybookRes(userIdByJwt, diarybookId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 일기장 이름 수정 API
     * [PATCH] /diarybooks/name
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "일기장의 이름을 변경하는 api입니다.",
            tags = "DIARYBOOK", summary = "일기장 이름 변경 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/name")
    public BaseResponse<PatchDiarybookRes> modifyDiarybookName(@RequestBody PatchDiarybookNameReq patchDiarybookReq) {
        try {
            int userIdByJwt = tokenService.getUserId();
            int diarybookId = diarybookService.modifyDiarybookName(userIdByJwt, patchDiarybookReq);
            return new BaseResponse<>(new PatchDiarybookRes(userIdByJwt, diarybookId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 일기장 전체 조회 API
     * [GET] /diarybooks
     */
    @Operation(method = "GET",
            description = "일기장 객체 그대로 반환하는 api입니다.", tags = "DIARYBOOK", summary = "모든 일기장 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<Diarybook>> getDiarybooks() {
        try {
            List<Diarybook> diarybookList = diarybookService.getDiarybooks();
            return new BaseResponse<>(diarybookList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * couple의 일기장 조회 API
     * [GET] /diarybooks/couples/:coupleId
     */
    @Operation(method = "GET",
            description = "일기장 객체 그대로 반환하는 api입니다.", tags = "DIARYBOOK", summary = "coupleId로 일기장 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/couples/{coupleId}")
    public BaseResponse<Diarybook> getDiarybookByCoupleId(@PathVariable("coupleId") int coupleId) {
        try {
            Diarybook diarybook = diarybookService.getDiarybookByCoupleId(coupleId);
            return new BaseResponse<>(diarybook);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 일기장 1개 조회 API
     * [GET] /diarybooks/:diarybookId
     */
    @Operation(method = "GET",
            description = "일기장 객체 그대로 반환하는 api입니다.", tags = "DIARYBOOK", summary = "diarybookId로 일기장 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/{diarybookId}")
    public BaseResponse<Diarybook> getDiarybookByDiarybookId(@PathVariable("diarybookId") int diarybookId) {
        try {
            Diarybook diarybook = diarybookService.getDiarybookByDiarybookId(diarybookId);
            return new BaseResponse<>(diarybook);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 일기장 삭제 API
     * [PATCH] /diarybooks/d
     */

    /**
     * 일기장 내지 첫장 추가 및 변경 API
     * [PATCH] /diarybooks/image
     */
    @Operation(method = "PATCH",
    description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
            "일기장 내지 첫장을 추가 및 변경하는 api입니다.",
    tags = "DIARYBOOK", summary = "일기장 내지 첫장 추가 및 변경 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PatchMapping("/image")
    public BaseResponse<PatchDiarybookRes> uploadDiarybookImage(@Parameter @RequestPart(value = "imageFile") MultipartFile imageFile){
        try{
            int userIdByJwt = tokenService.getUserId();
            int diarybookId = diarybookService.uploadDiarybookImage(userIdByJwt, imageFile);
            return new BaseResponse<>(new PatchDiarybookRes(userIdByJwt, diarybookId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 일기장 전송하기 API
     * [PATCH] /diarybooks/pass
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "일기장을 전송하는 api입니다.",
            tags = "DIARYBOOK", summary = "일기장 전송 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2070", description = "일기장이 해당 유저에게 없습니다."),
            @ApiResponse(responseCode = "2071", description = "아직 일기를 작성하지 않아 일기장을 보낼 수 없습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/pass")
    public BaseResponse<PatchDiarybookRes> passDiarybook(){
        try{
            int userIdByJwt = tokenService.getUserId();
            int diarybookId = diarybookService.passDiarybook(userIdByJwt);
            pushService.sendMessageToPartnerFree(userIdByJwt,"두근두근","일기장이 내게로 오고 있어");
            scheduler.schedule(() -> {
                try {
                    System.out.println("[스케줄러 실행] : 연인에게 일기장 도착 메세지");
                    pushService.sendMessageToPartnerFree(userIdByJwt,"드디어...!","일기장이 나에게 왔어. 연인의 일기를 보러 가볼까?");
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }, 3, TimeUnit.SECONDS);
            scheduler.schedule(() -> {
                try {
                    if(!diarybookService.isGetTurnUserId(userIdByJwt)){
                        System.out.println("스케줄러 실행");
                        pushService.sendMessageFree(userIdByJwt,"띵띵ㅇ띧띵띵ㅇㄷ띵ㄸ동","일기장을 아직도 안 보내다니. 벨을 누르러 가볼까?");
                        pushService.sendMessageToPartnerFree(userIdByJwt,"(속닥속닥)","연인이 나의 일상이 궁금하대!");
                    }
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }, 24, TimeUnit.HOURS);
            return new BaseResponse<>(new PatchDiarybookRes(userIdByJwt, diarybookId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 첫장 내지 조회 API
     * [GET] /diarybooks/firstPage
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "일기장 내지 첫장을 조회하는 api입니다.",
            tags = "DIARYBOOK", summary = "일기장 내지 첫장 조회 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/firstPage")
    public BaseResponse<GetFirstPageRes> getFirstPage() {
        try {
            int userIdByJwt = tokenService.getUserId();
            GetFirstPageRes firstPage = diarybookService.getFirstPage(userIdByJwt);
            return new BaseResponse<>(firstPage);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
