package asg.games.yipee.net;

import asg.games.yipee.objects.YipeePlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionResponse {
    private String serverId;
    private long timeStamp;
    private String sessionId;
    private YipeePlayer player;
    private boolean connected;
}
