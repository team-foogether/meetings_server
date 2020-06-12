package foogether.meetings.web.dto;

import foogether.meetings.domain.*;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Entity.MeetingMember;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MeetingDetailDto {
        private int idx;
        // 작성자 정보
        private int ownerIdx; // user_id
        private String ownerProfileImg;
        private String ownerNickname;
        private Gender ownerGender;

        // 전체 보기에서 나오는 하나의 Image
        // MeetingImgs 에서는 여러개의 Image
//        private String imgUrl;
        private String imgUrl;

        // DateInfo
//        private DateInfo endDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        @DateTimeFormat(pattern = "kk:mm:ss")
        private LocalTime endTime;
        private String endDayOfWeek;

        private String title;
        private String content;

//        private Address address;
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

        // entity -> dto 한 후 Controller나 Service에서 작업
        // 권한(작성자인지)
        private boolean auth;
        // 좋아요한 작품인지
        private boolean isLike;
        // 참석 중인지
        private boolean isJoin;

        // meetingList
        private List<MeetingMemberDto> meetingMemberList;

    // Repository에서 entity -> dto로 바꿔주는 작업
    public MeetingDetailDto(Meeting entity) {
            this.idx = entity.getIdx();
            this.ownerIdx = entity.getOwnerIdx();
//            this.meetingMemberDtoList = entity.getMemberList().stream().map(
//                    MeetingMemberDto::new
//            ).collect(Collectors.toList());
            this.endDate = entity.getEndDate().getMeeting_endDate();
            this.endTime = entity.getEndDate().getMeeting_endTime();
            this.endDayOfWeek = entity.getEndDate().getMeeting_endDayOfWeek();
            this.firstAddr = entity.getAddress().getFirstAddr();
            this.secondAddr = entity.getAddress().getSecondAddr();
            this.thirdAddr = entity.getAddress().getThirdAddr();
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.imgUrl = entity.getImgUrl();
            this.status = entity.getStatus();
            this.manMax = entity.getManMax();
            this.femMax = entity.getFemMax();
            this.active = entity.getActive();
//        } else {
//            this.idx = entity.getIdx();
//            this.ownerIdx = entity.getOwnerIdx();
//            this.endDate = entity.getEndDate().getMeeting_endDate();
//            this.endTime = entity.getEndDate().getMeeting_endTime();
//            this.endDayOfWeek = entity.getEndDate().getMeeting_endDayOfWeek();
//            this.firstAddr = entity.getAddress().getFirstAddr();
//            this.secondAddr = entity.getAddress().getSecondAddr();
//            this.thirdAddr = entity.getAddress().getThirdAddr();
//            this.title = entity.getTitle();
//            this.content = entity.getContent();
//            this.imgUrl = entity.getImgUrl();
//            this.status = entity.getStatus();
//            this.manMax = entity.getManMax();
//            this.femMax = entity.getFemMax();
//            this.active = entity.getActive();
//        }

    }


        // dto에서 Entity로 바꿔주는 작업
        // 먼저 Build로 선언할 것들 선언
        // Meeting Entity 지정
        public Meeting toEntity() {
            if(meetingMemberList != null){
                return Meeting.builder()
                        .idx(this.idx)
                        .active(this.active)
                        .content(this.content)
                        .endDate(new DateInfo(this.endDate, this.endTime, this.endDayOfWeek))
                        .femMax(this.femMax)
                        .manMax(this.manMax)
                        .imgUrl(this.imgUrl)
//                        .fileInfoList(this.imgUrl.stream().map(imgsDto -> new MeetingImgs(this.idx, imgsDto.getImgUrl())).collect(Collectors.toList()))
                        .status(this.status)
                        .title(this.title)
                        .ownerIdx(this.ownerIdx)
                        .address(new Address(this.firstAddr, this.secondAddr, this.thirdAddr))
                        .meetingMemberList(
                                this.meetingMemberList.stream().map(
//                                        meetingMember -> new MeetingMember(meetingMember.getOwnerIdx(), meetingMember.getGender(), meetingMember.getMeetingIdx())
                                        meetingMember -> new MeetingMember(meetingMember.getOwnerIdx(), meetingMember.getMeetingIdx())
                                ).collect(Collectors.toList()))
//                    .address(this.address)
                        .build();
            }
            else {
                return Meeting.builder()
                        .idx(this.idx)
                        .active(this.active)
                        .content(this.content)
                        .endDate(new DateInfo(this.endDate, this.endTime, this.endDayOfWeek))
                        .femMax(this.femMax)
                        .manMax(this.manMax)
                        .imgUrl(this.imgUrl)
//                        .fileInfoList(this.imgUrl.stream().map(imgsDto -> new MeetingImgs(idx, imgsDto.getImgUrl())).collect(Collectors.toList()))
                        .status(this.status)
                        .title(this.title)
                        .ownerIdx(this.ownerIdx)
                        .address(new Address(this.firstAddr, this.secondAddr, this.thirdAddr))
//                    .address(this.address)
                        .build();
            }

        }


}
