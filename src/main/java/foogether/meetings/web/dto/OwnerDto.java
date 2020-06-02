package foogether.meetings.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class OwnerDto {
    // 후에 토큰 적용해서 Idx 및 ProfileImg, Nickname 저장
    private int ownerIdx;
    private String ownerProfileImg;
    private String ownerNickname;

    public OwnerDto(int ownerIdx, String ownerProfileImg, String ownerNickname) {
        this.ownerIdx = ownerIdx;
        this.ownerProfileImg = ownerProfileImg;
        this.ownerNickname = ownerNickname;
    }
}
