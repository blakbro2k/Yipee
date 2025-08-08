package asg.games.yipee.net.game;

import asg.games.yipee.common.packets.PlayerAction;
import asg.games.yipee.net.packets.AbstractServerResponse;
import lombok.Getter;

@Getter
public class GameSeatActionPair extends AbstractServerResponse {

    private final int seat;
    private final int tick;
    private final PlayerAction action;

    public GameSeatActionPair(int seat, PlayerAction action, int tick) {
        this.seat = seat;
        this.action = action;
        this.tick = tick;
    }
}
