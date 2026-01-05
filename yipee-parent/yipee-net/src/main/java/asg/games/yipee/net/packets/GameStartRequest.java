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
package asg.games.yipee.net.packets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Sent by the client when a player clicks "Join" and toggles their ready state
 * before the game begins. The server uses this to determine whether all players
 * at the table are ready to begin a match.
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameStartRequest extends AbstractClientRequest {

    /**
     * Whether the player has marked themselves as ready (true) or unready (false).
     * The server may start the countdown when all players at a table are ready.
     */
    private boolean isReady;

    /**
     * Unique identifier of the table this snapshot describes.
     *
     * <p>Used by the client to:
     * <ul>
     *   <li>Verify table context and routing</li>
     *   <li>Associate subsequent updates with the correct table</li>
     *   <li>Detect stale or mismatched responses</li>
     * </ul>
     */
    private String tableId;

    /**
     * The player sending the request. Typically validated server-side using JWT.
     */
    private String playerId;
}
