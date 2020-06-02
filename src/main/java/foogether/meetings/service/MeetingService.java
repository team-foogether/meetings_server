package foogether.meetings.service;

import foogether.meetings.domain.Active;
import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.MeetingDto;
import foogether.meetings.web.dto.OwnerDto;

import java.util.List;

public interface MeetingService {

    /* 특정 게시글 조회 */
    // 특정 게시물 조회
    DefaultResponse<MeetingDto> findByIdx(int meetingIdx, OwnerDto ownerDto) throws Exception;

    /* 전체 게시글 조회 */
    // Entity List타입으로 먼저 반환받음
    // 전체 조회
    DefaultResponse<List<MeetingDto>> findAll(String sort) throws Exception;

    // 위치 정보 조회
    DefaultResponse<List<MeetingDto>> findAllByAddress(String sort, Address address) throws Exception;

    // 키워드로 조회
    DefaultResponse<List<MeetingDto>> findAllByKeyword(String sort, String keyword) throws Exception;

}
