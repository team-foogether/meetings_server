package foogether.meetings.web.controller;

import foogether.meetings.domain.Address;
import foogether.meetings.domain.Entity.Meeting;
import foogether.meetings.service.MeetingService;
import foogether.meetings.utils.ResponseMessage;
import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.MeetingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/search")
    public ResponseEntity findAll(@RequestHeader(value = "Authorization", required = false) final String header,
                                  @RequestParam(value = "limit", defaultValue = "0", required = false) int limit,
                                  @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
                                  @RequestParam(value = "sort", defaultValue = "", required = false) String sort,
                                  @RequestParam(value = "keyword",  defaultValue = "", required = false) String keyword,
                                  @RequestParam(value = "firstAddr", defaultValue = "", required = false) String firstAddr,
                                  @RequestParam(value = "secondAddr", defaultValue = "", required = false) String secondAddr,
                                  @RequestParam(value = "thirdAddr", defaultValue = "", required = false) String thirdAddr
                                  ) {
        try {
            DefaultResponse<List<MeetingDto>> defaultResponse;
            if(firstAddr.equals("")){ // 주소 검색 아님
                if(keyword.equals("")){ // keyword 검색 아님
                    // 검색해온 meetingList == defaultResponse
                    defaultResponse =
                            meetingService.findAll(sort);
                }
                else {  // keyword 검색
                    defaultResponse =
                            meetingService.findAllByKeyword(sort, keyword);
                }
            }
            else {  // 주소 검색
                Address address = new Address(firstAddr, secondAddr, thirdAddr);
                defaultResponse =
                        meetingService.findAllByAddress(sort, address);
            }


            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);

        } catch (Exception e) {
            DefaultResponse defaultResponse = DefaultResponse.res("fail", ResponseMessage.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
