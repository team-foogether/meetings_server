package foogether.meetings.web.controller;

import foogether.meetings.service.JwtService;
import foogether.meetings.service.MeetingService;
import foogether.meetings.service.S3FileUploadService;
import foogether.meetings.utils.auth.Auth;
import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.MeetingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static foogether.meetings.web.dto.DefaultResponse.FAIL_DEFAULT_RES;

@Slf4j
@RequestMapping("/mypages")
@RestController
public class MyPageController {
    @Autowired
    MeetingService meetingService;

    @Autowired
    private S3FileUploadService s3FileUploadService;
    /* Auth - 진행중 */

    @Autowired
    private JwtService jwtService;

    /* 내가 참여한 모임 중 완료된 모임 */
    /* 내가 참여한 모임 중 완료되지 않은 모임 */
    @Auth
    @GetMapping("/meetings/join")
    public ResponseEntity myMeetingJoinList(
            @RequestHeader(value = "Authorization", required = false) final String header)
    {
        DefaultResponse<List<MeetingDto>> defaultResponse;

        try {
            defaultResponse = meetingService.myJoinMeetingList(header);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 내가 작성한 글 조회 */
    @Auth
    @GetMapping("/meetings/write")
    public ResponseEntity myMeetingList(
            @RequestHeader(value = "Authorization", required = false) final String header)
    {
        DefaultResponse<List<MeetingDto>> defaultResponse;

        try {
            defaultResponse = meetingService.myMeetingList(header);
            return new ResponseEntity<>(defaultResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
