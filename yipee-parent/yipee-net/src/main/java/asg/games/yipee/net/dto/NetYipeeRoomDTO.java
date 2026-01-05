package asg.games.yipee.net.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NetYipeeRoomDTO extends AbstractNetObjectDTO {
    public String loungeName;
    // convenience for UI without dragging the graph around
    private int playerCount;
    private int tableCount;
}
