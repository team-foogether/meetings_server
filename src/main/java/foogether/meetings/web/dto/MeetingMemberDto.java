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
    private Gender gender;

    // Repository에서 entity -> dto로 바꿔주는 작업
    public MeetingMemberDto(MeetingMember entity) {
        this.idx = entity.getIdx();
        this.ownerIdx = entity.getOwnerIdx();
//        this.meetingIdx = entity.getMeetingIdx();
        this.gender = entity.getGender();
    }

    // dto -> Entity
    public MeetingMember toEntity() {
        return MeetingMember.builder()
//                .idx(this.idx)
//                .meetingIdx(this.meetingIdx
                .gender(this.gender)
                .ownerIdx(this.ownerIdx)
                .build();
    }


}
