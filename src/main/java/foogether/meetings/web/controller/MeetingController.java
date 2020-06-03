package foogether.meetings.web.controller;

import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.domain.Entity.MeetingMember;
import foogether.meetings.domain.Gender;
import foogether.meetings.service.MeetingService;
import foogether.meetings.utils.ResponseMessage;
import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.MeetingDto;
import foogether.meetings.web.dto.MeetingMemberDto;
import foogether.meetings.web.dto.OwnerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/meetings")
@RestController
public class MeetingController {
//    private static final DefaultResponse UNAUTHORIZED_RES = new DefaultResponse("error", ResponseMessage.UNAUTHORIZED);
    // TODO : Token 적용
    // @RequestHeader(value = "Authorization", required = false) final String header
    // ResponseEntity : Status 코드 상태변수와 body로 이루어져 있음
    /*
    (@RequestHeader(value = "Authorization") final String header,
      final MeetingDto meetingDto, final MultipartFile pic_url)
     */
    @Autowired
    MeetingService meetingService;
    /* Auth - 진행중 */
    /* 좋아요 - 진행중 */
    /* 글 삭제 - 진행중 */
    /* 글 작성 - TODO: 진행중 */
//    @Auth
//    @PostMapping("")
//    public ResponseEntity saveMeeting(
//            @RequestHeader(value = "Authorization") final String header,
//            final MeetingDto meetingDto, final MultipartFile pic_url) {
//
//            try {
//                return null;
//            } catch (Exception e){
//                return null;
//            }
//    }

    /* 상세 조회 부분 - Owner 받아오는 부분 / 참여한 멤버 수 / 현재 조인한 상태인지 */
    // TODO: 다른 페이지에서 실제로 Owner 받아오는 부분 구현해야함
    @GetMapping("/{meetingIdx}")
    public ResponseEntity findAllByIdx(@RequestHeader(value = "Authorization", required = false) final String header,
                                       @PathVariable("meetingIdx") int meetingIdx){

        DefaultResponse<MeetingDto> defaultResponse;

        try{
            // Writer 정보 조회하는 service 또는 Controller에서 api를 통해 호출해서 작성자 ProfileImg, nickname 저장
            // TODO: <- 다른 서비스에서 Owner 정보 갖고와야 함
            // TODO : TOKEN 통해 Owner 정보 가져오기
            OwnerDto ownerDto = new OwnerDto(5, "owner's profileImg", "닉네임", Gender.F);

            // 상세 조회 값 가져오기
            defaultResponse = meetingService.findByIdx(meetingIdx, ownerDto);

            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);

        }catch (Exception e){
            defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /* 참여하기 - TODO: 진행중 */
    //TODO: Auth 추가
    // @Auth
    // int는 게시판 번호 리턴
    @PostMapping("/join")
    public ResponseEntity joinMeeting(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @RequestBody MeetingMemberDto meetingMemberDto) {

        DefaultResponse<Integer> defaultResponse;
        try {
            // Auth 확인

            // meetingIdx 반환
            defaultResponse =  meetingService.postJoinState(meetingMemberDto);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
        } catch (Exception e){
            defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /* 전체 조회 부분 완료 */
    //조건 없이 전체 조회
    @GetMapping("/{offset}/{limit}/sort/{sort}")
    public ResponseEntity findAll(@RequestHeader(value = "Authorization", required = false) final String header,
                                  @PathVariable("sort") String sort) {
        DefaultResponse<List<MeetingDto>> defaultResponse;
        try{
            defaultResponse = meetingService.findAll(sort);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);

        } catch (Exception e) {
            defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 키워드로 전체 조회
    @GetMapping("/{offset}/{limit}/sort/{sort}/keyword/{keyword}")
    public ResponseEntity findAllByKeyword(@RequestHeader(value = "Authorization", required = false) final String header,
                                  @PathVariable("limit") int limit,
                                  @PathVariable("offset") int offset,
                                  @PathVariable("sort") String sort,
                                  @PathVariable("keyword") String keyword
    ) {

        DefaultResponse<List<MeetingDto>> defaultResponse;
        try {
            defaultResponse = meetingService.findAllByKeyword(sort, keyword);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);

        } catch (Exception e) {
            defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 위치 정보로 전체 조회
    @GetMapping("/{offset}/{limit}/sort/{sort}/firstAddr/{firstAddr}/secondAddr/{secondAddr}/thirdAddr/{thirdAddr}")
    public ResponseEntity findAllByKeyword(@RequestHeader(value = "Authorization", required = false) final String header,
                                           @PathVariable("limit") int limit,
                                           @PathVariable("offset") int offset,
                                           @PathVariable("sort") String sort,
                                           @PathVariable("firstAddr") String firstAddr,
                                           @PathVariable("secondAddr") String secondAddr,
                                           @PathVariable("thirdAddr") String thirdAddr
    ) {

        DefaultResponse<List<MeetingDto>> defaultResponse;

        try {
            Address address;

            // Adrress 객체 생성 및 데이터 저장
            if (secondAddr.equals("전체")) {
                address = new Address(firstAddr, "", "");
            } else if (thirdAddr.equals("전체")) {
                address = new Address(firstAddr, secondAddr, "");
            } else {
                address = new Address(firstAddr, secondAddr, thirdAddr);
            }
            
            log.info("controller sort >>> " + sort);
            log.info("contoller firstAddr >>> " + address.getFirstAddr());
            log.info("contoller secondAddr >>> " + address.getSecondAddr());
            log.info("contoller thirdAddr >>> " + address.getThirdAddr());

            defaultResponse = meetingService.findAllByAddress(sort, address);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);

        } catch (Exception e) {
            defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
