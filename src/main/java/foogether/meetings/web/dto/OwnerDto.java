package foogether.meetings.web.dto;

import foogether.meetings.domain.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class OwnerDto {
    // 후에 토큰 적용해서 Idx 및 ProfileImg, Nickname 저장
    private int ownerIdx;
    private String ownerProfileImg;
    private String ownerNickname;
    private Gender ownerGener;

    public OwnerDto(int ownerIdx, String ownerProfileImg, String ownerNickname, Gender ownerGener) {
        this.ownerIdx = ownerIdx;
        this.ownerProfileImg = ownerProfileImg;
        this.ownerNickname = ownerNickname;
        this.ownerGener = ownerGener;
    }
}
