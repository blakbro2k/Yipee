package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SitDownResponse {
    String roomId;
    String roomName;
    String tableId;
    String playerId;
    int tableNumber;
    String seatId;
    int seatNumber;
    boolean seatReady;
    boolean occupied;
}