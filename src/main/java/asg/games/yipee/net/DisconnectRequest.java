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
package asg.games.yipee.net;

import asg.games.yipee.objects.YipeePlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent by the client to notify the server that it is disconnecting from the session.
 *
 * <p>This packet is typically sent during a clean shutdown (e.g., app exit, logout)
 * and gives the server an opportunity to clean up resources, update session
 * status, and persist disconnection events.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 *
 * @see ClientHandshakeRequest
 * @see ClientHandshakeResponse
 */
@Data
@NoArgsConstructor
public class DisconnectRequest implements YipeeSerializable {

    /**
     * The unique identifier of the client that is disconnecting.
     * This may be used to match against the original handshake or connection record.
     */
    private String clientId;

    /**
     * The session ID associated with this connection.
     * Helps the server identify which active session to close or release.
     */
    private String sessionId;

    /**
     * The client-side timestamp of the disconnection event.
     * Useful for analytics or last-seen tracking.
     */
    private long timeStamp;

    /**
     * The player associated with this session.
     * Used for state cleanup, notifications, or removal from the game world.
     */
    private YipeePlayer player;
}
