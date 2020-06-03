package foogether.meetings.service;

import foogether.meetings.domain.Active;
import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Entity.MeetingLike;
import foogether.meetings.domain.Entity.MeetingMember;
import foogether.meetings.domain.Gender;
import foogether.meetings.domain.StatusInfo;
import foogether.meetings.repository.MeetingLikeRepository;
import foogether.meetings.repository.MeetingMemberRepository;
import foogether.meetings.repository.MeetingRepository;
import foogether.meetings.utils.ResponseMessage;
import foogether.meetings.web.dto.*;
import jdk.net.SocketFlow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.ACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor    // final차입 repository 의존성 주입
@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private final MeetingRepository meetingRepository;
    @Autowired
    private final MeetingLikeRepository meetingLikeRepository;
    @Autowired
    private final MeetingMemberRepository meetingMemberRepository;


    /* User정보 조회 */



    /* 모이자 상세 페이지 - 진행중 */
    // 모집 완료 요청
    @Transactional
    @Override
    public DefaultResponse<Integer> postComplete(int meetingIdx, OwnerDto ownerDto) {
        Meeting meeting = meetingRepository.findByIdx(meetingIdx);
        if(meeting == null || meeting.getActive().equals(Active.UNACTIVE)){ // UNACTIVE인 경우 반환
            return DefaultResponse.res("fail",
                    ResponseMessage.READ_ALL_BUT_ZERO);
        }

        MeetingDto meetingDto = new MeetingDto(meeting);
        meetingDto.setStatus(StatusInfo.COMPLETE);
        meetingRepository.save(meetingDto.toEntity());

        // meetingIdx 반환
        return DefaultResponse.res("success",
                ResponseMessage.MEETING_COMPLETE,
                meetingDto.getIdx());
    }

    // 좋아요 요청 및 취소
    @Transactional
    @Override
    public DefaultResponse<Integer> postLikeState(MeetingLikeDto meetingLikeDto) {
        // meeting Idx 가 ACTIVE인지 확인
        Meeting meeting = meetingRepository.findByIdx(meetingLikeDto.getMeetingIdx());
        if(meeting == null || meeting.getActive().equals(Active.UNACTIVE)){ // UNACTIVE인 경우 반환
            return DefaultResponse.res("fail",
                    ResponseMessage.READ_ALL_BUT_ZERO);
        }


        MeetingLike meetingLike = meetingLikeRepository.findByMeetingIdxAndOwnerIdx(
                meetingLikeDto.getMeetingIdx(), meetingLikeDto.getOwnerIdx()
        );
        if(meetingLike == null){
            meetingLikeRepository.save(meetingLikeDto.toEntity());
            // meetingIdx 반환
            return DefaultResponse.res("success",
                    ResponseMessage.LIKE_CONTENT,
                    meetingLikeDto.getMeetingIdx());
        } else {
            meetingLikeRepository.delete(meetingLike);
            // meetingIdx 반환
            return DefaultResponse.res("success",
                    ResponseMessage.UNLIKE_CONTENT,
                    meetingLikeDto.getMeetingIdx());
        }
    }

    // 참여 요청 및 취소
    @Override
    @Transactional
    public DefaultResponse<Integer> postJoinState(MeetingMemberDto meetingMemberDto) {
        // meeting Idx 가 ACTIVE인지 확인
        Meeting meeting = meetingRepository.findByIdx(meetingMemberDto.getMeetingIdx());
        if(meeting == null || meeting.getActive().equals(Active.UNACTIVE)){ // UNACTIVE인 경우 반환
            return DefaultResponse.res("fail",
                    ResponseMessage.READ_ALL_BUT_ZERO);
        }


        MeetingMember meetingMember = meetingMemberRepository.findByMeetingIdxAndOwnerIdx(
                meetingMemberDto.getMeetingIdx(), meetingMemberDto.getOwnerIdx()
        );
        if(meetingMember == null){
            meetingMemberRepository.save(meetingMemberDto.toEntity());
            // meetingIdx 반환
            return DefaultResponse.res("success",
                    ResponseMessage.JOIN_MEETING,
                    meetingMemberDto.getMeetingIdx());
        } else {
            meetingMemberRepository.delete(meetingMember);
            // meetingIdx 반환
            return DefaultResponse.res("success",
                    ResponseMessage.OUT_MEETING,
                    meetingMemberDto.getMeetingIdx());
        }


    }

    // 참여자 수 조회
    @Override
    public int findMemberCount(int meetingIdx, Gender gender) {
            return meetingMemberRepository.countAllByMeetingIdxAndGender(meetingIdx, gender);
    }

    // 특정 게시물 조회
    @Override
    public DefaultResponse<MeetingDto> findByIdx(int meetingIdx, OwnerDto ownerDto) throws Exception {
        try {
            Meeting meeting = meetingRepository.findByIdxAndActive(meetingIdx, Active.ACTIVE);
            // meeting 정보가 없으면
            if (meeting == null) {
                return DefaultResponse.res("fail", ResponseMessage.NOT_FOUND_LIST);
            }

            MeetingDto meetingDto = new MeetingDto(meeting);

            log.info("meetingIdx >>> " + meetingDto.getIdx());
            log.info("ownerIdx of meetingIdx >>> " + meetingDto.getOwnerIdx());
            log.info("ownerIdx >>> " + ownerDto.getOwnerIdx());

            // owner인지 확인
            if(ownerDto.getOwnerIdx() == meetingDto.getOwnerIdx()){
                meetingDto.setAuth(true);
            }

            // like했는지 확인
            MeetingLike meetingLike = meetingLikeRepository.findByMeetingIdxAndOwnerIdx(meetingIdx, ownerDto.getOwnerIdx());

            if(meetingLike != null){
                meetingDto.setLike(true);
            }

            //참여자 수
            meetingDto.setFemNum(findMemberCount(meetingDto.getIdx(),Gender.F));
            meetingDto.setManNum(findMemberCount(meetingDto.getIdx(), Gender.M));

            // 개최자 정보 저장
            meetingDto.setOwnerNickname(ownerDto.getOwnerNickname());
            meetingDto.setOwnerProfileImg(ownerDto.getOwnerProfileImg());
            meetingDto.setOwnerGender(ownerDto.getOwnerGener());


            return DefaultResponse.res("success", ResponseMessage.READ_CONTENT,
                    meetingDto);
        } catch (Exception e) {
            return DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /* 모이자 메인 페이지 - 완료 */
    // 게시글 전체 조회(ACTIVE 인것만)
    @Override
    public DefaultResponse<List<MeetingDto>> findAll(String sort) throws Exception {
        List<Meeting> meetingList;

        if(sort.equals("recruiting")) {
            // meetingList 중 객체 하나를 MeetingDto의 entity->dto 하는 생성자함수로 만들어서 List로 만들어라
            meetingList = meetingRepository.findAllByActiveAndStatus(Active.ACTIVE, StatusInfo.RECRUITING);
        }
        else if(sort.equals("complete")) {
            // meetingList 중 객체 하나를 MeetingDto의 entity->dto 하는 생성자함수로 만들어서 List로 만들어라
            meetingList = meetingRepository.findAllByActiveAndStatus(Active.ACTIVE, StatusInfo.COMPLETE);
        }
        else if(sort.equals("fail")){
            meetingList = meetingRepository.findAllByActiveAndStatus(Active.ACTIVE, StatusInfo.FAIL);
        }
        else if(sort.equals("all")){
            // meetingList 중 객체 하나를 MeetingDto의 entity->dto 하는 생성자함수로 만들어서 List로 만들어라
            meetingList = meetingRepository.findAllByActive(Active.ACTIVE);
        } else {
            return DefaultResponse.res("fail", ResponseMessage.WRONG_STATUS);
        }

        int numMeeting = meetingList.size();
        if(numMeeting == 0){
            return DefaultResponse.res("success", ResponseMessage.READ_ALL_BUT_ZERO);
        }



        return DefaultResponse.res("success", numMeeting, ResponseMessage.READ_ALL_CONTENTS,
                meetingList.stream().map(meetings -> {
                    MeetingDto meetingDto = new MeetingDto(meetings);
                    meetingDto.setFemNum(findMemberCount(meetingDto.getIdx(), Gender.F));
                    meetingDto.setManNum(findMemberCount(meetingDto.getIdx(), Gender.M));
                    return meetingDto;
                })
                .collect(Collectors.toList()));


    }

    // 주소로 검색
    // TODO: first, second 두개만 들어와도 동작할 수 있도록 하기
    @Override
    public DefaultResponse<List<MeetingDto>> findAllByAddress(String sort, Address address) throws Exception {
        List<Meeting> meetingList;

//        Example<Address> example;
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withIncludeNullValues();
//        Example<Address> example = Example.of(address, matcher);


            if(!address.getThirdAddr().equals("")){ // 주소가 다 들어 있음
                if(sort.equals("recruiting")){
                    log.info("recruiting alladdress >>> " + address.getFirstAddr() + "," + address.getSecondAddr()
                            + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.RECRUITING);
                } else if (sort.equals("complete")){
                    log.info("complete alladdress >>> " + address.getFirstAddr() + "," + address.getSecondAddr()
                            + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.COMPLETE);
                }
                else if(sort.equals("fail")){
                    meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.FAIL);
                }
                else if(sort.equals("all")){
                    log.info("all alladdress >>> " + address.getFirstAddr() + "," + address.getSecondAddr()
                            + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByActiveAndAddress(Active.ACTIVE, address);
                }
                else {
                    return DefaultResponse.res("fail", ResponseMessage.WRONG_STATUS);
                }
            } else if (!address.getSecondAddr().equals("")){ // 첫번째, 두번째 주소만 들어 있음
                if(sort.equals("recruiting")){
                    log.info("recruiting first,second >>> " + address.getFirstAddr() + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr(), "RECRUITING");
                } else if (sort.equals("complete")){
                    log.info("complete first,second >>> " + address.getFirstAddr() + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr(), "COMPLETE");
                } else if(sort.equals("fail")) {
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr(), "FAIL");
                }
                else if(sort.equals("all")){
                    log.info("all first,second >>> " + address.getFirstAddr() + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithoutStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr());
                } else {
                    return DefaultResponse.res("fail", ResponseMessage.WRONG_STATUS);
                }
            } else {    // 첫번째 주소만 들어있음
                if(sort.equals("recruiting")) {
                    log.info("recruiting firstAddr >>> " + address.getFirstAddr());
                    meetingList = meetingRepository.findAllByFirstAddrWithStatus("ACTIVE", address.getFirstAddr(), "RECRUITING");
                } else if(sort.equals("complete")){
                    log.info("complete firstAddr >>> " + address.getFirstAddr());
                    meetingList = meetingRepository.findAllByFirstAddrWithStatus("ACTIVE", address.getFirstAddr(), "COMPLETE");
                } else if(sort.equals("fail")) {
                    meetingList = meetingRepository.findAllByFirstAddrWithStatus("ACTIVE", address.getFirstAddr(), "FAIL");
                }
                else if(sort.equals("all")){
                    log.info("all firstAddr >>> " + address.getFirstAddr());
                    meetingList = meetingRepository.findAllByFirstAddrWithoutStatus("ACTIVE", address.getFirstAddr());
                } else {
                    return DefaultResponse.res("fail", ResponseMessage.NOT_FOUND_LIST);
                }
            }


        int numMeeting = meetingList.size();
        if(numMeeting == 0){
            return DefaultResponse.res("success", ResponseMessage.READ_ALL_BUT_ZERO);
        }

        return DefaultResponse.res("success", numMeeting, ResponseMessage.READ_ALL_CONTENTS,
                meetingList.stream().map(meetings -> {
                    MeetingDto meetingDto = new MeetingDto(meetings);
                    meetingDto.setFemNum(findMemberCount(meetingDto.getIdx(), Gender.F));
                    meetingDto.setManNum(findMemberCount(meetingDto.getIdx(), Gender.M));
                    return meetingDto;
                }).collect(Collectors.toList()));
    }

    // 제목 keyword로 검색
    @Override
    public DefaultResponse<List<MeetingDto>> findAllByKeyword(String sort, String keyword) throws Exception {
        List<Meeting> meetingList;
        log.info("sort >>> " + sort);
        if(sort.equals("recruiting")) {
            log.info("recruiting >>> " + keyword);

            meetingList = meetingRepository.findAllByActiveAndStatusAndTitleContaining(Active.ACTIVE, StatusInfo.RECRUITING, keyword);
        }
        else if(sort.equals("complete")){
            meetingList = meetingRepository.findAllByActiveAndStatusAndTitleContaining(Active.ACTIVE, StatusInfo.COMPLETE, keyword);
        }
        else if(sort.equals("all")) {
            meetingList = meetingRepository.findAllByActiveAndTitleContaining(Active.ACTIVE, keyword);
        }
        else {
            return DefaultResponse.res("fail", ResponseMessage.WRONG_STATUS);
        }
        int numMeeting = meetingList.size();

        if(meetingList.size() == 0){
            return DefaultResponse.res("success", ResponseMessage.READ_ALL_BUT_ZERO);
        }

        return DefaultResponse.res("success", numMeeting, ResponseMessage.READ_ALL_CONTENTS,
                meetingList.stream().map(meetings -> {
                    MeetingDto meetingDto = new MeetingDto(meetings);
                    meetingDto.setFemNum(findMemberCount(meetingDto.getIdx(), Gender.F));
                    meetingDto.setManNum(findMemberCount(meetingDto.getIdx(), Gender.M));
                    return meetingDto;
                }).collect(Collectors.toList()));
    }


}
