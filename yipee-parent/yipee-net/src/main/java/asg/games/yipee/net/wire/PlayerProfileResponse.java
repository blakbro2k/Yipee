package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PlayerProfileResponse {
    private String playerId;
    private String name;
    private int icon;
    private int rating;
    private String sessionId;
}