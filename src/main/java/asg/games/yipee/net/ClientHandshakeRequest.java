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
package asg.games.yipee.net;

import asg.games.yipee.objects.YipeePlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent from the client to the server during the initial handshake phase.
 *
 * <p>This packet carries authentication and identity details, allowing the server
 * to associate the connection with a known user or create a session from scratch.</p>
 *
 * <p>Typically used immediately after establishing a socket connection.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */

@Data
@NoArgsConstructor
public class ClientHandshakeRequest implements YipeeSerializable {
    /**
     * A unique identifier for the client instance (may be generated or assigned).
     */
    private String clientId;

    /**
     * A JWT token issued by the Web CMS (e.g., WordPress) for authentication.
     */
    private String authToken;

    /**
     * Optional player profile details. If not supplied, the server may extract
     * player identity from the auth token.
     */
    private YipeePlayer player;
}
