package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.DiarybookDao;
import com.example.demo.src.data.dto.diarybook.*;
import com.example.demo.src.data.entity.Diarybook;
import com.example.demo.utils.S3Uploader;
import com.example.demo.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.FAIL_TO_UPLOAD_FILE;
import static com.example.demo.config.BaseResponseStatus.PATCH_COUPLES_INVALID_FIRSTMETDAY;
import static com.example.demo.utils.ValidationRegex.isRegexDay;


@Service
public class DiarybookService {
    private final DiarybookDao diarybookDao;
    private final S3Uploader s3Uploader;

    @Autowired
    public DiarybookService(DiarybookDao diarybookDao, S3Uploader s3Uploader) {
        this.diarybookDao = diarybookDao;
        this.s3Uploader = s3Uploader;
    }

    // 일기장 등록
    public PostDiarybookRes createDiarybook(int userId, PostDiarybookReq postDiarybookReq) throws BaseException {
        int diarybookId = diarybookDao.createDiarybook(userId, postDiarybookReq);
        return new PostDiarybookRes(diarybookId, postDiarybookReq.getCoverNum(), postDiarybookReq.getName());
    }

    // coupleId로 일기장 조회
    public Diarybook getDiarybookByCoupleId(int coupleId) throws BaseException {
        return diarybookDao.getDiarybookByCoupleId(coupleId);
    }
    // coupleId로 일기장 조회
    public Diarybook getDiarybookByDiarybookId(int diarybookId) throws BaseException {
        return diarybookDao.getDiarybookByDiarybookId(diarybookId);
    }
    // 일기장 전체 조회
    public List<Diarybook> getDiarybooks() throws BaseException {
        return diarybookDao.getDiarybooks();
    }

    // 일기장 표지 수정
    public int modifyDiarybookCoverNum(int userId, PatchDiarybookCoverNumReq patchDiarybookReq) throws BaseException{
        return diarybookDao.modifyDiarybookCover(userId, patchDiarybookReq.getCoverNum());
    }

    public int modifyDiarybookName(int userId, PatchDiarybookNameReq patchDiarybookReq) throws BaseException{
        return diarybookDao.modifyDiarybookName(userId, patchDiarybookReq.getName());
    }

    public int uploadDiarybookImage(int userId, MultipartFile imageFile) throws BaseException{
        String fileUrl = "";
        if(imageFile != null){
            try{
                fileUrl = s3Uploader.upload(imageFile, "images/diarybooks"); // S3 버킷의 images 디렉토리 안에 저장됨
            }catch (BaseException | IOException exception){
                throw new BaseException(FAIL_TO_UPLOAD_FILE);
            }
        }
        return diarybookDao.uploadDiarybookImage(userId, fileUrl);
    }
    // 일기장 삭제

    public int passDiarybook(int userId) throws BaseException {
        return diarybookDao.passDiarybook(userId);
    }

    public GetFirstPageRes getFirstPage(int userId) throws BaseException {
        return diarybookDao.getFirstPage(userId);
    }
}
