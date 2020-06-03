package foogether.meetings.repository;

import foogether.meetings.domain.Entity.MeetingMember;
import foogether.meetings.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Integer> {
    int countAllByMeetingIdxAndGender(int meeting, Gender gender);
}
