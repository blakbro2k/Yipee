package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class JoinTableRequest {
    String roomId;
    int tableNumber;
    boolean createIfMissing;
}