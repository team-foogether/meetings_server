package foogether.meetings.repository;

import foogether.meetings.domain.Entity.MeetingLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingLikeRepository extends JpaRepository<MeetingLike, Integer> {
    MeetingLike findByMeetingIdxAndOwnerIdx(int meetingIdx, int ownerIdx);
}
