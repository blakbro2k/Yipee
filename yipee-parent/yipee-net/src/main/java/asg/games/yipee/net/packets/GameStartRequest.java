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

import asg.games.yipee.common.net.NetYipeePlayer;
import asg.games.yipee.common.net.NetYipeeTable;
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
     * The tick at which this request was sent. Used for debug or timing analysis.
     * Not used in actual gameplay simulation.
     */
    private int tick;

    /**
     * The table the player is attempting to join or mark ready in.
     */
    private NetYipeeTable table;

    /**
     * The player sending the request. Typically validated server-side using JWT.
     */
    private NetYipeePlayer player;
}
