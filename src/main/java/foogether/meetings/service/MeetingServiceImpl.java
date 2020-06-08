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
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
//    @Autowired
//    private final MeetingImgsRepository meetingImgsRepository;
//    @Autowired
//    FileUtils fileUtils;


    /* User정보 조회 */

    /* 게시글 수정 */
    @Transactional
    @Override
    public DefaultResponse modify(MeetingDto meetingDto) throws Exception {
        return null;
    }

    /* 게시글 등록 */
    @Transactional
    @Override
    public DefaultResponse save(
            MeetingDetailDto meetingDetailDto) throws Exception {
        // meetingDetailDto의 meetingIdx가지고 Img저장
//        // 이미지 파일 처리
//        // board.setFileInfoList(fileInfoList);
//        List<MeetingImgsDto> files = fileUtils.parseFileInfo(meetingDetailDto.getIdx()
//                , imgUrlList);
//        if (!CollectionUtils.isEmpty(files)) {
//            meetingDetailDto.setImgUrlList(files);
//        }

        // 그저 save...
        meetingRepository.save(meetingDetailDto.toEntity());
        if(meetingDetailDto.getIdx() == 0) {
            return DefaultResponse.res("success", ResponseMessage.CREATE_CONTENT);
        } else {
            return DefaultResponse.res("success", ResponseMessage.UPDATE_CONTENT);
        }
    }


    /* 모이자 상세 페이지 - 완료, Auth부분 추가 */
    // 모집 완료 요청
    @Transactional
    @Override
    public DefaultResponse<Integer> postComplete(int meetingIdx, OwnerDto ownerDto) {
        Meeting meeting = meetingRepository.findByIdx(meetingIdx);
        if(meeting == null || meeting.getActive().equals(Active.UNACTIVE)){ // UNACTIVE인 경우 반환
            return DefaultResponse.res("fail",
                    ResponseMessage.READ_ALL_BUT_ZERO);
        }

        MeetingDetailDto meetingDetailDto = new MeetingDetailDto(meeting);
        meetingDetailDto.setStatus(StatusInfo.COMPLETE);
        meetingRepository.save(meetingDetailDto.toEntity());

        // meetingIdx 반환
        return DefaultResponse.res("success",
                ResponseMessage.MEETING_COMPLETE,
                meetingDetailDto.getIdx());
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

        List<MeetingMember> meetingMembers = meeting.getMemberList();
        // MemberList 중 ownerIdx가 맞는게 있는지 찾아라
        for(MeetingMember meetingMember : meetingMembers){
            if(meetingMember.getOwnerIdx() == meetingMemberDto.getOwnerIdx()){
                log.info(" meetingMember Idx >>> " + meetingMember.getIdx());
                log.info(" meetingMember Gender >>> " + meetingMember.getGender());
                log.info(" meetingMember OwnerId >>> " + meetingMember.getOwnerIdx());
                meetingMembers.remove(meetingMember);
                log.info(" meetingMember OwnerId >>> " + meetingMember.getOwnerIdx());
                // member 추가
                List<MeetingMemberDto> meetingMemberList = meetingMembers.stream().map(
                        meetingMember1 -> new MeetingMemberDto(meetingMember1)
                ).collect(Collectors.toList());
                MeetingDetailDto meetingDetailDto = new MeetingDetailDto(meeting);
                meetingDetailDto.setMeetingMemberDtoList(meetingMemberList);

//                meetingRepository.save(meetingDetailDto.toEntity());
                meetingMemberRepository.deleteByIdx(meetingMember.getIdx());

                // meetingIdx 반환
                return DefaultResponse.res("success",
                        ResponseMessage.OUT_MEETING,
                        meetingMemberDto.getMeetingIdx());
            }
        }


//            meetingMemberRepository.delete(meetingMember);

        // member 추가
        List<MeetingMemberDto> meetingMemberList = meetingMembers.stream().map(
                MeetingMemberDto::new
        ).collect(Collectors.toList());
        meetingMemberList.add(meetingMemberDto);

        MeetingDetailDto meetingDetailDto = new MeetingDetailDto(meeting);
        meetingDetailDto.setMeetingMemberDtoList(meetingMemberList);


        meetingRepository.save(meetingDetailDto.toEntity());
        // meetingIdx 반환
        return DefaultResponse.res("success",
                ResponseMessage.JOIN_MEETING,
                meetingMemberDto.getMeetingIdx());


    }

    // 참여자 수 조회
    @Override
    public int findMemberCount(int meetingIdx, Gender gender) {
            int number = 0;
            List<MeetingMember> meetingMemberList = meetingRepository.findByIdx(meetingIdx).getMemberList();
            for(MeetingMember meetingMember : meetingMemberList){
                if(meetingMember.getGender() == gender){
                    number++;
                }
            }
            return number;
//            return meetingMemberRepository.countAllByMeetingIdxAndGender(meetingIdx, gender);
    }

    // 특정 게시물 조회
    @Override
    public DefaultResponse<MeetingDetailDto> findByIdx(int meetingIdx, OwnerDto ownerDto) throws Exception {
        try {
            Meeting meeting = meetingRepository.findByIdxAndActive(meetingIdx, Active.ACTIVE);
            // meeting 정보가 없으면
            if (meeting == null) {
                return DefaultResponse.res("fail", ResponseMessage.NOT_FOUND_LIST);
            }

            MeetingDetailDto meetingDetailDto = new MeetingDetailDto(meeting);

            log.info("meetingIdx >>> " + meetingDetailDto.getIdx());
            log.info("ownerIdx of meetingIdx >>> " + meetingDetailDto.getOwnerIdx());
            log.info("ownerIdx >>> " + ownerDto.getOwnerIdx());

            // owner인지 확인
            if(ownerDto.getOwnerIdx() == meetingDetailDto.getOwnerIdx()){
                meetingDetailDto.setAuth(true);
            }

            // like했는지 확인
            MeetingLike meetingLike = meetingLikeRepository.findByMeetingIdxAndOwnerIdx(meetingIdx, ownerDto.getOwnerIdx());

            if(meetingLike != null){
                meetingDetailDto.setLike(true);
            }

            //참여자 수
            meetingDetailDto.setFemNum(findMemberCount(meetingDetailDto.getIdx(),Gender.FEMALE));
            meetingDetailDto.setManNum(findMemberCount(meetingDetailDto.getIdx(), Gender.MALE));

            List<MeetingMember> meetingMembers = meeting.getMemberList();
            //참여 여부
            for(MeetingMember meetingMember : meetingMembers){
                if(meetingMember.getOwnerIdx() == ownerDto.getOwnerIdx()){
                    meetingDetailDto.setJoin(true);
                }
            }
            if(meetingMembers.size() != 0) {
                meetingDetailDto.setMeetingMemberDtoList(meetingMembers.stream().map(meetingMember ->
                        {
                            MeetingMemberDto meetingMemberDto = new MeetingMemberDto(meetingMember);
                            meetingMemberDto.setMeetingIdx(meetingIdx);
                            return meetingMemberDto;
                        }
                ).collect(Collectors.toList()));
            }


            // 사진 정보 가져오기
//            List<MeetingImgsDto> meetingImgsDtos =
//                    findImgsByMeetingIdx(meeting, meetingIdx);

//            List<MeetingImgsDto> meetingImgsList = meeting.getFileInfoList().stream().map(
//                    meetingImgs -> new MeetingImgsDto(meetingImgs)
//            ).collect(Collectors.toList());
//
//            meetingDetailDto.setImgUrl(meetingImgsList);

            // 개최자 정보 저장
            meetingDetailDto.setOwnerNickname(ownerDto.getOwnerNickname());
            meetingDetailDto.setOwnerProfileImg(ownerDto.getOwnerProfileImg());
            meetingDetailDto.setOwnerGender(ownerDto.getOwnerGener());


            return DefaultResponse.res("success", 1, ResponseMessage.READ_CONTENT,
                    meetingDetailDto);
        } catch (Exception e) {
            return DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
//
//    // 이미지리스트 조회 //TODO: 지워야 할 수도
//    @Override
//    public List<MeetingImgsDto> findImgsByMeetingIdx(Meeting meeting, int meetingIdx) {
////        List<MeetingImgs> meetingImgs = meetingImgsRepository.findAllByMeetingIdx(
////                meetingIdx
////        );
//        List<MeetingImgs> meetingImgs = meeting.getFileInfoList();
//
//        return meetingImgs.stream().map(
//                meetingImg -> new MeetingImgsDto(meetingImg)
//        ).collect(Collectors.toList());
//    }


    /* 모이자 메인 페이지 - 완료 */
    // 게시글 전체 조회(ACTIVE 인것만)
    @Override
    public DefaultResponse<List<MeetingDto>> findAll(String sort) throws Exception {
        List<Meeting> meetingList;

        if(sort.equals("RECRUITING")) {
            // meetingList 중 객체 하나를 MeetingDto의 entity->dto 하는 생성자함수로 만들어서 List로 만들어라
            meetingList = meetingRepository.findAllByActiveAndStatus(Active.ACTIVE, StatusInfo.RECRUITING);
        }
        else if(sort.equals("COMPLETE")) {
            // meetingList 중 객체 하나를 MeetingDto의 entity->dto 하는 생성자함수로 만들어서 List로 만들어라
            meetingList = meetingRepository.findAllByActiveAndStatus(Active.ACTIVE, StatusInfo.COMPLETE);
        }
        else if(sort.equals("FAIL")){
            meetingList = meetingRepository.findAllByActiveAndStatus(Active.ACTIVE, StatusInfo.FAIL);
        }
        else if(sort.equals("ALL")){
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
                    meetingDto.setFemNum(findMemberCount(meetingDto.getIdx(), Gender.FEMALE));
                    meetingDto.setManNum(findMemberCount(meetingDto.getIdx(), Gender.MALE));
//                    if(meetings.getFileInfoList().size() != 0){
//                        meetingDto.setImgUrl(meetings.getFileInfoList().get(0).getImgUrl());
//                    }
                    return meetingDto;
                }).collect(Collectors.toList()));


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
                if(sort.equals("RECRUITING")){
                    log.info("RECRUITING alladdress >>> " + address.getFirstAddr() + "," + address.getSecondAddr()
                            + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.RECRUITING);
                } else if (sort.equals("COMPLETE")){
                    log.info("COMPLETE alladdress >>> " + address.getFirstAddr() + "," + address.getSecondAddr()
                            + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.COMPLETE);
                }
                else if(sort.equals("FAIL")){
                    meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.FAIL);
                }
                else if(sort.equals("ALL")){
                    log.info("all alladdress >>> " + address.getFirstAddr() + "," + address.getSecondAddr()
                            + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByActiveAndAddress(Active.ACTIVE, address);
                }
                else {
                    return DefaultResponse.res("fail", ResponseMessage.WRONG_STATUS);
                }
            } else if (!address.getSecondAddr().equals("")){ // 첫번째, 두번째 주소만 들어 있음
                if(sort.equals("RECRUITING")){
                    log.info("RECRUITING first,second >>> " + address.getFirstAddr() + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr(), "RECRUITING");
                } else if (sort.equals("COMPLETE")){
                    log.info("complete first,second >>> " + address.getFirstAddr() + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr(), "COMPLETE");
                } else if(sort.equals("FAIL")) {
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr(), "FAIL");
                }
                else if(sort.equals("ALL")){
                    log.info("all first,second >>> " + address.getFirstAddr() + "," + address.getSecondAddr());
                    meetingList = meetingRepository.findAllByFirstAndSecondAddrWithoutStatus(
                            "ACTIVE", address.getFirstAddr(), address.getSecondAddr());
                } else {
                    return DefaultResponse.res("fail", ResponseMessage.WRONG_STATUS);
                }
            } else {    // 첫번째 주소만 들어있음
                if(sort.equals("RECRUITING")) {
                    log.info("RECRUITING firstAddr >>> " + address.getFirstAddr());
                    meetingList = meetingRepository.findAllByFirstAddrWithStatus("ACTIVE", address.getFirstAddr(), "RECRUITING");
                } else if(sort.equals("COMPLETE")){
                    log.info("COMPLETE firstAddr >>> " + address.getFirstAddr());
                    meetingList = meetingRepository.findAllByFirstAddrWithStatus("ACTIVE", address.getFirstAddr(), "COMPLETE");
                } else if(sort.equals("FAIL")) {
                    meetingList = meetingRepository.findAllByFirstAddrWithStatus("ACTIVE", address.getFirstAddr(), "FAIL");
                }
                else if(sort.equals("ALL")){
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
                    meetingDto.setFemNum(findMemberCount(meetingDto.getIdx(), Gender.FEMALE));
                    meetingDto.setManNum(findMemberCount(meetingDto.getIdx(), Gender.MALE));
//                    if(meetings.getFileInfoList().size() != 0){
//                        meetingDto.setImgUrl(meetings.getFileInfoList().get(0).getImgUrl());
//                    }
                    return meetingDto;
                }).collect(Collectors.toList()));
    }

    // 제목 keyword로 검색
    @Override
    public DefaultResponse<List<MeetingDto>> findAllByKeyword(String sort, String keyword) throws Exception {
        List<Meeting> meetingList;
        log.info("sort >>> " + sort);
        if(sort.equals("RECRUITING")) {
            log.info("RECRUITING >>> " + keyword);

            meetingList = meetingRepository.findAllByActiveAndStatusAndTitleContaining(Active.ACTIVE, StatusInfo.RECRUITING, keyword);
        }
        else if(sort.equals("COMPLETE")){
            meetingList = meetingRepository.findAllByActiveAndStatusAndTitleContaining(Active.ACTIVE, StatusInfo.COMPLETE, keyword);
        }
        else if(sort.equals("ALL")) {
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
                    meetingDto.setFemNum(findMemberCount(meetingDto.getIdx(), Gender.FEMALE));
                    meetingDto.setManNum(findMemberCount(meetingDto.getIdx(), Gender.MALE));
//                    if(meetings.getFileInfoList().size() != 0){
//                        meetingDto.setImgUrl(meetings.getFileInfoList().get(0).getImgUrl());
//                    }
                    return meetingDto;
                }).collect(Collectors.toList()));
    }


}
