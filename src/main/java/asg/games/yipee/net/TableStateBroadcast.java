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
package asg.games.yipee.net;

import asg.games.yipee.objects.YipeeTable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Broadcast sent by the server to all clients seated at a table,
 * indicating that the table's state has changed.
 * <p>
 * This may reflect player actions (e.g. ready, seated), table configuration changes,
 * or game lifecycle events (e.g. start/end).
 *
 * <p><b>Direction:</b> Server → Clients</p>
 */
@Data
@NoArgsConstructor
public class TableStateBroadcast implements YipeeSerializable {

    /**
     * Enum describing what kind of update occurred to the table state.
     * Helps clients efficiently decide how to re-render or respond.
     */
    public enum TableUpdateType {
        /**
         * A player marked themselves ready (while seated).
         */
        PLAYER_READY,

        /**
         * A player has taken a seat at the table.
         */
        PLAYER_SEATED,

        /**
         * A player has stood up from a seat and is now a watcher.
         */
        PLAYER_STAND,

        /**
         * A player has left the table entirely (disconnected or exited).
         */
        PLAYER_LEFT,

        /**
         * A player's board overflowed; they are now out of the game.
         */
        PLAYER_LOST,

        /**
         * The table settings were modified (owner, access type, etc.).
         */
        SETTINGS_CHANGED,

        /**
         * All required players are ready and the game is starting.
         */
        GAME_STARTED,

        /**
         * The game has ended, either due to win condition or player loss.
         */
        GAME_ENDED
    }


    /**
     * The authoritative table object after the server has applied updates.
     * Clients use this to re-render or apply UI state transitions.
     */
    private YipeeTable table;

    /**
     * Enum describing what triggered this update (e.g. a setting or seat change).
     */
    private TableUpdateType updateType;
}
