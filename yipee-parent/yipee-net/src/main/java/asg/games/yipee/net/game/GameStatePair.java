package asg.games.yipee.net.game;

import asg.games.yipee.common.game.GameBoardState;
import asg.games.yipee.net.packets.AbstractServerResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStatePair extends AbstractServerResponse {
    public final int seat;
    public final GameBoardState state;

    public GameStatePair(int seat, GameBoardState state) {
        this.seat = seat;
        this.state = state;
    }
}
