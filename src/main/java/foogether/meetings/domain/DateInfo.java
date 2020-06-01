package foogether.meetings.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Getter
public class DateInfo {
    private LocalDate meeting_endDate;
    private LocalTime meeting_endTime;
    private int meeting_endDayOfWeek;

    public DateInfo() {
    }

    public DateInfo(LocalDate meeting_endDate, LocalTime meeting_endTime, int meeting_endDayOfWeek){
        this.meeting_endDate = meeting_endDate;
        this.meeting_endTime = meeting_endTime;
        this.meeting_endDayOfWeek = meeting_endDayOfWeek;
    }

}
