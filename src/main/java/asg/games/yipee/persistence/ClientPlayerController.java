package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeePlayer;

import java.util.Collection;

public interface ClientPlayerController {
    /** Player Controls
     *  Methods control the flow of a player and client */
    /** Must link clientID to Player ID to Player Object. */
    void registerPlayer(String clientId, YipeePlayer player) throws Exception;

    /** Remove Registered Player. */
    /** Should remove the from all tables, seats, games and rooms **/
    void unRegisterPlayer(String clientID) throws Exception;

    /** Get Registered Player given Yokel Id. */
    YipeePlayer getRegisteredPlayerGivenId(String playerId);

    /** Get Registered Player given YokelObject. */
    YipeePlayer getRegisteredPlayer(YipeePlayer player);

    /** Gets all registered players. */
    Collection<YipeePlayer> getAllRegisteredPlayers();

    /** Check if client id is registered **/
    boolean isClientRegistered(String clientId);

    /** Check if player id is registered **/
    boolean isPlayerRegistered(String playerId);
}
