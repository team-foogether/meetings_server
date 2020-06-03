package foogether.meetings.utils;

public class ResponseMessage {
    /**
     * default
     */
    public static final String AUTHORIZED = "인증 성공";
    public static final String UNAUTHORIZED = "인증 실패";
    public static final String FORBIDDEN = "인가 실패";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String SERVICE_UNAVAILABLE = "현재 서비스를 사용하실 수 없습니다. 잠시후 다시 시도해 주세요.";

    public static final String DB_ERROR = "데이터베이스 에러";

    /**
     * Meetings
     */
    public static final String NOT_FOUND_LIST = "조회 정보를 찾을 수 없습니다.";
    public static final String READ_ALL_BUT_ZERO = "작성된 게시글이 없습니다.";
    public static final String WRONG_STATUS = "카테고리 설정이 잘못되었습니다.";
    public static final String READ_ALL_CONTENTS = "전체 게시판 조회 성공";
    public static final String READ_CONTENT = "상세 게시판 조회 성공";

    public static final String CREATE_CONTENT = "게시글 작성 성공";
    public static final String FAIL_CREATE_CONTENT = "게시글 작성 실패";
    public static final String UPDATE_CONTENT = "컨텐츠 수정 성공";
    public static final String FAIL_UPDATE_CONTENT = "게시글 수정 실패";
    public static final String DELETE_CONTENT = "게시글 삭제 성공";
    public static final String LIKE_CONTENT = "게시글 좋아요/해제 성공";
    public static final String JOIN_MEETING = "참여하기 성공";
    public static final String OUT_MEETING = "참여취소 성공";



}