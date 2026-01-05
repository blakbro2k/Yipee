package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JoinTableResponse {
    String roomId;
    String roomName;
    String tableId;
    String playerId;
}