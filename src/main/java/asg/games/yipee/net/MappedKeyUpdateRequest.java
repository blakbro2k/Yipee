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

import asg.games.yipee.objects.YipeeKeyMap;
import asg.games.yipee.objects.YipeePlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent by the client to update or redefine a key binding for a specific in-game action.
 *
 * <p>This allows players to customize their control scheme (e.g., change the key
 * used to rotate a block or trigger a power-up). This packet is typically sent from
 * a settings menu or keybinding screen.</p>
 *
 * <p>The server may store this mapping for session persistence, enforce action permissions,
 * or forward it to the game engine for input handling.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
public class MappedKeyUpdateRequest implements YipeeSerializable {
    /**
     * The player submitting the key configuration update.
     */
    private YipeePlayer player;

    /**
     * The full replacement key configuration to be applied.
     */
    private YipeeKeyMap keyConfig;
}
