package com.example.demo.src.controller;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.data.dto.push.PushMsgReq;
import com.example.demo.src.service.PushService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/app/test")
@Tag(name = "TEST", description = "테스트 API")
public class TestController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PushService pushService;
    @Autowired
    public TestController(PushService pushService) {
        this.pushService = pushService;
    }

    /**
     * 로그 테스트 API
     * [GET] /test/log
     * @return String
     */
    @Operation(method = "GET", description = "테스트 로그 API : 테스트 로그를 찍는 api입니다.", tags = "TEST", summary = "테스트 로그 API")
    @ResponseBody
    @GetMapping("/log")
    public String getAll() {
        System.out.println("테스트");
//        trace, debug 레벨은 Console X, 파일 로깅 X
//        logger.trace("TRACE Level 테스트");
//        logger.debug("DEBUG Level 테스트");

//        info 레벨은 Console 로깅 O, 파일 로깅 X
        logger.info("INFO Level 테스트");
//        warn 레벨은 Console 로깅 O, 파일 로깅 O
        logger.warn("Warn Level 테스트");
//        error 레벨은 Console 로깅 O, 파일 로깅 O (app.log 뿐만 아니라 error.log 에도 로깅 됨)
//        app.log 와 error.log 는 날짜가 바뀌면 자동으로 *.gz 으로 압축 백업됨
        logger.error("ERROR Level 테스트");

        return "Success Test";
    }

    /** 테스트 API
     * [GET] /test
     */
    @Operation(method = "GET", description = "테스트 API : 서버가 켜져 있는지 테스트하는 api입니다.", tags = "TEST", summary = "테스트 API")
    @GetMapping("")
    public BaseResponse<Date> test() {
        Date date = new Date();
        return new BaseResponse<>(date);
    }

    /**
     * scheduled 테스트 API
     */
//    @Operation(method = "GET", description = "테스트 API : 1분 후의 시간을 출력하는지 테스트하는 api입니다.", tags = "TEST", summary = "테스트 API")
//    @GetMapping("/scheduled")
//    public BaseResponse<Date> testScheduled() throws BaseException {
//        Date date = new Date();
//        // date = pushService.testScheduled();
//        return new BaseResponse<>(date);
//    }

    /**
    * push 테스트 API
    */
    @Operation(method = "POST",
            description = "IOS 특정 Device에 푸시 알람을 주는 api입니다.",
            tags = "TEST", summary = "IOS 푸시 알람 test API")
    @ResponseBody
    @PostMapping("/push")
    public BaseResponse<String> pushMessage(@RequestBody PushMsgReq pushMsgReq){
        try{
            pushService.sendMessageTo(
                    pushMsgReq.getTargetToken(),
                    "알림이에요",
                    "잘 도착했나요?");
            return new BaseResponse<>("SUCCES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
