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
 * Response sent by the server after processing a {@link ClientHandshakeRequest}.
 *
 * <p>This packet finalizes the authentication and session–establishment phase.
 * A successful handshake yields:
 *
 * <ul>
 *   <li>a server-generated {@code sessionId},</li>
 *   <li>the authoritative {@code playerId},</li>
 *   <li>a confirmation flag indicating successful authentication,</li>
 *   <li>standard response metadata from {@link AbstractServerResponse}.</li>
 * </ul>
 *
 * <p>The client must retain the {@code sessionId} and include it in all subsequent
 * game-related requests. The server will reject any request that lacks a valid
 * session identifier.</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientHandshakeResponse extends AbstractServerResponse {

    /**
     * The authoritative identifier of the player as resolved by the server.
     *
     * <p>This may originate from:
     * <ul>
     *   <li>claims within the {@code authToken} (preferred), or</li>
     *   <li>the client-provided {@code playerId}, after validation.</li>
     * </ul>
     *
     * <p>The server always decides the final value in order to ensure consistency
     * between Web CMS identity and in-game identity.</p>
     */
    private String playerId;

    /**
     * Indicates whether the handshake succeeded and the connection is now
     * considered authenticated and session-bound.
     *
     * <p>If {@code false}, the server will typically follow with an
     * {@link ErrorResponse}, and the client should not send further
     * game-related packets until a new handshake is attempted.</p>
     */
    private boolean connected;
}
