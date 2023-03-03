package com.example.demo.src.controller;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.diarybook.PostDiarybookReq;
import com.example.demo.src.data.dto.diarybook.PostDiarybookRes;
import com.example.demo.src.data.entity.Diarybook;
import com.example.demo.src.service.DiarybookService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_REQ_PARAM;

@RestController
@RequestMapping("/app/diarybooks")
@Tag(name = "DIARYBOOK", description = "일기장 등록/조회/수정/삭제 API")
public class DiarybookController {
    @Autowired
    private final DiarybookService diarybookService;
    @Autowired
    private final TokenService tokenService;

    public DiarybookController(DiarybookService diarybookService, TokenService tokenService) {
        this.diarybookService = diarybookService;
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
        tags = "DIARYBOOK", summary = "일기장 등록 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/")
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
     * 일기장 수정 API
     * [PATCH] /diarybooks/:typeNum
     * 표지 수정 : typeNum = 1
     * 이름 수정 : typeNum = 2
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣어 " +
                    "경로 변수 typeNum에 따라 일기장의 표지 또는 이름을 변경하는 api입니다.",
            tags = "DIARYBOOK", summary = "일기장 정보 변경(표지, 이름) API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "2004", description = "파라미터 값을 확인해주세요."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
    })
    @ResponseBody
    @PatchMapping("/{typeNum}")
    public BaseResponse<Integer> modifyUser(@Parameter(name = "typeNum", description = "1(표지) / 2(이름)", required = true) @PathVariable("typeNum") int type, @RequestBody Object object) {
        try {
            int userIdByJwt = tokenService.getUserId();
            if (type == 1 || type == 2) {
                diarybookService.modifyDiarybook(userIdByJwt, type, object);
                return new BaseResponse<>(userIdByJwt);
            } else {
                throw new BaseException(INVALID_REQ_PARAM);
            }
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
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
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
            @ApiResponse(responseCode = "4001", description = "데이터가 존재하지 않습니다.")
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
}
