package foogether.meetings.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String firstAddr;
    private String secondAddr;
    private String thirdAddr;

    protected Address(){

    }

    public Address(String firstAddr, String secondAddr, String thirdAddr){
        this.firstAddr = firstAddr;
        this.secondAddr = secondAddr;
        this.thirdAddr = thirdAddr;
    }

}
