package foogether.meetings.domain.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name="meetingLike")
@NoArgsConstructor
@Getter
public class MeetingLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meetingLike_idx")
    private int idx;

    @Column(name="owner_idx")
    private int ownerIdx;

    // LAZY : 실행순간에 갖고옴
    @Column(name = "meeting_idx")
    private int meetingIdx;

    // 좋아요 생성시간
    @Column(name="meetingLike_createdDate")
    @CreatedDate
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Builder
    public MeetingLike(int ownerIdx, int meetingIdx) {
        this.ownerIdx = ownerIdx;
        this.meetingIdx = meetingIdx;
    }
}
