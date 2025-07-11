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

import asg.games.yipee.core.objects.YipeePlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Sent by the client to notify the server it is disconnecting from the session.
 * <p>
 * Typically used during a clean shutdown (e.g., logout, app exit), giving the server
 * a chance to clean up the session, release resources, or persist disconnection events.
 * </p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 *
 * @see ClientHandshakeRequest
 * @see ClientHandshakeResponse
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DisconnectRequest extends AbstractClientRequest {

    /**
     * The player associated with the current session.
     * Used for cleanup, notification, or session teardown.
     */
    private YipeePlayer player;
}
