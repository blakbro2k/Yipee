/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
