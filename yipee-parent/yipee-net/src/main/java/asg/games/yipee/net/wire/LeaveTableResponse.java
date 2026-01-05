package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LeaveTableResponse {
    String tableId;
    String playerId;
    boolean leftTable;
    boolean wasSeated;
    boolean wasWatcher;
}