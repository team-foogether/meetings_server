package foogether.meetings.web.dto;

import foogether.meetings.domain.Gender;
import lombok.*;

@Data
@NoArgsConstructor
public class OwnerDto {
    // 후에 토큰 적용해서 Idx 및 ProfileImg, Nickname 저장
    private int ownerIdx;
    private String ownerProfileImg;
    private String ownerNickname;
    private Gender ownerGener;

    private String ownerName;
    private String ownerPhoneNum;
    private String ownerEmailAddr;

    @Builder
    public OwnerDto(int ownerIdx, String ownerProfileImg, String ownerNickname, Gender ownerGener
    , String ownerName, String ownerPhoneNum, String ownerEmailAddr) {
        this.ownerIdx = ownerIdx;
        this.ownerProfileImg = ownerProfileImg;
        this.ownerNickname = ownerNickname;
        this.ownerGener = ownerGener;

        this.ownerName = ownerName;
        this.ownerPhoneNum = ownerPhoneNum;
        this.ownerEmailAddr = ownerEmailAddr;
    }

    @Builder
    public OwnerDto(int ownerIdx, String ownerProfileImg, String ownerNickname, Gender ownerGener) {
        this.ownerIdx = ownerIdx;
        this.ownerProfileImg = ownerProfileImg;
        this.ownerNickname = ownerNickname;
        this.ownerGener = ownerGener;
    }
}
