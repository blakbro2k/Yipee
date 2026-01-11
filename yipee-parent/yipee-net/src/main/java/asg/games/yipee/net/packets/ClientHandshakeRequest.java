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
 * Initial request sent by the client immediately after establishing a network
 * connection to the Yipee game server.
 *
 * <p>This packet begins the authentication and session–establishment workflow.
 * It contains:
 *
 * <ul>
 *   <li>{@code clientId} – locally generated stable identifier for the client device.</li>
 *   <li>{@code authToken} – identity token issued by WordPress/IdP.</li>
 *   <li>{@code playerId} – the player's persistent ID as known by the CMS.</li>
 *   <li>{@code timestamp/clientTick} – diagnostic information from the base class.</li>
 * </ul>
 *
 * <p>Upon receiving this request, the server will:
 * <ol>
 *   <li>Validate the {@code authToken},</li>
 *   <li>Resolve the {@code playerId} (or override it using token claims),</li>
 *   <li>Create a new server-owned {@code sessionId},</li>
 *   <li>Persist or update the player's connection record,</li>
 *   <li>Respond with a {@link ClientHandshakeResponse} acknowledging the session.</li>
 * </ol>
 *
 * <p>This packet does <b>not</b> represent joining a room or table. It only
 * authorizes the connection and establishes a valid session.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientHandshakeRequest extends AbstractClientRequest {

    /**
     * Persistent identifier of the player, as assigned by the external
     * identity provider or CMS (e.g., WordPress user ID).
     *
     * <p>The server may use this field directly, or it may derive/validate
     * the authoritative player identity from the {@code authToken} instead.</p>
     */
    private String playerId;
}
