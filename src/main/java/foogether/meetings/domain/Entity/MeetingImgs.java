//package foogether.meetings.domain.Entity;
//
//import foogether.meetings.domain.BaseTimeEntity;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.persistence.*;
//import java.util.Collection;
//
//@Entity
//@Table(name="meetingImgs")
//@NoArgsConstructor
//@Getter
//public class MeetingImgs {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "meetingImgs_idx")
//    int idx;
//
//    // Meeting에서 만들어줌
////    @Column(name = "meeting_idx")
////    int meetingIdx;
//
////    @ManyToOne
////    @JoinColumn(name = "meeting_idx")
////    Meeting meeting_idx;
//
//    @Column(name = "meetingImgs_url")
//    String imgUrl;
//
//    @Builder
//    public MeetingImgs(int idx, String imgUrl) {
//        this.idx = idx;
//        this.imgUrl = imgUrl;
//    }
//
////    @Builder
////    public MeetingImgs(String imgUrl) {
////        this.imgUrl = imgUrl;
////    }
//
////    @Builder
////    public MeetingImgs(int idx, int meetingIdx, String imgUrl) {
////        this.idx = idx;
////        this.imgUrl = imgUrl;
////        this.meetingIdx = meetingIdx;
////    }
//
//}
