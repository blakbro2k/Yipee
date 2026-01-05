package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TableSummary {
    String tableId;
    int tableNumber;
    String accessType;
    boolean created;
    boolean rated;
    boolean soundOn;
    int watcherCount;
}