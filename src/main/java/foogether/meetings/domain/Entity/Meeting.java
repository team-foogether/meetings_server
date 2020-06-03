package foogether.meetings.domain.Entity;

import foogether.meetings.domain.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="meeting")
@NoArgsConstructor
@Getter
@org.hibernate.annotations.DynamicUpdate
public class Meeting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meeting_idx")
    private int idx;

    @Column(name="owner_idx")
    private int ownerIdx; // user_id

    // 전체 보기에서 나오는 하나의 Image
    // MeetingImgs 에서는 여러개의 Imag
    @Column(name="meeting_imgUrl")
    private String imgUrl;

    @Column(name="meeting_createdDate")
    @CreatedDate
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Column(name="meeting_updateDate")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name="meeting_endDate")
    private DateInfo endDate;

    @Column(name="meeting_title")
    private String title;

    @Column(name="meeting_content")
    private String content;

    @Column(name="meeting_address")
    private Address address;

    // 모이자, 모집중, 모집실패
    @Enumerated(EnumType.STRING)
    @Column(name="meeting_status")
    private StatusInfo status;

    @Column(name="meeting_manMax")
    private int manMax;

    @Column(name="meeting_femMax")
    private int femMax;

    @Enumerated(EnumType.STRING)
    @Column(name="meeting_active")
    private Active active;

    // MeetingDto에서 호출하여 dto->Entity로 만들때 사용
    @Builder
    public Meeting(int idx, int ownerIdx, String imgUrl, DateInfo endDate, String title, String content,
                      Address address, StatusInfo status, int manMax, int femMax, Active active) {
//        // autoIncrement로 DB에서 저장할때 자동으로 올려주기 때문에 따로 저장 하지 않음
        this.idx = idx;
        this.ownerIdx = ownerIdx;
        this.imgUrl = imgUrl;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.address = address;
        this.status = status;
        this.manMax = manMax;
        this.femMax = femMax;
        this.active = active;
    }

}
