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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Response sent by the server before the game starts.
 *
 * <p>This informs all clients of a synchronized countdown and provides
 * the deterministic seed needed to initialize their local prediction engines.</p>
 *
 * <p>Used for synchronizing game start time and ensuring all players
 * begin with the same RNG-based logic and initial state.</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameStartResponse extends AbstractServerResponse {

    /**
     * Number of seconds remaining before the game starts.
     * <p>
     * Clients should use this to display a countdown UI.
     * When it reaches 0, the game logic begins on all sides.
     */
    private int countdownSecondsRemaining;

    /**
     * A shared RNG seed used by both client and server to ensure
     * deterministic gameplay logic (e.g., piece drops).
     * <p>
     * This must be used to initialize all YipeeGameBoard instances
     * involved in the match for consistent simulation and prediction.
     */
    private long gameSeed;
}
