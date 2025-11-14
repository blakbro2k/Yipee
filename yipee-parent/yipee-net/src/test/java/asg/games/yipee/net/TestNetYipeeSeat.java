package asg.games.yipee.net;


import asg.games.yipee.common.dto.NetYipeeSeat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestNetYipeeSeat extends TestNetYipeeObject implements NetYipeeSeat {
    int seatNumber;
    boolean seatReady;

    public TestNetYipeeSeat() {
    }
}
