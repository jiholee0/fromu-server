package com.example.demo.src.controller;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.diary.DiaryReq;
import com.example.demo.src.data.dto.diary.DiaryRes;
import com.example.demo.src.service.DiaryService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/app/diaries")
@Tag(name = "DIARY", description = "일기 등록/조회/수정/삭제 API")
public class DiaryController {
    @Autowired
    private final DiaryService diaryService;
    @Autowired
    private final TokenService tokenService;

    public DiaryController(DiaryService diaryService, TokenService tokenService) {
        this.diaryService = diaryService;
        this.tokenService = tokenService;
    }

    /**
     * 일기 등록 API
     * [POST] /diaries
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 일기(내용, 사진, 날씨)를 입력하여 "+
                    "해당 유저 커플의 일기를 추가하는 api입니다.",
            tags = "DIARY", summary = "일기 등록 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2030", description = "아직 상대방이 일기장을 작성하지 않았습니다."),
            @ApiResponse(responseCode = "2031", description = "아직 일기장이 오지 않았습니다."),
            @ApiResponse(responseCode = "2032", description = "일기를 이미 작성하였습니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<DiaryRes> createDiary(@Parameter @RequestPart DiaryReq postDiaryReq, @Parameter(required = false) @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try{
            int userIdByJwt = tokenService.getUserId();
            DiaryRes postDiaryRes = diaryService.createDiary(userIdByJwt, postDiaryReq, imageFile);
            return new BaseResponse<>(postDiaryRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /** 일기 수정 API
     * [PATCH] /diaries/:diaryId
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 일기(내용, 사진, 날씨)를 입력하여 "+
                    "해당 유저 커플의 일기를 수정하는 api입니다. - 개발중",
            tags = "DIARY", summary = "일기 수정 API - \uD83D\uDD12 JWT")
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
    @PatchMapping("/{diaryId}")
    public BaseResponse<DiaryRes> modifyDiary(@PathVariable("diaryId") int diaryId,
                                                  @Parameter(required = true) @RequestBody DiaryReq patchDiaryReq,
                                                  @Parameter @RequestPart(value = "imageFile", required = false) MultipartFile imageFile)
    {
        try{
            int userIdByJwt = tokenService.getUserId();
            DiaryRes patchDiaryRes = diaryService.modifyDiary(userIdByJwt, diaryId, patchDiaryReq, imageFile);
            return new BaseResponse<>(patchDiaryRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
