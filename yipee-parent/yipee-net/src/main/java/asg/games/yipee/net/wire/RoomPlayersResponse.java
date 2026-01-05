package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RoomPlayersResponse {
    String roomId;
    String roomName;
    String loungeName;
    java.util.List<PlayerSummary> players;
}