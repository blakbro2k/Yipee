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
 * Server response containing a short-lived game launch authentication context.
 * <p>
 * This object is returned to the client after a successful request to obtain
 * a game launch token (e.g. {@code /api/game/getLaunchToken}). It provides the
 * minimal identity and session metadata required for the client to initiate
 * a secure game connection (WebSocket or KryoNet).
 * <p>
 * <strong>Security notes:</strong>
 * <ul>
 *   <li>This response does <em>not</em> represent an active game session.</li>
 *   <li>The associated launch token is intentionally short-lived and
 *       intended for one-time use during the initial game handshake.</li>
 *   <li>After a successful game handshake, the server binds player identity
 *       to the transport connection and no longer trusts client-supplied
 *       identity fields.</li>
 * </ul>
 *
 * <p>
 * Typical client flow:
 * <ol>
 *   <li>Client requests a game launch token.</li>
 *   <li>Server returns {@code GameAuthTokenResponse}.</li>
 *   <li>Client uses the embedded launch token to establish a game connection.</li>
 *   <li>Server validates the token and binds the connection to the player.</li>
 * </ol>
 *
 * <p>
 * Fields such as {@code playerId}, {@code clientId}, and {@code tableId} are
 * provided for client convenience and logging only; authoritative identity
 * validation occurs exclusively on the server during the game handshake.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameAuthTokenResponse extends AbstractServerResponse {

    /**
     * Unique identifier of the authenticated player requesting game launch.
     */
    public String playerId;

    /**
     * Display name of the player.
     * <p>
     * May be empty depending on authentication context or client requirements.
     */
    public String name;

    /**
     * Icon identifier selected by the player.
     */
    public int icon;

    /**
     * Current rating or skill value associated with the player.
     */
    public int rating;

    /**
     * Client identifier associated with the current session.
     */
    public String clientId;

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
     * The index of the seat that was requested.
     */
    private int seatIndex;

    /**
     * ISO-8601 timestamp indicating when the launch token expires.
     * <p>
     * Clients should treat this value as informational; expired tokens will
     * be rejected by the server during the game handshake.
     */
    public String expiresAt;
}