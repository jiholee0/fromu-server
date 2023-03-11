package com.example.demo.src.data.dao;

import com.example.demo.config.BaseException;
import com.example.demo.src.data.dto.letter.*;
import com.example.demo.src.data.entity.*;
import com.example.demo.src.service.LetterService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class LetterDao {
    @Autowired
    LetterRepository letterRepository;
    @Autowired
    CoupleRepository coupleRepository;
    @Autowired
    ReportRepository reportRepository;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    Date date = new Date();

    @Transactional
    public PostLetterRes sendLetter(int userId, PostLetterReq postLetterReq, Couple receiveCouple) throws BaseException {
        Optional<Couple> sendCouple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        date = new Date();
        Letter letter = postLetterReq.toEntity(0,userId,sendCouple.get().getCoupleId(),receiveCouple.getCoupleId(), date);
        letterRepository.save(letter);
        return new PostLetterRes(letter.getLetterId(),sendCouple.get().getMailboxName(),receiveCouple.getMailboxName());
    }

    @Transactional
    public PostLetterRes sendLetterReply(int userId, int letterId, PostLetterReq postLetterReq) throws BaseException {
        Optional<Couple> sendCouple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId, userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Letter> refLetter = Optional.of(letterRepository.findById(letterId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_LETTER)
        ));
        if (refLetter.get().getRefLetterId()!=0){
            throw new BaseException(POST_LETTERS_ALREADY_REPLY);
        }
        Optional<Couple> receiveCouple = Optional.of(coupleRepository.findById(refLetter.get().sendCoupleId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        date = new Date();
        Letter letter = postLetterReq.toEntity(letterId,userId,sendCouple.get().getCoupleId(),receiveCouple.get().getCoupleId(), date);
        letterRepository.save(letter);
        return new PostLetterRes(letter.getLetterId(),sendCouple.get().getMailboxName(),receiveCouple.get().getMailboxName());
    }

    @Transactional
    public PatchReadLetterRes readLetter(int userId, int letterId) throws BaseException {
        Optional<Letter> letter = Optional.of(letterRepository.findById(letterId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_LETTER)
        ));
        Optional<Couple> sendCouple = Optional.of(coupleRepository.findById(letter.get().getSendCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        Optional<Couple> receiveCouple = Optional.of(coupleRepository.findById(letter.get().getReceiveCoupleId()).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        int status = -1;
        boolean replyFlag = false;
        boolean scoreFlag = false;

        if(sendCouple.get().getUserId1()==userId || sendCouple.get().getUserId2()==userId){
            status = 1;
        } else if(receiveCouple.get().getUserId1()==userId || receiveCouple.get().getUserId2()==userId){
            if(letter.get().getRefLetterId()==0) {
                status = 0;
                Optional<Letter> checkLetterReply = letterRepository.findByRefLetterId(letterId);
                if (checkLetterReply.isPresent()){
                    replyFlag = true;
                }
            }
            else {
                status = 2;
                if(letter.get().getScore()!=-1){scoreFlag = true;}
            }
            letter.get().read();
        }
        return new PatchReadLetterRes(letterId,letter.get().getStampNum(),letter.get().getContent(),
                sendCouple.get().getMailboxName(),receiveCouple.get().getMailboxName(),letter.get().getCreateDate(),status,replyFlag,scoreFlag);
    }

    @Transactional
    public List<GetLetterRes> getSendLetterList(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        List<Letter> letterList = letterRepository.findBySendCoupleId(couple.get().getCoupleId());
        List<GetLetterRes> resList = new ArrayList<>();
        for(Letter letter : letterList){
            Optional<Couple> receiveCouple = Optional.of(coupleRepository.findById(letter.getReceiveCoupleId()).orElseThrow(
                    () -> new BaseException(NOT_EXIST_DATA_COUPLE)
            ));
            resList.add(new GetLetterRes(
                    letter.getLetterId(),
                    receiveCouple.get().getMailboxName(),
                    letter.getCreateDate(),
                    letter.isReadFlag()));
        }
        return resList;
    }
    public List<GetLetterRes> getReceiveLetterList(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        List<Letter> letterList = letterRepository.findByReceiveCoupleId(couple.get().getCoupleId());
        List<GetLetterRes> resList = new ArrayList<>();
        for(Letter letter : letterList){
            Optional<Couple> sendCouple = Optional.of(coupleRepository.findById(letter.getSendCoupleId()).orElseThrow(
                    () -> new BaseException(NOT_EXIST_DATA_COUPLE)
            ));
            resList.add(new GetLetterRes(
                    letter.getLetterId(),
                    sendCouple.get().getMailboxName(),
                    letter.getCreateDate(),
                    letter.isReadFlag()));
        }
        return resList;
    }

    @Transactional
    public Map<Integer, Integer> scoreLetter(int userId, int letterId, int score) throws BaseException {
        Optional<Letter> letter = Optional.of(letterRepository.findById(letterId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_LETTER)
        ));
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        if(letter.get().getReceiveCoupleId()!=couple.get().getCoupleId()){
            throw new BaseException(FAIL_TO_SCORE_INVALID_COUPLE);
        }
        if(letter.get().getScore()!=-1){
            throw new BaseException(FAIL_TO_SCORE_ALREADY);
        }
        letter.get().score(score);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0,letter.get().getReceiveCoupleId());
        map.put(1,letter.get().getSendCoupleId());
        return map;
    }

    @Transactional
    public int getNewLetterId(int userId) throws BaseException {
        Optional<Couple> couple = Optional.of(coupleRepository.findByUserId1OrUserId2(userId,userId).orElseThrow(
                () -> new BaseException(NOT_EXIST_DATA_COUPLE)
        ));
        List<Letter> receiveLetter = letterRepository.findByReceiveCoupleId(couple.get().getCoupleId());
        int lastIndex = receiveLetter.size()-1;
        if(lastIndex>=0 && !receiveLetter.get(lastIndex).isReadFlag()){
            return receiveLetter.get(lastIndex).getLetterId();
        }
        return 0;
    }

    @Transactional
    public int report(int userId, int letterId, PostReportReq postReportReq) throws BaseException {
        Report report = postReportReq.toEntity(userId,letterId);
        reportRepository.save(report);
        return report.getReportId();
    }

    @Transactional
    public void init(int coupleId) throws BaseException{
        String content = "안녕 만나서 반가워!\n" +
                "우리는 프롬유를 만들고, 운영하고 있는 프롬유팀이야. ♥︎\n" +
                "\n" +
                "먼저, 프롬유를 찾아줘서 정말 고마워!\n" +
                "앞으로 오랫동안 우리와 함께 하지 않겠어?\n" +
                "\n" +
                "우리는 너희 같이 커플들을 위한\n" +
                "사람 냄새가 나는 따뜻한 어플을 만들고 싶었어.\n" +
                "\n" +
                "그래서 가져온 서비스가 바로,\n" +
                "오프라인의 특성을 그대로 가져온 교환일기와\n" +
                "그리고 다른 커플과 편지를 주고 받을 수 있는 우편함이야!\n" +
                "\n" +
                "프롬유를 만들기까지 정말 오랜 시간이 걸렸어!\n" +
                "정말 하나부터 끝까지 정성을 안 들인 곳이 없을 정도라니까!\n" +
                "하지만 분명히 부족한 점이 많을 거야...\n" +
                "\n" +
                "프롬유도 너희의 사랑과 함께 성장해 나가고 싶어!\n" +
                "\n" +
                "그러니, 궁금한 거나 문의할 일이 있다면 언제든지 우리의 인스타그램을 찾아줘!\n" +
                "우리는 너희들의 이야기를 최대한 반영할 준비가 되어있어 :)\n" +
                "최대한 빠르게 답변할게!\n" +
                "(물론 개발자를 위한 사랑 고백도 환영해 ㅎㅎ...)\n" +
                "\n" +
                "인스타그램 아이디: _fromus2\n" +
                "\n" +
                "From. 프롬유 개발자들\n";
        date = new Date();
        FirstLetter firstLetter = new FirstLetter(content,0);
        Letter letter = firstLetter.toEntity(0,0,1,coupleId,date);
        letterRepository.save(letter);
    }
}
