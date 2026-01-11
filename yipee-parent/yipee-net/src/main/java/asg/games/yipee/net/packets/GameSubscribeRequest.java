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
 * Client request to subscribe the current connection to a game's
 * state update stream.
 * <p>
 * This request is sent after a successful handshake/bind step,
 * once the server has associated the WebSocket connection with
 * a validated session, player, and table.
 * </p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *   <li>Registers the client as an active subscriber for game state updates</li>
 *   <li>Enables the server to push {@code TableStateUpdateResponse} packets
 *       to this connection during the game loop</li>
 * </ul>
 *
 * <h3>Authorization</h3>
 * <ul>
 *   <li>The server MUST verify that the {@code clientId} matches the
 *       authenticated session bound to the WebSocket</li>
 *   <li>The client is only allowed to subscribe to the table/game
 *       associated with its session</li>
 * </ul>
 *
 * <h3>MVP Notes</h3>
 * <ul>
 *   <li>For alpha, subscriptions are typically keyed by {@code tableId}
 *       (resolved from the session), not directly by {@code gameId}</li>
 *   <li>Duplicate subscriptions are ignored or treated as idempotent</li>
 * </ul>
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameSubscribeRequest extends AbstractClientRequest {

    /**
     * Identifier for the client instance issuing this request.
     * <p>
     * This is used for validation and debugging only; authoritative
     * identity is derived from the bound session.
     * </p>
     */
    private String clientId;
}
