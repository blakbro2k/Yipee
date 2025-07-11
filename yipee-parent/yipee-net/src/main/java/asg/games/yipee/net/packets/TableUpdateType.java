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
package asg.games.yipee.net.packets;

/**
 * Enum representing the type of update that occurred at the table.
 * Used by clients to determine what changed and respond accordingly.
 */
public enum TableUpdateType {
    PLAYER_READY,      // A player marked themselves ready.
    PLAYER_SEATED,     // A player sat down at the table.
    PLAYER_STAND,      // A player stood up and became a spectator.
    PLAYER_LEFT,       // A player left the table entirely.
    PLAYER_LOST,       // A player's game ended (e.g. board overflow).
    SETTINGS_CHANGED,  // Table settings were modified.
    UPDATE_STATE,      // Updating the state of a table.
    GAME_STARTED,      // Game is starting (all required players ready).
    GAME_ENDED         // Game has ended (win/loss condition met).
}
