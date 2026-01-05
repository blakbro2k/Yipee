package asg.games.yipee.net.wire;

import asg.games.yipee.common.game.PlayerAction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class TickedPlayerActionData extends PlayerAction {
    private int tick;
    private long timeStamp;

    TickedPlayerActionData(PlayerAction action, int tick, long timeStamp) {
        this.tick = tick;
        this.timeStamp = timeStamp;
    }
}