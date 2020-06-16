package foogether.meetings.repository;

import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Entity.MeetingMember;
import foogether.meetings.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Integer> {
    List<MeetingMember> findAllByOwnerIdx(int ownerIdx);
    void deleteByIdx(int meetingMemberIdx);
    //참여자인지 조회
//    int countByMeetingIdxAndOwnerIdx(int meetingIdx, int ownerIdx);
    
    // 주석삭제
//    MeetingMember findByMeetingIdxAndOwnerIdx(int meetingIdx, int ownerIdx);
//
//    // 참여자 수 조회
//    int countAllByMeetingIdxAndGender(int meetingIdx, Gender gender);

    // 참여 요청 및 취소
//    int save(int meetingIdx, int ownerIdx);

}
