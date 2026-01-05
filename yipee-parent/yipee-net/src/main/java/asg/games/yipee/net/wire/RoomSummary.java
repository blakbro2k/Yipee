package asg.games.yipee.net.wire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RoomSummary {
    String roomId;
    String name;
    String loungeName;
    int playerCount;
    int tableCount;
}