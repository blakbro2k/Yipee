package asg.games.yipee.net;


import asg.games.yipee.common.dto.NetYipeeKeyMap;
import asg.games.yipee.common.dto.NetYipeePlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestNetYipeePlayer extends TestNetYipeeObject implements NetYipeePlayer {
    int rating;
    int icon;

    public TestNetYipeePlayer() {
    }

    public TestNetYipeePlayer(String playerName, int rating, int iconNumber) {
        setName(playerName);
        setRating(rating);
        setIcon(iconNumber);
    }

    @Override
    public NetYipeeKeyMap getKeyConfig() {
        return null;
    }
}
