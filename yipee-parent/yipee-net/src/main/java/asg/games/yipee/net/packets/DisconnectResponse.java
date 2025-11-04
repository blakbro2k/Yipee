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
 * Response sent by the server to acknowledge that a client has disconnected.
 * <p>
 * Used to confirm that the session associated with a client has been terminated
 * cleanly, and may include metadata such as the final player profile.
 * </p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 *
 * @see DisconnectRequest
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"player"})
public class DisconnectResponse extends AbstractServerResponse {

    /**
     * The resolved player profile associated with this session.
     * Typically extracted from the provided JWT during handshake.
     */
    private NetYipeePlayer player;

    /**
     * Indicates whether the disconnection was processed successfully.
     */
    private boolean successful;
}
