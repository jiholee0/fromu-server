package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.couple.PatchCoupleRes;
import com.example.demo.src.data.dto.schedule.ScheduleReq;
import com.example.demo.src.data.dto.schedule.ScheduleRes;
import com.example.demo.src.service.PushService;
import com.example.demo.src.service.ScheduleService;
import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_REQ_PARAM;

@RestController
@RequestMapping("/app/schedules")
@Tag(name = "SCHEDULE", description = "일정 등록/조회/수정/삭제 API")
public class ScheduleController {
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final ScheduleService scheduleService;
    @Autowired
    private final PushService pushService;

    public ScheduleController(TokenService tokenService, ScheduleService scheduleService, PushService pushService) {
        this.tokenService = tokenService;
        this.scheduleService = scheduleService;
        this.pushService = pushService;
    }


    /**
     * 일정 추가 api
     * [POST] /schedules
     * 일정 날짜, 일정 이름
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값과 일정 내용, 일정 날짜(ex - 20230316)를 입력하여 일정을 추가하는 api입니다. ",
            tags = "SCHEDULE", summary = "일정 추가 API - \uD83D\uDD12 JWT")
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
    @PostMapping("")
    public BaseResponse<Integer> createSchedule(@RequestBody ScheduleReq postScheduleReq){
        try {
            int userIdByJwt = tokenService.getUserId();
            int scheduleId = scheduleService.createSchedule(userIdByJwt, postScheduleReq);
            pushService.sendMessageToPartnerFree(userIdByJwt,"끄적끄적","새로운 일정이 등록되었어!");
            return new BaseResponse<>(scheduleId);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 일정 삭제 api
     * [DELETE] /schedules/:scheduleId
     */
    @Operation(method = "DELETE",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값과 파라미터에 일정 id를 넣어 일정을 삭제하는 api입니다. ",
            tags = "SCHEDULE", summary = "일정 삭제 API - \uD83D\uDD12 JWT")
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
    @DeleteMapping("/{scheduleId}")
    public BaseResponse<Integer> deleteSchedule(@PathVariable("scheduleId") int scheduleId){
        try {
            int userIdByJwt = tokenService.getUserId();
            scheduleService.deleteSchedule(userIdByJwt, scheduleId);
            return new BaseResponse<>(scheduleId);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 일정 편집 api
     * [PATCH] /schedules/:scheduleId
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값과 파라미터에 일정 id, requestBody에는 일정 내용을 넣어 일정을 편집하는 api입니다. ",
            tags = "SCHEDULE", summary = "일정 편집 API - \uD83D\uDD12 JWT")
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
    @PatchMapping("/{scheduleId}")
    public BaseResponse<Integer> modifySchedule(@PathVariable("scheduleId") int scheduleId, @RequestBody ScheduleReq patchScheduleReq){
        try {
            int userIdByJwt = tokenService.getUserId();
            scheduleService.modifySchedule(userIdByJwt, scheduleId, patchScheduleReq);
            return new BaseResponse<>(scheduleId);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 해당 달의 일정 조회 api
     * [GET] /schedules?month={month}&&date={date}
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값과 파라미터에 month(ex-202303)와 date(ex-03)을 넣어 일정을 조회하는 api입니다. month는 필수, date는 필수가 아닙니다." +
                    "date를 입력하지 않으면 특정 달의 모든 일정을 조회합니다. **기념일이라면 일정의 닉네임은 \"우리\"입니다.**",
            tags = "SCHEDULE", summary = "특정 달 또는 날짜의 일정 전체 조회 API - \uD83D\uDD12 JWT")
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
    @GetMapping("")
    public BaseResponse<List<ScheduleRes>> getSchedules(@Parameter(required = true) @RequestParam(name = "month", required = true) String month,
                                                               @Parameter(required = false) @RequestParam(name = "date", required = false) String date){
        try {
            if(month == null) throw new BaseException(INVALID_REQ_PARAM);
            if(month.length() != 6) throw new BaseException(INVALID_REQ_PARAM);
            int userIdByJwt = tokenService.getUserId();
            if(date == null || date.equals("")){
                return new BaseResponse<>(scheduleService.getSchedulesByMonth(userIdByJwt, month));
            }
            else {
                if(date.length() != 2) throw new BaseException(INVALID_REQ_PARAM);
                return new BaseResponse<>(scheduleService.getSchedulesByDate(userIdByJwt, month, date));
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 해당 달의 일정이 존재하는 날짜 리스트 조회 api
     * [GET] /schedules/list?month={month}
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값과 파라미터에 month(ex-202303)을 넣어 일정이 존재하는 날짜를 List<Integer>로 return하는 api입니다. ",
            tags = "SCHEDULE", summary = "특정 달의 일정이 존재하는 날짜 리스트 조회 API - \uD83D\uDD12 JWT")
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
    @GetMapping("/list")
    public BaseResponse<List<Integer>> getDateListByMonth(@Parameter(required = true) @RequestParam(name = "month", required = true) String month){
        try {
            if(month == null) throw new BaseException(INVALID_REQ_PARAM);
            if(month.length() != 6) throw new BaseException(INVALID_REQ_PARAM);
            int userIdByJwt = tokenService.getUserId();
            List<Integer> scheduleList = scheduleService.getDateListByMonth(userIdByJwt, month);
            return new BaseResponse<>(scheduleList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
