package foogether.meetings.utils.auth;


import foogether.meetings.client.UserClient;
import foogether.meetings.repository.MeetingRepository;
import foogether.meetings.service.JwtService;
import foogether.meetings.web.dto.DefaultResponse;
import foogether.meetings.web.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
// filter와 같은 역할
public class AuthAspect {
    private final static String AUTHORIZATION = "Authorization";

    /**
     * 실패 시 기본 반환 Response
     */
    private final static DefaultResponse DEFAULT_RES = DefaultResponse.builder().message("인증 실패").build();
    private final static ResponseEntity<DefaultResponse> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);

    private final HttpServletRequest httpServletRequest;

//    @Autowired
//    private final UserRepository userRepository;
    @Autowired
    private final MeetingRepository meetingRepository;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    UserClient userClient;

    /**
     * 토큰 유효성 검사
     * @param pjp
     * @return
     * @throws Throwable
     */
    //항상 @annotation 패키지 이름을 실제 사용할 annotation 경로로 맞춰줘야 한다.
    @Around("@annotation(foogether.meetings.utils.auth.Auth)")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        final String jwt = httpServletRequest.getHeader(AUTHORIZATION);
        //토큰 존재 여부 확인
        if (jwt == null) return RES_RESPONSE_ENTITY;
        //토큰 해독
        final JwtService.Token token = jwtService.decode(jwt);
        //토큰 검사
        if (token == null) {
            return RES_RESPONSE_ENTITY;
        } else {
//            final User user = userRepository.findByIdx(token.getUserIdx());
            UserResponseDto userResponseDto;
            try {
                final ResponseEntity<DefaultResponse<UserResponseDto>> userInfo
                        = userClient.getUserInfo(token.getUserIdx());
                userResponseDto = new UserResponseDto(userInfo.getBody().getData());
            } catch (Exception e){
                return RES_RESPONSE_ENTITY;
            }

//          final User user = userRepository.findByIdx(token.getUserIdx());
            //유효 사용자 검사
            if (userResponseDto == null) return RES_RESPONSE_ENTITY;
            return pjp.proceed(pjp.getArgs());
        }
    }
}
