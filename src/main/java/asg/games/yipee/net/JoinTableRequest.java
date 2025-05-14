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
 * Sent by the client to request joining a specific table within a room or lobby.
 *
 * <p>This packet typically follows a successful handshake and is used to assign
 * a player to a table for either spectating or taking a seat.</p>
 *
 * <p>The server is responsible for validating the request, checking if the table exists,
 * if the player is allowed to join, and whether seats are available.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 *
 * @see JoinTableResponse
 */
@Data
@NoArgsConstructor
public class JoinTableRequest implements YipeeSerializable {

    /**
     * The unique identifier of the table the player is attempting to join.
     */
    private String tableId;

    /**
     * The player requesting to join the table.
     * Must be fully initialized with at least an ID and name.
     */
    private YipeePlayer player;
}
