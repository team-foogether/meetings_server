package foogether.meetings.domain.Entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import foogether.meetings.domain.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="meeting")
@NoArgsConstructor
@Getter
@DynamicInsert // (insert 시 null 인필드 제외)
@DynamicUpdate // (update 시 null 인필드 제외)
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meeting_idx")
    private int idx;

    @Column(name="owner_idx")
    private int ownerIdx; // user_id

    // 전체 보기에서 나오는 하나의 Image
    // MeetingImgs 에서는 여러개의 Imag
//    @Column(name="meeting_imgUrl")
//    private String imgUrl;

    @Column(name="meeting_createdDate")
    @CreatedDate
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Column(name="meeting_modifiedDate")
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

    // fileList
//    @OneToMany(cascade=CascadeType.ALL, mappedBy = "meeting_idx")
//    @JoinColumn(name = "meeting_idx")
    private String imgUrl;

    @PrePersist
    public void prePersist() {
        this.active = this.active == null ? Active.ACTIVE : this.active;
        this.status = this.status == null ? StatusInfo.RECRUITING : this.status;
    }

    // 나중에 추가
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meeting_idx")
    private List<MeetingMember> memberList;

    // MeetingDto에서 호출하여 dto->Entity로 만들때 사용
    // String imgUrl
    @Builder
    public Meeting(int idx, int ownerIdx, LocalDateTime modifiedDate, DateInfo endDate, String title, String content,
                      Address address, StatusInfo status, int manMax, int femMax, Active active, String imgUrl
                    , List<MeetingMember> meetingMemberList) {
//        // autoIncrement로 DB에서 저장할때 자동으로 올려주기 때문에 따로 저장 하지 않음
        this.idx = idx;
        this.ownerIdx = ownerIdx;
//        this.imgUrl = imgUrl;
        this.modifiedDate = modifiedDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.address = address;
        this.status = status;
        this.manMax = manMax;
        this.femMax = femMax;
        this.active = active;
        
        // TODO: 확인
        this.imgUrl = imgUrl;
        this.memberList = meetingMemberList;
    }

}
