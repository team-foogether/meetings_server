package foogether.meetings.web.dto;

import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Entity.MeetingImgs;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class MeetingImgsDto {
    int idx;
    int meetingIdx;
    String imgUrl;

    // Entity -> dto
    public MeetingImgsDto(MeetingImgs entity) {
        this.idx = entity.getIdx();
        this.meetingIdx = entity.getMeetingIdx();
        this.imgUrl = entity.getImgUrl();
    }

    // dto -> entity
    public MeetingImgs toEntity() {
        return MeetingImgs.builder()
                .idx(this.idx)
                .imgUrl(this.imgUrl)
                .meetingIdx(this.getMeetingIdx())
                .build();
    }

}
