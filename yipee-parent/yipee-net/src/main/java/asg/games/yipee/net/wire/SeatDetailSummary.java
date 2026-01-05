package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SeatDetailSummary {
    String seatId;
    int seatNumber;
    boolean seatReady;
    boolean occupied;
    PlayerSummary playerSummary;
}