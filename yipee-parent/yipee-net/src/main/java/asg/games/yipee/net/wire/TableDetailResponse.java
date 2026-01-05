package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TableDetailResponse {
    String roomId;
    String roomName;
    TableDetailsSummary tableDetailsSummary;
}