package foogether.meetings.service;

import com.sun.org.apache.xpath.internal.operations.Mult;
import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Gender;
import foogether.meetings.web.dto.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface MeetingService {

    /* 게시글 수정 */
    DefaultResponse modify(MeetingDto meetingDto) throws Exception;
    /* 게시글 등록 */
//    DefaultResponse save(
//            MultipartHttpServletRequest request) throws Exception;
    DefaultResponse save(
            MeetingDetailDto meetingDetailDto) throws Exception;

    /* 특정 게시글 조회 */
    // 모집 완료 요청
    DefaultResponse<Integer> postComplete(int meetingIdx, OwnerDto ownerDto);

    // 좋아요 요청 및 취소
    DefaultResponse<Integer> postLikeState(MeetingLikeDto meetingLikeDto);

    // 참여 요청 및 취소
    DefaultResponse<Integer> postJoinState(MeetingMemberDto meetingMemberDto);

    // 참여자 수 조회
    int findMemberCount(int meetingIdx, Gender gender) throws Exception;

    // 특정 게시물 조회
    DefaultResponse<MeetingDetailDto> findByIdx(int meetingIdx, OwnerDto ownerDto) throws Exception;

    /* 전체 게시글 조회 */
    // Img 조회
//    List<MeetingImgsDto> findImgsByMeetingIdx(Meeting meeting, int meetingIdx);
    // Entity List타입으로 먼저 반환받음
    // 전체 조회
    DefaultResponse<List<MeetingDto>> findAll(String sort) throws Exception;

    // 위치 정보 조회
    DefaultResponse<List<MeetingDto>> findAllByAddress(String sort, Address address) throws Exception;

    // 키워드로 조회
    DefaultResponse<List<MeetingDto>> findAllByKeyword(String sort, String keyword) throws Exception;

}
