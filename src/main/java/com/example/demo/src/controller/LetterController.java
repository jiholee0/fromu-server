package com.example.demo.src.controller;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.letter.*;
import com.example.demo.src.service.LetterService;
import com.example.demo.src.service.PushService;
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
import static com.example.demo.config.BaseResponseStatus.PATCH_LETTER_INVALID_SCORE;

@RestController
@RequestMapping("app/letters")
@Tag(name = "LETTER", description = "편지 등록/조회, 별점주기/신고하기 API")
public class LetterController {
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final LetterService letterService;

    public LetterController(TokenService tokenService, LetterService letterService){
        this.tokenService = tokenService;
        this.letterService = letterService;
    }

    /**
     * 편지 쓰기 API
     * [POST] /letters
     */
    /**
     * PostLetterReq : content, stampNum(int)
     * PostLetterRes : letterId, sendMailboxName, receiveMailboxName
     * ERROR : JWT 관련, 우표 보유 현황 체크, 이외의 데이터 부재 에러
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 편지(내용, 우표 번호-1~6)를 입력하여 "+
                    "임의의 커플에게 편지를 보내는 api입니다. 선택한 우표의 개수가 차감됩니다.",
            tags = "LETTER", summary = "편지 쓰기 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostLetterRes> sendLetter(@Parameter(required = true) @RequestBody PostLetterReq postLetterReq){
        try{
            int userIdByJwt = tokenService.getUserId();
            PostLetterRes postLetterRes = letterService.sendLetter(userIdByJwt, postLetterReq);
            return new BaseResponse<>(postLetterRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 답장 쓰기 API
     * [POST] /letters/:letterId/reply
     */
    /**
     * PostReplyReq : content, letterId(답장하는 대상 편지 id)
     * PostReplyRes : letterId, sendMailboxName, receiveMailboxName
     * ERROR : JWT 관련, 이미 답장한 편지인지 체크, 이외의 데이터 부재 에러
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 letterId, content를 입력하여 "+
                    "답장을 보내는 api입니다.",
            tags = "LETTER", summary = "답장 쓰기 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PostMapping("/{letterId}/reply")
    public BaseResponse<PostLetterRes> sendLetterReply(@PathVariable("letterId") int letterId,
                                                  @Parameter(required = true) @RequestBody PostLetterReq postLetterReq){
        try{
            int userIdByJwt = tokenService.getUserId();
            PostLetterRes postLetterRes = letterService.sendLetterReply(userIdByJwt, letterId, postLetterReq);
            return new BaseResponse<>(postLetterRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 편지 1개 조회 API
     * [PATCH] /letters/:letterId/read
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 letterId로 편지를 조회하는 api입니다." +
                    "letterId, stampNum(1~6 / 개발자가 준 편지면 0), content, sendMailboxName, receiveMailboxName, time(yyyy-MM-dd HH:mm:ss), " +
                    "status(내가 받은 편지면 0 : 신고하기 / 답장하기 | 내가 보낸 편지면 1 | 답장 편지면 2 : 신고하기 / 별점 남기기), replyFlag(답장 여부), scoreFlag(감사인사 여부)",
            tags = "LETTER", summary = "편지 1개 조회/읽음 처리 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PatchMapping("/{letterId}/read")
    public BaseResponse<PatchReadLetterRes> getLetter(@PathVariable("letterId") int letterId){
        try{
            int userIdByJwt = tokenService.getUserId();
            PatchReadLetterRes letterInfo = letterService.readLetter(userIdByJwt, letterId);
            return new BaseResponse<>(letterInfo);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 편지함 조회 API
     * [GET] /letters/mailbox?type={type}
     */
    @Operation(method = "GET",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 "+
                    "받은 편지함, 보낸 편지함을 조회하는 api입니다. " +
                    "받은 편지함이면 type = 0, 보낸 편지함이면 type = 1이며, " +
                    "letterId, 우편함 이름, 작성 시간, 읽음 여부 list를 return합니다.",
            tags = "LETTER", summary = "편지함 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @GetMapping("/mailbox")
    public BaseResponse<List<GetLetterRes>> getMailbox(@Parameter(required = true) @RequestParam(required = true) String type){
        try{
            int userIdByJwt = tokenService.getUserId();
            if(type.equals("0")){
                List<GetLetterRes> getLetterList = letterService.getReceiveLetterList(userIdByJwt);
                return new BaseResponse<>(getLetterList);
            }
            else if(type.equals("1")){
                List<GetLetterRes> getLetterList = letterService.getSendLetterList(userIdByJwt);
                return new BaseResponse<>(getLetterList);
            }
            throw new BaseException(INVALID_REQ_PARAM);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 별점 주기 API
     * [PATCH] /letters/:letterId/score
     */
    @Operation(method = "PATCH",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 letterId를 입력하여 특정 편지에 별점을 주는 api입니다. ",
            tags = "LETTER", summary = "별점 주기 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "파일 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PatchMapping("/{letterId}/score")
    public BaseResponse<PatchScoreRes> scoreLetter(@PathVariable("letterId") int letterId,
                                             @Parameter(required = true) @RequestBody PatchScoreReq patchScoreReq){
        try{
            int userIdByJwt = tokenService.getUserId();
            if(patchScoreReq.getScore()>=0&&patchScoreReq.getScore()<=5){
                PatchScoreRes patchScoreRes = letterService.scoreLetter(userIdByJwt, letterId, patchScoreReq.getScore());
                return new BaseResponse<>(patchScoreRes);
            }
            else {
                throw new BaseException(PATCH_LETTER_INVALID_SCORE);
            }
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 신고하기 API
     * [POST] /letters/:letterId/report
     */
    @Operation(method = "POST",
            description = "Header-'X-ACCESS-TOKEN'에 JWT 값을 넣고 letterId, content를 입력하여 특정 편지를 신고하는 api입니다. "+
                    "reportId를 return 합니다. ",
            tags = "LETTER", summary = "신고하기 API - \uD83D\uDD12 JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2000", description = "JWT를 입력해주세요."),
            @ApiResponse(responseCode = "2001", description = "유효하지 않은 JWT입니다."),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(responseCode = "4002", description = "커플이 존재하지 않습니다.")
    })
    @ResponseBody
    @PostMapping("/{letterId}/report")
    public BaseResponse<Integer> score(@PathVariable("letterId") int letterId,
                                            @Parameter(required = true) @RequestBody PostReportReq postReportReq){
        try{
            int userIdByJwt = tokenService.getUserId();
            int reportId = letterService.report(userIdByJwt, letterId, postReportReq);
            return new BaseResponse<>(reportId);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
