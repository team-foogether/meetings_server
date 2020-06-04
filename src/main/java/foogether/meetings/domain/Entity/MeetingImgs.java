package foogether.meetings.domain.Entity;

import foogether.meetings.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Table(name="meetingImgs")
@NoArgsConstructor
@Getter
public class MeetingImgs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meetingImgs_idx")
    int idx;

    @Column(name = "meeting_idx")
    int meetingIdx;

    @Column(name = "imgUrl")
    String imgUrl;

    @Builder
    public MeetingImgs(int idx, int meetingIdx, String imgUrl) {
        this.idx = idx;
        this.imgUrl = imgUrl;
        this.meetingIdx = meetingIdx;
    }
}
