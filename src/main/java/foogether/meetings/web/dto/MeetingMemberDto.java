package foogether.meetings.web.dto;

import foogether.meetings.domain.Entity.MeetingMember;
import foogether.meetings.domain.Gender;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class MeetingMemberDto {
    private int idx;
    private int ownerIdx; // user_id
    private int meetingIdx;
    private Gender gender;

    // dto -> Entity
    public MeetingMember toEntity() {
        return MeetingMember.builder()
                .meetingIdx(this.meetingIdx)
                .gender(this.gender)
                .ownerIdx(this.ownerIdx)
                .build();
    }


}
