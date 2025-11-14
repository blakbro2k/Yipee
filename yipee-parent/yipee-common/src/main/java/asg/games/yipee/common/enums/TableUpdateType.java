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
package asg.games.yipee.common.enums;

/**
 * Represents the specific type of update or event that occurred at a Yipee game table.
 *
 * <p>These update types allow clients and servers to determine which aspect of a table's
 * state changed and to react appropriately—whether updating UI elements, refreshing seat
 * states, or triggering transitions such as game start or end sequences.
 *
 * <p>Each constant is intentionally granular to support lightweight network updates and
 * efficient filtering on the client side.
 */
public enum TableUpdateType {

    /**
     * Indicates that a player marked themselves as ready to start the game.
     */
    PLAYER_READY,

    /**
     * Indicates that a player has taken a seat at the table.
     *
     * <p>Clients may use this signal to update seat occupancy visuals or enable
     * contextual UI elements (e.g., ready buttons).
     */
    PLAYER_SEATED,

    /**
     * Indicates that a player stood up from their seat and became a watcher/spectator.
     */
    PLAYER_STAND,

    /**
     * Indicates that a player has fully left the table.
     *
     * <p>Clients should remove or grey-out the player entry and may need to recalculate
     * whether the minimum number of active players is still met.
     */
    PLAYER_LEFT,

    /**
     * Indicates that a player's game session ended, often due to board overflow or a loss
     * condition during gameplay.
     */
    PLAYER_LOST,

    /**
     * Indicates that one or more table-level settings were modified (e.g., rating mode,
     * sound settings, access type, or argument flags).
     */
    SETTINGS_CHANGED,

    /**
     * Indicates a general update to the table's state that does not fall under a more
     * specific category. Often used for synchronization passes.
     */
    UPDATE_STATE,

    /**
     * Indicates that the game is starting, typically when required ready conditions are met.
     *
     * <p>Clients may respond by transitioning to the gameplay screen, initializing boards,
     * or triggering countdown animations.
     */
    GAME_STARTED,

    /**
     * Indicates that the game has ended, usually due to win/loss conditions being met
     * or all opponents being eliminated.
     *
     * <p>Clients may respond by showing final results, animations, or returning players
     * back to the table view.
     */
    GAME_ENDED
}
