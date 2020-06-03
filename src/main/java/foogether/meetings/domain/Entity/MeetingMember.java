package foogether.meetings.domain.Entity;

import foogether.meetings.domain.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="meetingMember")
@NoArgsConstructor
@Getter
public class MeetingMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meetingMember_idx")
    private int idx;

    // 모임 참여자
    @Column(name="owner_idx")
    private int ownerIdx; // user_id

    @Column(name="meeting_idx")
    private int meetingIdx;

    @Enumerated(EnumType.STRING)
    @Column(name="owner_gender")
    private Gender gender;

    @Builder
    public MeetingMember(int ownerIdx, int meetingIdx, Gender gender) {
        this.ownerIdx = ownerIdx;
        this.meetingIdx = meetingIdx;
        this.gender = gender;
    }
}
