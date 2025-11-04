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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Sent from the client to the server during the initial handshake phase.
 * <p>
 * This request carries authentication and identity details, allowing the server
 * to associate the connection with a known user or create a session.
 * </p>
 * <p>
 * Typically sent immediately after establishing a socket connection.
 * </p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"player"})
public class ClientHandshakeRequest extends AbstractClientRequest {
    /**
     * Optional player profile details. If omitted, the server will extract
     * the player identity from the provided auth token.
     */
    private NetYipeePlayer player;
}
