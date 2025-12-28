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
 * Request sent by the client to inform the server that it is intentionally
 * disconnecting and no longer participating in the session.
 *
 * <p>Used during:
 * <ul>
 *   <li>clean app shutdown,</li>
 *   <li>logout or account switch,</li>
 *   <li>navigating away from the game screen,</li>
 *   <li>or pre-emptive shutdown before refreshing/reloading the client.</li>
 * </ul>
 *
 * <p>The server uses this message to:
 * <ul>
 *   <li>mark the session as closed,</li>
 *   <li>release table/seat reservations,</li>
 *   <li>persist disconnect state for analytics,</li>
 *   <li>remove pending input/action queues,</li>
 *   <li>and clean up player-specific resources within a {@code ServerGameManager}.</li>
 * </ul>
 *
 * <p>Clients are expected (but not required) to send this message before
 * terminating the underlying socket connection. Unexpected disconnects will
 * still be detected by KryoNet’s connection-monitoring logic, but without the
 * semantic clarity this request provides.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DisconnectRequest extends AbstractClientRequest {

    /**
     * The persistent identifier of the player who is disconnecting.
     *
     * <p>This is included for convenience and logging only. The server will
     * always resolve the authoritative player identity from the existing
     * session metadata rather than trusting the client-provided value.</p>
     */
    private String playerId;
}
