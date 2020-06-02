package foogether.meetings.web.controller;

import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.service.MeetingService;
import foogether.meetings.utils.ResponseMessage;
import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.MeetingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/meetings")
@RestController
public class MeetingController {
//    private static final DefaultResponse UNAUTHORIZED_RES = new DefaultResponse("error", ResponseMessage.UNAUTHORIZED);

    @Autowired
    MeetingService meetingService;

    // TODO : Token 적용
    // @RequestHeader(value = "Authorization", required = false) final String header
    // ResponseEntity : Status 코드 상태변수와 body로 이루어져 있음
    // 모이자 전체 리스트 가져오기
//    @GetMapping("/search")
//    public ResponseEntity findAll(@RequestHeader(value = "Authorization", required = false) final String header,
//                                  @RequestParam(value = "limit", defaultValue = "0", required = false) int limit,
//                                  @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
//                                  @RequestParam(value = "sort", defaultValue = "", required = false) String sort,
//                                  @RequestParam(value = "keyword",  defaultValue = "", required = false) String keyword,
//                                  @RequestParam(value = "firstAddr", defaultValue = "", required = false) String firstAddr,
//                                  @RequestParam(value = "secondAddr", defaultValue = "", required = false) String secondAddr,
//                                  @RequestParam(value = "thirdAddr", defaultValue = "", required = false) String thirdAddr
//                                  ) {
//
//        DefaultResponse<List<MeetingDto>> defaultResponse;
//        try {
//            if(firstAddr.equals("")){ // 주소 검색 아님
//                if(keyword.equals("")){ // keyword 검색 아님
//                    // 검색해온 meetingList == defaultResponse
//                    defaultResponse =
//                            meetingService.findAll(sort);
//                }
//                else {  // keyword 검색
//                    defaultResponse =
//                            meetingService.findAllByKeyword(sort, keyword);
//                }
//            }
//            else {  // 주소 검색
//                Address address = new Address(firstAddr, secondAddr, thirdAddr);
//                defaultResponse =
//                        meetingService.findAllByAddress(sort, address);
//            }
//
//
//            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
//
//        } catch (Exception e) {
//            defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
//            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



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
