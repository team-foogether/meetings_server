package foogether.meetings.web.dto;

import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Entity.MeetingLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
public class MeetingLikeDto {
    private int idx;
    private int ownerIdx;
    private int meetingIdx;


    // Repository에서 entity -> dto로 바꿔주는 작업
    public MeetingLikeDto(MeetingLike entity) {
        this.idx = entity.getIdx();
        this.ownerIdx = getOwnerIdx();
        this.meetingIdx = getMeetingIdx();
    }

    // dto에서 Entity로 바꿔주는 작업
    // 먼저 Build로 선언할 것들 선언
    public MeetingLike toEntity() {
        return MeetingLike.builder()
                .meetingIdx(this.meetingIdx)
                .ownerIdx(this.ownerIdx)
                .build();
    }
}
