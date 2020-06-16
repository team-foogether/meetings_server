package foogether.meetings.client;

import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.OwnerDto;
import foogether.meetings.web.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@Component
@FeignClient(url="http://foogether.us:9090", name = "user-service")
public interface UserClient {

    @PostMapping("/users/list")
    ResponseEntity<DefaultResponse<List<UserResponseDto>>> getUserList(
            @RequestBody List<Integer> UserIdxList);

    @GetMapping("/users/{userIdx}")
    ResponseEntity<DefaultResponse<UserResponseDto>> getUserInfo(
            @PathVariable("userIdx") int userIdx);

}

