/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.core.persistence;

import asg.games.yipee.core.objects.YipeePlayer;

import java.util.Collection;

/**
 * Defines methods for managing the association between client IDs and {@link YipeePlayer} instances.
 * <p>
 * Used to track connected players, register/unregister them, and query their association across sessions.
 * </p>
 *
 * <p>Implementations should ensure proper cleanup of players from tables, seats, games, and rooms upon unregistration.</p>
 */
public interface ClientPlayerController {

    /**
     * Registers a player to a given client ID.
     * This creates an association between the client's network connection and a specific {@link YipeePlayer}.
     *
     * @param clientId the ID of the client connection
     * @param player   the player to register
     * @throws Exception if registration fails or is invalid
     */
    void registerPlayer(String clientId, YipeePlayer player) throws Exception;

    /**
     * Unregisters a player associated with the given client ID.
     * <p>This should also remove the player from all game-related associations such as tables, seats, games, and rooms.</p>
     *
     * @param clientID the ID of the client to unregister
     * @throws Exception if unregistration fails
     */
    void unRegisterPlayer(String clientID) throws Exception;

    /**
     * Retrieves a registered player by their player ID.
     *
     * @param playerId the unique ID of the player
     * @return the registered {@link YipeePlayer} or {@code null} if not found
     */
    YipeePlayer getRegisteredPlayerGivenId(String playerId);

    /**
     * Retrieves the canonical registered player instance for the given player object.
     *
     * @param player the player object to look up
     * @return the registered {@link YipeePlayer}, or {@code null} if not found
     */
    YipeePlayer getRegisteredPlayer(YipeePlayer player);

    /**
     * Returns a collection of all currently registered players.
     *
     * @return a collection of {@link YipeePlayer} instances
     */
    Collection<YipeePlayer> getAllRegisteredPlayers();

    /**
     * Checks whether the specified client ID has a registered player.
     *
     * @param clientId the ID of the client
     * @return {@code true} if registered, otherwise {@code false}
     */
    boolean isClientRegistered(String clientId);

    /**
     * Checks whether a player ID is currently registered.
     *
     * @param playerId the ID of the player
     * @return {@code true} if the player is registered, otherwise {@code false}
     */
    boolean isPlayerRegistered(String playerId);
}
