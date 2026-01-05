package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LaunchTokenResponse {
    String launchToken;
    long expiresAt;
    String wsUrl;
}