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

import asg.games.yipee.common.dto.NetYipeePlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Snapshot of the current state of a game table.
 *
 * <p>This response provides clients with an authoritative, point-in-time
 * view of a table's occupancy and observers. It is typically sent:
 * <ul>
 *   <li>When a client first joins or reconnects to a table</li>
 *   <li>When table membership changes (seat or watcher updates)</li>
 *   <li>During lobby or pre-game synchronization</li>
 * </ul>
 *
 * <p>This packet is <b>not</b> used to start gameplay and contains no
 * deterministic game data. Gameplay initialization and timing are
 * communicated separately via {@link GameStartResponse}.</p>
 *
 * <p><b>Direction:</b> Server → Client</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TableDetailsResponse extends AbstractServerResponse {

    /**
     * Unique identifier of the table this snapshot describes.
     *
     * <p>Used by the client to:
     * <ul>
     *   <li>Verify table context and routing</li>
     *   <li>Associate subsequent updates with the correct table</li>
     *   <li>Detect stale or mismatched responses</li>
     * </ul>
     */
    private String tableId;

    /**
     * Current state of all seats at the table.
     *
     * <p>Each entry describes a seat's occupancy and readiness state,
     * including which player (if any) is seated there.</p>
     */
    public List<SeatStateUpdateResponse> seats;

    /**
     * List of players currently observing the table without occupying a seat.
     *
     * <p>Watchers may receive table updates but are not allowed to
     * participate in gameplay actions.</p>
     */
    public List<NetYipeePlayer> watchers;
}