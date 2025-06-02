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
 * Sent by the client to select a seat at an already assigned table.
 *
 * <p>This request is typically issued after a successful handshake and table assignment
 * from the CMS (e.g., WordPress), allowing the game server to place the player into a
 * specific seat or register them as a spectator.</p>
 *
 * <p><b>Direction:</b> Client → Server</p>
 */
@Data
@NoArgsConstructor
public class SeatSelectionRequest extends AbstractClientRequest {

    /**
     * The ID of the table the player is assigned to.
     * This is typically issued by the web layer.
     */
    private String tableId;

    /**
     * The index of the seat being requested (0–7).
     * Ignored if {@code spectator} is true.
     */
    private int seatIndex;

    /**
     * Indicates whether the player is joining as a spectator.
     */
    private boolean spectator;

    /**
     * The player initiating the seat selection.
     */
    private YipeePlayer player;
}
