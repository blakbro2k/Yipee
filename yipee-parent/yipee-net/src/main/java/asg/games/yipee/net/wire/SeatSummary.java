package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SeatSummary {
    String seatId;
    int seatNumber;
    boolean seatReady;
    boolean occupied;
    String playerId;
    String playerName;
}