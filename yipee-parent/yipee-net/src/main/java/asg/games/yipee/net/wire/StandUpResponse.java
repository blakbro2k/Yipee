package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StandUpResponse {
    String roomId;
    String roomName;
    String tableId;
    int tableNumber;
    String playerId;
    String seatId;
    int seatNumber;
    boolean success;
}