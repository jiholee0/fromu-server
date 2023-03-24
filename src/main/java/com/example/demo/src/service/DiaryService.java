package com.example.demo.src.service;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dao.DiaryDao;
import com.example.demo.src.data.dto.diary.*;
import com.example.demo.utils.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    public int createDiary(int userId, DiaryReq postDiaryReq, MultipartFile imageFile) throws BaseException {
        String fileUrl = "";
        if(imageFile != null){
            try{
                fileUrl = s3Uploader.upload(imageFile, "images/diaries");
            }catch (BaseException | IOException exception){
                throw new BaseException(FAIL_TO_UPLOAD_FILE);
            }
        }
        return diaryDao.createDiary(userId, postDiaryReq, fileUrl);
    }

    public void modifyDiary(int userId, int diaryId, DiaryReq patchDiaryReq, MultipartFile imageFile) throws BaseException {
        String fileUrl = "";
        if(imageFile != null){
            try{
                fileUrl = s3Uploader.upload(imageFile, "images/diaries");
            } catch (BaseException | IOException exception){
                throw new BaseException(FAIL_TO_UPLOAD_FILE);
            }
        }
        diaryDao.modifyDiary(userId, diaryId, patchDiaryReq, fileUrl);
    }

    public DiaryRes getDiaryByDiaryId(int diaryId) throws BaseException {
        return diaryDao.getDiaryByDiaryId(diaryId);
    }

    public DiaryResWithUserId getDiaryWithUserIdByDiaryId(int diaryId) throws BaseException {
        return diaryDao.getDiaryWithUserIdByDiaryId(diaryId);
    }

    public List<String> getMonthList(int diarybookId) throws BaseException {
        return diaryDao.getMonthList(diarybookId);
    }
    public GetDiaryListByMonthRes getDiaryListByMonth(int diarybookId, String month) throws BaseException {
        return diaryDao.getDiaryListByMonth(diarybookId, month);
    }

    public List<Integer> getDiaries(int diarybookId) throws BaseException {
        return diaryDao.getDiaries(diarybookId);
    }

    public List<GetDiariesWithUserId> getDiariesWithUserId(int diarybookId) throws BaseException {
        return diaryDao.getDiariesWithUserId(diarybookId);
    }

    public void deleteDiary(int userId, int diaryId) throws BaseException {
        diaryDao.deleteDiary(userId, diaryId);
    }
}
