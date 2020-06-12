package foogether.meetings.web.dto;

import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Entity.MeetingMember;
import foogether.meetings.domain.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
public class MeetingMemberDto {
    private int idx;
    private int ownerIdx; // user_id
    private int meetingIdx;
//    private Gender gender;
    // 닉네임
    String nickname;
    // 프로필 이미지
    String profileImg;
    // 성별
    Gender gender;


    public MeetingMemberDto(UserResponseDto data) {
        this.ownerIdx = data.getIdx();
//        this.emailAddr = data.getEmailAddr();
//        this.name = data.getName();
//        this.phoneNum = data.getPhoneNum();
        this.nickname = data.getNickname();
        this.profileImg = data.getProfileImg();
        this.gender = data.getGender();
    }

    // Repository에서 entity -> dto로 바꿔주는 작업
    public MeetingMemberDto(MeetingMember entity) {
        this.idx = entity.getIdx();
//        this.meetingIdx = entity.getMeetingIdx();
        this.ownerIdx = entity.getOwnerIdx();
//        this.gender = entity.getGender();
    }

    // dto -> Entity
    public MeetingMember toEntity() {
        return MeetingMember.builder()
                .idx(this.idx)
                .meetingIdx(this.meetingIdx)
//                .gender(this.gender)
                .ownerIdx(this.ownerIdx)
                .build();
    }


}
