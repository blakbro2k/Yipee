package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RegisterPlayerRequest {
    private String playerName;
    private int icon;
    private int rating;
    private String clientId;
}