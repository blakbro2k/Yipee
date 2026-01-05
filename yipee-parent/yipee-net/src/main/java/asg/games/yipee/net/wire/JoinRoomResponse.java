package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JoinRoomResponse {
    String roomId;
    String roomName;
    String loungeName;
    List<TableSummary> tables;
}