package com.example.demo.src.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/notices")
@Tag(name = "NOTICE", description = "알림 API")
public class NoticeController {

    /**
     * 알림 읽음 처리 API
     * [PATCH] /notices/:noticeId/read
     */

    /**
     * 알림 조회 API
     * [GET] /notices?page={page}
     */
}
