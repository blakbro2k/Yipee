package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateTableResponse {
    String roomId;
    String roomName;
    String tableId;
    int tableNumber;
    String playerId;
    boolean created;
}