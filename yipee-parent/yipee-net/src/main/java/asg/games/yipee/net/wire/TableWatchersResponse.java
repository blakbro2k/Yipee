package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TableWatchersResponse {
    String tableId;
    int watcherCount;
    List<PlayerSummary> watchers;
}