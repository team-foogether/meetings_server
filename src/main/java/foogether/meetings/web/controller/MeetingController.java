package foogether.meetings.web.controller;

import foogether.meetings.domain.Address;
import foogether.meetings.domain.Gender;
import foogether.meetings.service.JwtService;
import foogether.meetings.service.MeetingService;
import foogether.meetings.service.S3FileUploadService;
import foogether.meetings.utils.ResponseMessage;
import foogether.meetings.utils.auth.Auth;
import foogether.meetings.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static foogether.meetings.web.dto.DefaultResponse.FAIL_DEFAULT_RES;

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
    // TODO: 삭제
    OwnerDto ownerDto = new OwnerDto(5, "owner's profileImg", "닉네임", Gender.FEMALE);

    @Autowired
    MeetingService meetingService;

    @Autowired
    private S3FileUploadService s3FileUploadService;
    /* Auth - 진행중 */

    @Autowired
    private JwtService jwtService;

    /* 글 삭제 */
    @Auth
    @DeleteMapping(value = "/{meetingIdx}")
    public ResponseEntity deleteMeeting(
            @RequestHeader(value = "Authorization") final String header,
            @PathVariable("meetingIdx") int meetingIdx
    ) {
        DefaultResponse<MeetingDetailDto> defaultResponse;
        try {

            // Auth 확인 및 Service 호출
            defaultResponse
                    = meetingService.deleteMeeting(meetingIdx, header);

            return new ResponseEntity(defaultResponse, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 글 작성 및 수정 - 완료 */
    @Auth
    @PostMapping(value = "")
    public ResponseEntity saveMeeting(
            @RequestHeader(value = "Authorization") final String header,
            MeetingDetailDto meetingDetailDto,
            @RequestPart(value = "file", required = true)
            MultipartFile img
    ) {
            DefaultResponse<MeetingDetailDto> defaultResponse;
            try {
                String imgUrl = s3FileUploadService.upload(img);

                // 이 사진 저장할 때 meeting Entity에서 저장
//            MeetingImgsDto meetingImgsDto = new MeetingImgsDto(imgUrl);
//            List<MeetingImgsDto> meetingImgsDtoList = new ArrayList<>();
//            meetingImgsDtoList.add(meetingImgsDto);

                // DetailDto 에서 endDate, endTime을 LocalDateTime으로 변경
                meetingDetailDto.setEndDayOfWeek(meetingDetailDto.getEndDate().getDayOfWeek().getDisplayName(
                        TextStyle.FULL, Locale.KOREAN));
                // img List 설정
                meetingDetailDto.setImgUrl(imgUrl);

//             save Service 호출
                defaultResponse
                        = meetingService.saveMeeting(meetingDetailDto, header);

                return new ResponseEntity(defaultResponse, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    /* 상세 조회 부분 - Owner 받아오는 부분 */
    /* 모집 완료 - 진행중 */
    // data : meetingIdx 리턴
    @Auth
    @PostMapping("/{meetingIdx}/complete")
    public ResponseEntity status(
            @RequestHeader(value = "Authorization", required = false) final String header,
            @PathVariable("meetingIdx") int meetingIdx) {

        DefaultResponse<Integer> defaultResponse;

        try {
            defaultResponse = meetingService.postComplete(meetingIdx, header);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /* 찜하기 완료 */
    // Auth 필요 없음
    @PostMapping("/{meetingIdx}/like")
    public ResponseEntity likeMeeting(
            @RequestHeader(value = "Authorization", required = true) final String header,
            @PathVariable("meetingIdx") int meetingIdx) {

        DefaultResponse<Integer> defaultResponse;
        if(jwtService.decode(header).getUserIdx() != -1) {
            try {
                // meetingIdx 반환
                defaultResponse = meetingService.postLikeState(meetingIdx, header);
                return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    /* 참여하기 - Auth 추가중*/
    @PostMapping("/{meetingIdx}/join")
    public ResponseEntity joinMeeting(
            @RequestHeader(value = "Authorization", required = true) final String header,
            @PathVariable("meetingIdx") int meetingIdx) {

        DefaultResponse<Integer> defaultResponse;

        if(jwtService.decode(header).getUserIdx() != -1) {
            try {
                // Auth 확인

                // meetingIdx 반환
                defaultResponse = meetingService.postJoinState(meetingIdx, header);
                return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    // 상세 조회 페이지
    // 추후 : PictureList 갖고오는거 구현(meetingPictures Entity 구현)
    @GetMapping("/{meetingIdx}")
    public ResponseEntity findAllByIdx(@RequestHeader(value = "Authorization", required = false) final String header,
                                       @PathVariable("meetingIdx") int meetingIdx){

        DefaultResponse<MeetingDetailDto> defaultResponse;

        try{
            // Writer 정보 조회하는 service 또는 Controller에서 api를 통해 호출해서 작성자 ProfileImg, nickname 저장
            // 상세 조회 값 가져오기
            defaultResponse = meetingService.findByIdx(meetingIdx, header);

            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
