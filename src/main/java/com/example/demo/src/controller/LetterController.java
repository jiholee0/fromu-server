package com.example.demo.src.controller;

import com.example.demo.utils.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/letters")
@Tag(name = "LETTER", description = "편지 등록/조회, 별점주기/신고하기 API")
public class LetterController {
//    @Autowired
//    private final TokenService tokenService;
//    @Autowired
//    private final LetterService letterService;
//
//    public LetterController(TokenService tokenService, LetterService letterService){
//        this.tokenService = tokenService;
//        this.letterService = letterService;
//    }

    /**
     * 편지 쓰기 API
     * [POST] /letters
     */
    /**
     * PostLetterReq : content, stampId
     * PostLetterRes : letterId, sendMailboxName, receiveMailboxName
     * ERROR : JWT 관련, 우표 보유 현황 체크, 이외의 데이터 부재 에러
     */

    /**
     * 답장 쓰기 API
     * [POST] /letters/reply
     */
    /**
     * PostReplyReq : content, letterId
     * PostReplyRes : letterId, sendMailboxName, receiveMailboxName
     * ERROR : JWT 관련, 이미 답장한 편지인지 체크, 이외의 데이터 부재 에러
     */

    /**
     * 편지 1개 조회 API
     * [GET] /letters/:letterId
     */
    /**
     * PathVariable : letterId
     * GetLetterRes : stampId, content, sendMailboxName, receiveMailboxName, time,
     *              status(내가 보낸 편지면 0 | 내가 받은 편지면 1 | 답장 편지면 2)
     * ERROR : JWT 관련, 이외의 데이터 부재 에러
     */

    /**
     * 우편함 조회 API
     * [GET] /letters/mailbox?type={type}
     */
    /**
     * QueryString : type(받은 편지함이면 0 | 보낸 편지함이면 1)
     * GetMailboxRes : List<letterId, mailboxName, date>
     * ERROR : JWT 관련, type에러, 이외의 데이터 부재 에러
     */

    /**
     * 별점 주기 API
     * [PATCH] /letters/:letterId/score
     */
    /**
     * PathVariable : letterId
     * PostScoreReq : score
     * PostScoreRes : coupleId, fromNum
     */

    /**
     * 신고하기 API
     * [POST] /letters/report
     */
    /**
     * PathVariable : letterId
     * PostReportRes : reportId
     */



}
