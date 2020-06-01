package foogether.meetings.repository;

import com.sun.istack.Nullable;
import foogether.meetings.domain.Active;
import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.StatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    //    @Query("SELECT m FROM Meeting m ORDER BY m.id DESC")
    //    List<Meeting> findAllDesc();
    List<Meeting> findAllByActive(Active active);
    List<Meeting> findAllByActiveAndStatus(Active active, StatusInfo status);


    // 주소값 조회
    List<Meeting> findAllByActiveAndAddressAndStatus(Active active, Address address, StatusInfo status);
    List<Meeting> findAllByActiveAndAddress(Active active, Address address);
    // first, second, third 중에 값이 하나라도 안들어오면 검색이 안됨 -> 부분 검색 있는지 확인
    //    List<Meeting> findAllByfirstAddrAndsecondAddrAndthridAddr
    //    (String firstAddr, String secondAddr, String thridAddr);


    // keyword 조회
    List<Meeting> findAllByActiveAndStatusAndTitleContaining(Active active, StatusInfo status, String keyword);
    // Containing : %keyword%
    List<Meeting> findAllByActiveAndTitleContaining(Active active, String keyword);
//    // End : %keyword
//    List<Meeting> findAllByActiveAndTitleEndsWith(Active active, String keyword);



}
