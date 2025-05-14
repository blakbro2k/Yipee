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

import asg.games.yipee.game.PlayerAction;
import asg.games.yipee.objects.YipeePlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent by a player during gameplay to perform an in-game action
 * such as rotate, drop, attack, or trigger a special move.
 * <p>
 * The server processes these actions deterministically during each game tick.
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
public class PlayerActionPacket implements YipeeSerializable {

    /**
     * The player who initiated the action.
     */
    private YipeePlayer player;

    /**
     * Optional string describing the action type (e.g. "ROTATE", "ATTACK").
     * Consider removing this if PlayerAction already contains it.
     */
    private String actionType;

    /**
     * The index of the game board the action is targeting.
     * Used for team-based actions or attacks.
     */
    private int targetBoardIndex;

    /**
     * The full PlayerAction object containing details like move direction,
     * attack strength, combo multiplier, etc.
     */
    private PlayerAction playerAction;
}
