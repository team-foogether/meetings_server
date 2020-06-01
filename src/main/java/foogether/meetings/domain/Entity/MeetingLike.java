package foogether.meetings.domain.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private int owner_idx;

    // LAZY : 실행순간에 갖고옴
    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "meeting_idx")
    private Meeting meetingIdx;


}
