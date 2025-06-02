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
package asg.games.yipee.net;

import asg.games.yipee.objects.YipeeGameBoardState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Sent by the server to all clients during each game loop tick.
 *
 * <p>This packet contains the current authoritative game board state and
 * tick counter, allowing clients to animate, interpolate, or re-synchronize
 * their local view of the board.</p>
 *
 * <p><b>Direction:</b> Server → Clients</p>
 *
 * <p>Clients should expect to receive this at a regular interval (e.g. 60Hz)
 * and use {@code tickNumber} to align or skip late/missing updates.</p>
 */
@Data
@NoArgsConstructor
public class GameBoardStateTick implements YipeeSerializable {

    /**
     * The tick number corresponding to this game state update.
     * Typically incremented by the server each frame or turn.
     */
    private int tick;

    /**
     * The current state of the game board at this tick.
     * This should be replaced with a strongly-typed class (e.g., {@code YipeeGameBoardState})
     * for better serialization and safety.
     */
    private Map<Integer, YipeeGameBoardState> gameBoardStates; // Replace with actual board state class
}
