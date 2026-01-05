package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TableDetailsSummary {
    TableSummary table;          // existing lightweight summary
    List<SeatDetailSummary> seats;     // existing seat summaries
    List<PlayerSummary> watchers;    // just the names of watchers
}