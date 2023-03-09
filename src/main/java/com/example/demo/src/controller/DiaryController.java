package com.example.demo.src.controller;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.diary.DiaryReq;
import com.example.demo.src.data.dto.diary.DiaryRes;
import com.example.demo.src.data.dto.diary.GetDiaryListByMonthRes;
import com.example.demo.src.service.DiaryService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexMonth;

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
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 일기(내용, 사진, 날씨-'SUNNY/CLOUD/RAINY)를 입력하여 "+
                    "해당 유저 커플의 일기를 추가하는 api입니다.",
            tags = "DIARY", summary = "일기 등록 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2023", description = "날씨 형식을 확인해주세요."),
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
    public BaseResponse<Integer> createDiary(@Parameter(required = true) @RequestPart(value = "postDiaryReq", required = true) DiaryReq postDiaryReq,
                                             @Parameter(required = false) @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try{
            if(postDiaryReq.getWeather().equalsIgnoreCase("SUNNY") || postDiaryReq.getWeather().equalsIgnoreCase("CLOUD") || postDiaryReq.getWeather().equalsIgnoreCase("RAINY")) {
                int userIdByJwt = tokenService.getUserId();
                int diaryId = diaryService.createDiary(userIdByJwt, postDiaryReq, imageFile);
                return new BaseResponse<>(diaryId);
            }
            throw new BaseException(POST_DIARIES_INVALID_WEATHER);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @Operation(method = "POST",
            description = "Multipart imageFile 테스트용 api",
            tags = "DIARY", summary = "테스트 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다.")
    })
    @ResponseBody
    @PostMapping("/test")
    public BaseResponse<Integer> createDiary(@Parameter(required = false) @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try{
            DiaryReq postDiaryReq = new DiaryReq("content","sunny");
            if(postDiaryReq.getWeather().equalsIgnoreCase("SUNNY") || postDiaryReq.getWeather().equalsIgnoreCase("CLOUD") || postDiaryReq.getWeather().equalsIgnoreCase("RAINY")) {
                int diaryId = 0;
                return new BaseResponse<>(diaryId);
            }
            throw new BaseException(POST_DIARIES_INVALID_WEATHER);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /** 일기 수정 API
     * [PATCH] /diaries/:diaryId
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 일기(내용, 사진(null 가능), 날씨)를 입력하여 "+
                    "해당 유저 커플의 일기를 수정하는 api입니다. 수정한 diaryId와 수정한 결과(내용, 사진, 날씨), 날짜, 요일, 작성자 닉네임을 return하며, " +
                    "사진이 NULL인 경우에는 기존 저장되어 있는 사진을 return합니다.",
            tags = "DIARY", summary = "일기 수정 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2002", description = "권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode = "2072", description = "날씨 형식을 확인해주세요."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다."),
            @ApiResponse(responseCode = "4004", description = "일기가 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PostMapping("/{diaryId}")
    public BaseResponse<DiaryRes> modifyDiary(@PathVariable("diaryId") int diaryId,
                                                  @Parameter(required = true) @RequestPart(value = "patchDiaryReq", required = true) DiaryReq patchDiaryReq,
                                                  @Parameter(required = false) @RequestPart(value = "imageFile", required = false) MultipartFile imageFile)
    {
        try{
            if(patchDiaryReq.getWeather().equalsIgnoreCase("SUNNY") || patchDiaryReq.getWeather().equalsIgnoreCase("CLOUD") || patchDiaryReq.getWeather().equalsIgnoreCase("RAINY")) {
                int userIdByJwt = tokenService.getUserId();
                DiaryRes patchDiaryRes = diaryService.modifyDiary(userIdByJwt, diaryId, patchDiaryReq, imageFile);
                return new BaseResponse<>(patchDiaryRes);
            }
            throw new BaseException(PATCH_DIARIES_INVALID_WEATHER);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * diaryId로 일기 1개 조회
     * [GET] /diaries/:diaryId
     */
    @Operation(method = "GET",
    description = "diaryId로 일기를 조회하는 api입니다. 일기 Id, 내용, 사진 url, 날씨, 날짜(ex : 20230305), 요일(ex : 1(월)~7(일)), 작성자 nickname을 return합니다.",
            tags = "DIARY", summary = "diaryId로 일기 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다."),
            @ApiResponse(responseCode = "4004", description = "일기가 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/{diaryId}")
    public BaseResponse<DiaryRes> getDiaryByDiaryId(@PathVariable("diaryId") int diaryId){
        try{
            DiaryRes patchDiaryRes = diaryService.getDiaryByDiaryId(diaryId);
            return new BaseResponse<>(patchDiaryRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * diarybookId로 월챕터 조회
     * [GET] /diaries/monthList/:diarybookId
     */
    @Operation(method = "GET",
            description = "diarybookId로 일기 월챕터를 조회하는 api입니다. 해당 일기장에 일기가 작성된 년월(ex : 202303, 202302)을 내림차순으로 return합니다.",
            tags = "DIARY", summary = "diarybookId로 일기 월챕터 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/monthList/{diarybookId}")
    public BaseResponse<List<String>> getDiaryMonth(@PathVariable("diarybookId") int diarybookId){
        try{
            List<String> monthList = diaryService.getMonthList(diarybookId);
            return new BaseResponse<>(monthList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * diarybookId로 월별 일기 list 조회
     * [GET] /diaries/byMonth/:diarybookId?month={month}
     */
    @Operation(method = "GET",
            description = "diarybookId로 월별 일기 list를 조회하는 api입니다. month 값을 쿼리 스트링으로 넣어주세요.(ex : 202303) " +
                    "month 값이 없는 경우 error가 발생하므로 유의해주세요.",
            tags = "DIARY", summary = "diarybookId로 월별 일기 list 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2004", description = "파라미터 값을 확인해주세요."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/byMonth/{diarybookId}")
    public BaseResponse<GetDiaryListByMonthRes> getDiaryListByMonth(@PathVariable("diarybookId") int diarybookId,
                                                                        @Parameter(required = true) @RequestParam(required = true) String month){
        try {
            GetDiaryListByMonthRes diaryList;
            if (month == null) {
                throw new BaseException(INVALID_REQ_PARAM);
            } else if (month.equals("")){
                throw new BaseException(INVALID_REQ_PARAM);
            } else if(isRegexMonth(month)){
                diaryList = diaryService.getDiaryListByMonth(diarybookId, month);
            } else {
                throw new BaseException(INVALID_REQ_PARAM);
            }
            return new BaseResponse<>(diaryList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * diarybookId로 모든 일기 오름차순으로 조회 API
     * [GET] /diaries/all/:diarybookId
     */
    @Operation(method = "GET",
            description = "diarybookId로 모든 일기를 오름차순으로 조회하는 api입니다. 해당 일기장에 작성된 일기 ID를 모두 return합니다.",
            tags = "DIARY", summary = "diarybookId로 모든 일기 오름차순으로 조회 API(최근 순)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "4003", description = "일기장이 존재하지 않습니다.")
    })
    @ResponseBody
    @GetMapping("/all/{diarybookId}")
    public BaseResponse<List<Integer>> getDiaries(@PathVariable("diarybookId") int diarybookId){
        try{
            List<Integer> diaryIdList = diaryService.getDiaries(diarybookId);
            return new BaseResponse<>(diaryIdList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 일기 삭제 API
     * [PATCH] /diaries/:diaryId/d
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 일기를 삭제하는 api입니다. ",
            tags = "DIARY", summary = "일기 삭제 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2002", description = "권한이 없는 유저의 접근입니다."),
            @ApiResponse(responseCode = "4004", description = "일기가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/{diaryId}/d")
    public BaseResponse<Integer> deleteDiary(@PathVariable("diaryId") int diaryId){
        try{
            int userIdByJwt = tokenService.getUserId();
            int deletedDiaryId = diaryService.deleteDiary(userIdByJwt, diaryId);
            return new BaseResponse<>(deletedDiaryId);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
