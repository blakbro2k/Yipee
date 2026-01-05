package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateTableRequest {
    String roomId;
    boolean rated;
    boolean soundOn;
    String accessType;
}