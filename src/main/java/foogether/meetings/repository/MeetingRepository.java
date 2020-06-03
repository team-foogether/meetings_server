package foogether.meetings.repository;

import com.sun.istack.Nullable;
import foogether.meetings.domain.Active;
import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.StatusInfo;
import jdk.net.SocketFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

    /* 상세 조회 */
    // 특정 게시물의 삭제여부 조회
    Meeting findByIdx(int meetingIdx);
    
    // 특정 게시물 조회
    Meeting findByIdxAndActive(int meetingIdx, Active active);

    /* 기본 전체 조회 */
    List<Meeting> findAllByActive(Active active);
    List<Meeting> findAllByActiveAndStatus(Active active, StatusInfo status);


    /* 주소값 조회 */
    // 첫번째 주소만 status 구별
    @Query("SELECT m FROM Meeting m WHERE first_addr= :firstAddr and meeting_active = :active and meeting_status = :status")
    List<Meeting> findAllByFirstAddrWithStatus(@Param("active") String active, @Param("firstAddr") String firstAddr, @Param("status") String status);
    // 첫번째 주소만 status = all
    @Query("SELECT m FROM Meeting m WHERE first_addr= :firstAddr and meeting_active = :active")
    List<Meeting> findAllByFirstAddrWithoutStatus(@Param("active") String active, @Param("firstAddr") String firstAddr);

    // 두번째 주소 status 구별
    @Query("SELECT m FROM Meeting m WHERE first_addr= :firstAddr and second_addr= :secondAddr and meeting_active = :active " +
            "and meeting_status = :status")
    List<Meeting> findAllByFirstAndSecondAddrWithStatus(@Param("active") String active,
                                              @Param("firstAddr") String firstAddr, @Param("secondAddr") String secondAddr,
                                              @Param("status") String status);
    // 두번째 주소까지 status = all
    @Query("SELECT m FROM Meeting m WHERE first_addr= :firstAddr and second_addr= :secondAddr and meeting_active = :active")
    List<Meeting> findAllByFirstAndSecondAddrWithoutStatus(@Param("active") String active,
                                                        @Param("firstAddr") String firstAddr, @Param("secondAddr") String secondAddr);

    // 전체 주소가 다 있는 경우
    List<Meeting> findAllByActiveAndAddressAndStatus(Active active, Address address, StatusInfo status);
    // status = all
    List<Meeting> findAllByActiveAndAddress(Active active, Address address);


    /* keyword 조회 */
    // keyword 조회
    List<Meeting> findAllByActiveAndStatusAndTitleContaining(Active active, StatusInfo status, String keyword);
    // Containing : %keyword%
    List<Meeting> findAllByActiveAndTitleContaining(Active active, String keyword);
//    // End : %keyword
//    List<Meeting> findAllByActiveAndTitleEndsWith(Active active, String keyword);




}
