package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GameWhoAmIResponse {
    String playerId;
    String name;
    int icon;
    int rating;
    String clientId;
    String sessionId;
    String gameId;
    String tableId;
    int seatIndex;
    long expiresAt;
    String serverId;
    long serverTick;
    long serverTimestamp;
    float tickRate;
}
