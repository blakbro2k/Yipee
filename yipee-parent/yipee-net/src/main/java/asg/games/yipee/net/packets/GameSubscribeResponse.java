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
 * Server response acknowledging a successful game subscription.
 * <p>
 * This response confirms that the client connection has been
 * registered to receive live game state updates.
 * </p>
 *
 * <h2>Semantics</h2>
 * <ul>
 *   <li>After receiving this response, the client should expect
 *       server-initiated state broadcasts</li>
 *   <li>The response does not start the game; it only establishes
 *       the subscription</li>
 * </ul>
 *
 * <h3>MVP Notes</h3>
 * <ul>
 *   <li>For alpha, this response may be purely informational</li>
 *   <li>Future versions may include:
 *     <ul>
 *       <li>Current active {@code gameId}</li>
 *       <li>Initial snapshot of table/game state</li>
 *       <li>Subscription role (player vs watcher)</li>
 *     </ul>
 *   </li>
 * </ul>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameSubscribeResponse extends AbstractServerResponse {

    /**
     * Identifier for the client instance that was successfully subscribed.
     */
    private String clientId;
}
