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
    /* 내가 작성한 게시글 조회 */
    DefaultResponse<List<MeetingDto>> myMeetingList(String header);

    /* 게시글 삭제 */
    DefaultResponse deleteMeeting(int meetingIdx, String header);

    /* 게시글 등록 및 수정 */
    DefaultResponse saveMeeting(
            MeetingDetailDto meetingDetailDto, String header) throws Exception;

    /* 특정 게시글 조회 */
    // 모집 완료 요청
    DefaultResponse<Integer> postComplete(int meetingIdx, String header);

    // 좋아요 요청 및 취소
//    DefaultResponse<Integer> postLikeState(MeetingLikeDto meetingLikeDto);
    DefaultResponse<Integer> postLikeState(int meetingIdx, String header);

    // 참여 요청 및 취소
//    DefaultResponse<Integer> postJoinState(MeetingMemberDto meetingMemberDto, String header);
    DefaultResponse<Integer> postJoinState(int meetingMemberDto, String header);

    // 참여자 수 조회
    int findMemberCount(int meetingIdx, Gender gender) throws Exception;

    // 특정 게시물 조회
    DefaultResponse<MeetingDetailDto> findByIdx(int meetingIdx, String header) throws Exception;

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
