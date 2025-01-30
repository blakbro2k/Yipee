package asg.games.yipee.net;

import asg.games.yipee.objects.YipeePlayer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DisconnectRequest {
    private long clientId;
    private long timeStamp;
    private String sessionId;
    private YipeePlayer player;
}
