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
 * Response packet sent by the server after a successful client handshake.
 *
 * <p>This packet finalizes the connection process by issuing a unique session ID
 * to the client, confirming connection status, and optionally including the
 * resolved {@link YipeePlayer} identity.</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 *
 * @see ClientHandshakeRequest
 */
@Data
@NoArgsConstructor
public class ClientHandshakeResponse implements YipeeSerializable {

    /**
     * The unique identifier of the server instance handling the connection.
     * Useful for debugging or load-balanced setups.
     */
    private String serverId;

    /**
     * The server-side timestamp of when the handshake was processed.
     * Can be used for latency estimation or session timeout validation.
     */
    private long timeStamp;

    /**
     * A unique session ID assigned to this client connection.
     * Clients should retain and include this in all future requests.
     */
    private String sessionId;

    /**
     * The resolved player profile for this connection.
     * May be extracted from the JWT or passed directly in the handshake.
     */
    private YipeePlayer player;

    /**
     * Whether the handshake was accepted and the client is now connected.
     */
    private boolean connected;
}
