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
 * Response sent by the server after processing a {@link ClientHandshakeRequest}.
 *
 * <p>This packet finalizes the initial authentication and session establishment
 * phase between the client and the Yipee server. A successful handshake results in:
 *
 * <ul>
 *   <li>a server-generated {@code sessionId},</li>
 *   <li>the authoritative {@code playerId},</li>
 *   <li>a confirmation flag indicating authentication success,</li>
 *   <li>an optional {@code apiToken} (JWT) used for authenticated REST access,</li>
 *   <li>standard metadata inherited from {@link AbstractServerResponse}.</li>
 * </ul>
 *
 * <h2>Session Model</h2>
 * <p>The {@code sessionId} binds the client instance (browser, desktop, etc.)
 * to a server-side {@code PlayerConnectionEntity}. All subsequent requests must
 * include this identifier (typically via the {@code X-Session-Id} header) or
 * present a valid JWT in the {@code Authorization} header.</p>
 *
 * <h2>JWT Authentication</h2>
 * <p>If provided, {@code apiToken} contains a server-minted JWT that allows
 * access to secured REST endpoints (e.g. {@code /api/player/whoami}). The token
 * is signed by the server and must be included as:</p>
 *
 * <pre>
 * Authorization: Bearer &lt;apiToken&gt;
 * </pre>
 *
 * <p>The server remains authoritative and may reject expired, tampered,
 * or client-mismatched tokens.</p>
 *
 * <h2>Direction</h2>
 * <p><b>Server → Client</b></p>
 *
 * @see ClientHandshakeRequest
 * @see AbstractServerResponse
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientHandshakeResponse extends AbstractServerResponse {

    /**
     * The authoritative identifier of the player as resolved by the server.
     *
     * <p>This value is determined by the server during handshake and may originate
     * from validated JWT claims or a trusted database lookup. The server always
     * decides the final value to prevent client-side identity spoofing.</p>
     */
    private String playerId;

    /**
     * Indicates whether the handshake completed successfully and the connection
     * is now authenticated and session-bound.
     *
     * <p>If {@code false}, the client should treat the handshake as failed and
     * must not issue further game-related requests until re-authenticated.</p>
     */
    private boolean connected;

    /**
     * Optional server-minted JWT used for authenticated REST calls.
     *
     * <p>This token encapsulates the player's identity and session context.
     * It must be supplied in the {@code Authorization} header when accessing
     * secured endpoints. The server validates signature, scope, expiration,
     * and client binding on each request.</p>
     *
     * <p>This value may be {@code null} if the server is operating in
     * session-only mode.</p>
     */
    private String apiToken;
}
