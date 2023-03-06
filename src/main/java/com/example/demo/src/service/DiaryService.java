package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.DiaryDao;
import com.example.demo.src.data.dto.diary.DiaryReq;
import com.example.demo.src.data.dto.diary.DiaryRes;
import com.example.demo.utils.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.demo.config.BaseResponseStatus.FAIL_TO_UPLOAD_FILE;

@Service
public class DiaryService {
    private final DiaryDao diaryDao;
    private final S3Uploader s3Uploader;

    @Autowired
    public DiaryService(DiaryDao diaryDao, S3Uploader s3Uploader) {
        this.diaryDao = diaryDao;
        this.s3Uploader = s3Uploader;
    }

    public DiaryRes createDiary(int userId, DiaryReq postDiaryReq, MultipartFile imageFile) throws BaseException {
        String fileUrl = "";
        if(imageFile != null){
            try{
                fileUrl = s3Uploader.upload(imageFile, "images/diaries");
            }catch (BaseException | IOException exception){
                throw new BaseException(FAIL_TO_UPLOAD_FILE);
            }
        }
        int diaryId = diaryDao.createDiary(userId, postDiaryReq, fileUrl);
        return new DiaryRes(diaryId, postDiaryReq.getContent(), fileUrl, postDiaryReq.getWeather());
    }

    public DiaryRes modifyDiary(int userId, int diaryId, DiaryReq patchDiaryReq, MultipartFile imageFile) throws BaseException {
        String fileUrl = "";
        if(imageFile != null){
            try{
                fileUrl = s3Uploader.upload(imageFile, "images/diaries");
            }catch (BaseException | IOException exception){
                throw new BaseException(FAIL_TO_UPLOAD_FILE);
            }
        }
        diaryDao.modifyDiary(userId, diaryId, patchDiaryReq, fileUrl);
        return new DiaryRes(diaryId, patchDiaryReq.getContent(), fileUrl, patchDiaryReq.getWeather());
    }
}
