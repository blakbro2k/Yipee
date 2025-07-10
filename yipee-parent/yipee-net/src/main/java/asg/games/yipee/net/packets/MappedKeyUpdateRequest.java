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

import asg.games.yipee.objects.YipeeKeyMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Sent by the client to update or redefine their in-game key bindings.
 *
 * <p>Typically sent from a settings or input configuration screen, this request
 * allows players to customize controls for actions like movement, rotation, or
 * activating special abilities.</p>
 *
 * <p>The server may choose to:</p>
 * <ul>
 *   <li>Validate the mapping against allowed keys</li>
 *   <li>Persist the mapping for session or player profile</li>
 *   <li>Forward it to the game logic for real-time input handling</li>
 * </ul>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MappedKeyUpdateRequest extends AbstractClientRequest {

    /**
     * The full key configuration to apply, replacing the current binding.
     */
    private YipeeKeyMap keyConfig;
}
