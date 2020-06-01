package foogether.meetings.service;

import foogether.meetings.domain.Active;
import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.StatusInfo;
import foogether.meetings.repository.MeetingRepository;
import foogether.meetings.utils.ResponseMessage;
import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.MeetingDto;
import jdk.net.SocketFlow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.ACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor    // final차입 repository 의존성 주입
@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private final MeetingRepository meetingRepository;

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
        else {
            // meetingList 중 객체 하나를 MeetingDto의 entity->dto 하는 생성자함수로 만들어서 List로 만들어라
            meetingList = meetingRepository.findAllByActive(Active.ACTIVE);
        }

        int numMeeting = meetingList.size();

        return DefaultResponse.res("success", numMeeting, ResponseMessage.READ_ALL_CONTENTS,
                meetingList.stream()
                        .map(meetings -> new MeetingDto(meetings))
                        .collect(Collectors.toList()));

    }

    // 주소로 검색
    // TODO: first, second 두개만 들어와도 동작할 수 있도록 하기
    @Override
    public DefaultResponse<List<MeetingDto>> findAllByAddress(String sort, Address address) throws Exception {
        List<Meeting> meetingList;

        if(sort.equals("recruiting")) {
            meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.RECRUITING);
        }
        else if(sort.equals("complete")){
            meetingList = meetingRepository.findAllByActiveAndAddressAndStatus(Active.ACTIVE, address, StatusInfo.COMPLETE);
        }
        else {
            meetingList = meetingRepository.findAllByActiveAndAddress(Active.ACTIVE, address);
        }
        int numMeeting = meetingList.size();

        return DefaultResponse.res("success", numMeeting, ResponseMessage.READ_ALL_CONTENTS,
                meetingList.stream()
                        .map(meetings -> new MeetingDto(meetings))
                        .collect(Collectors.toList()));
    }

    // 제목 keyword로 검색
    @Override
    public DefaultResponse<List<MeetingDto>> findAllByKeyword(String sort, String keyword) throws Exception {
        List<Meeting> meetingList;

        if(sort.equals("recruiting")) {
            meetingList = meetingRepository.findAllByActiveAndStatusAndTitleContaining(Active.ACTIVE, StatusInfo.RECRUITING, keyword);
        }
        else if(sort.equals("complete")){
            meetingList = meetingRepository.findAllByActiveAndStatusAndTitleContaining(Active.ACTIVE, StatusInfo.COMPLETE, keyword);
        }
        else {
            meetingList = meetingRepository.findAllByActiveAndTitleContaining(Active.ACTIVE, keyword);

        }

        int numMeeting = meetingList.size();

        return DefaultResponse.res("success", numMeeting, ResponseMessage.READ_ALL_CONTENTS,
                meetingList.stream()
                        .map(meetings -> new MeetingDto(meetings))
                        .collect(Collectors.toList()));
    }
}
