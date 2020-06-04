package foogether.meetings.web.dto;

import foogether.meetings.domain.*;
import foogether.meetings.domain.Entity.Meeting;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.security.acl.Owner;
import java.time.LocalDateTime;
import java.util.List;

@Getter
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
        private List<MeetingImgsDto> imgUrlList;

        // DateInfo
        private DateInfo endDate;
        private String title;
        private String content;
        private Address address;

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


    public void setOwnerProfileImg(String ownerProfileImg) {
        this.ownerProfileImg = ownerProfileImg;
    }

    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public void setOwnerGender(Gender ownerGender) {
        this.ownerGender = ownerGender;
    }

    public void setStatus(StatusInfo status) {
        this.status = status;
    }

    public void setAuth(boolean auth) { this.auth = auth; }
    public void setLike(boolean like) { this.isLike = like; }

    public void setManNum(int manNum) {
        this.manNum = manNum;
    }

    public void setFemNum(int femNum) {
        this.femNum = femNum;
    }

    public void setJoin(boolean isJoin) {
        this.isJoin = isJoin;
    }

    public void setImgUrlList(List<MeetingImgsDto> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    // Repository에서 entity -> dto로 바꿔주는 작업
    public MeetingDetailDto(Meeting entity) {
        this.idx = entity.getIdx();
        this.ownerIdx = entity.getOwnerIdx();
//        this.imgUrl = entity.getImgUrl();
        this.endDate = entity.getEndDate();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.address = entity.getAddress();
        this.status = entity.getStatus();
        this.manMax = entity.getManMax();
        this.femMax = entity.getFemMax();
        this.active = entity.getActive();
    }


        // dto에서 Entity로 바꿔주는 작업
        // 먼저 Build로 선언할 것들 선언
        // Meeting Entity 지정
        public Meeting toEntity() {
            return Meeting.builder()
                    .idx(this.idx)
                    .active(this.active)
                    .content(this.content)
                    .endDate(this.endDate)
                    .femMax(this.femMax)
                    .manMax(this.manMax)
//                    .imgUrl(this.imgUrl)
                    .status(this.status)
                    .title(this.title)
                    .ownerIdx(this.ownerIdx)
                    .address(this.address)
                    .build();
        }
}
