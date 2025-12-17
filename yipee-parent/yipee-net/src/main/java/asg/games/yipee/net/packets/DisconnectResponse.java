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
 * Response sent by the server to acknowledge and finalize a client's
 * disconnection request.
 *
 * <p>This message confirms that the server has:
 * <ul>
 *   <li>closed the session associated with the request,</li>
 *   <li>released table/seat reservations (if any),</li>
 *   <li>persisted disconnection metadata for the player, and</li>
 *   <li>cleaned up server-side resources linked to the client.</li>
 * </ul>
 *
 * <p>The response is mainly informational for the client. In most cases,
 * the client may already be in shutdown state but may still be listening
 * for this final confirmation.</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 *
 * @see DisconnectRequest
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DisconnectResponse extends AbstractServerResponse {

    /**
     * The persistent identifier of the player whose session was closed.
     *
     * <p>This value is provided for logging and UI feedback and reflects the
     * authoritative identity already known to the server. The server does not
     * rely on the client-sent value for trust or validation.</p>
     */
    private String playerId;

    /**
     * Indicates whether the disconnection was processed successfully by the server.
     *
     * <p>A value of {@code true} means the server:
     * <ul>
     *   <li>recognized the session,</li>
     *   <li>terminated it cleanly,</li>
     *   <li>and persisted any required metadata.</li>
     * </ul>
     *
     * <p>A value of {@code false} may occur if:
     * <ul>
     *   <li>the session was already expired,</li>
     *   <li>no active session existed,</li>
     *   <li>or the request failed authorization.</li>
     * </ul>
     * <p>This does not necessarily indicate an error — it simply reflects that
     * there was no active session to close.</p>
     */
    private boolean successful;
}
