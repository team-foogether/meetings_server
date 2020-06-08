package foogether.meetings.web.dto;

import foogether.meetings.domain.*;
import foogether.meetings.domain.Entity.Meeting;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class MeetingDto {
    // TODO: MeetingDetail Dto에 따라 변수 타입 바꾸기
    // meetingIdx를 공유
    private int idx;

    // 작성자 정보
    private int ownerIdx; // user_id

    // 한개의 Images // TODO: multipart
    private String imgUrl;

    // DateInfo
//    private DateInfo endDate;
    private LocalDate endDate;
    private LocalTime endTime;
    private String endDayOfWeek;

    private String title;
//    private String content;
//    private Address address;
    private String firstAddr;
    private String secondAddr;
    private String thirdAddr;

    private StatusInfo status;
    private int manMax;
    private int femMax;
    private int manNum;
    private int femNum;
    // 활동중인 회원이 작성한 글인지 확인
    private Active active;



    // Repository에서 entity -> dto로 바꿔주는 작업
    public MeetingDto(Meeting entity) {
        this.idx = entity.getIdx();
        this.ownerIdx = entity.getOwnerIdx();
        this.endDate = entity.getEndDate().getMeeting_endDate();
        this.endTime = entity.getEndDate().getMeeting_endTime();
        this.endDayOfWeek = entity.getEndDate().getMeeting_endDayOfWeek();
        this.title = entity.getTitle();
//        this.content = entity.getContent();
//        this.address = entity.getAddress();
        this.firstAddr = entity.getAddress().getFirstAddr();
        this.secondAddr = entity.getAddress().getSecondAddr();
        this.thirdAddr = entity.getAddress().getThirdAddr();
        this.status = entity.getStatus();
        this.manMax = entity.getManMax();
        this.femMax = entity.getFemMax();
        this.active = entity.getActive();
        this.imgUrl = entity.getImgUrl();
    }


    // dto에서 Entity로 바꿔주는 작업
    // 먼저 Build로 선언할 것들 선언
    // Meeting Entity 지정
    public Meeting toEntity() {
        return Meeting.builder()
                .idx(this.idx)
                .active(this.active)
//                .content(this.content)
                .endDate(new DateInfo(this.endDate, this.endTime, this.endDayOfWeek))
                .femMax(this.femMax)
                .manMax(this.manMax)
                .status(this.status)
                .title(this.title)
                .imgUrl(this.imgUrl)
                .address(new Address(this.firstAddr, this.secondAddr, this.thirdAddr))
                .build();
    }
}
