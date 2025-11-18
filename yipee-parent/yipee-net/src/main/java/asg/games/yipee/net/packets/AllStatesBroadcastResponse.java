/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.net.packets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Broadcast packet containing the latest full-state snapshots for all
 * eight seats at a Yipee table.
 *
 * <p>This message is emitted by the server when it needs to deliver a
 * synchronized, table-wide view of game state to all connected clients.
 * It is typically used during:
 *
 * <ul>
 *   <li>initial table sync after a player joins,</li>
 *   <li>recovery after a network hiccup,</li>
 *   <li>full-state reconciliation after desync,</li>
 *   <li>or periodic broadcast in lower-latency modes.</li>
 * </ul>
 *
 * <p>The server includes one {@link TableStateBroadcastResponse} per seat,
 * indexed consistently from 1 through 8. Any seat not currently occupied
 * may contain a null entry.
 *
 * <p><b>Direction:</b> Server → Clients
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllStatesBroadcastResponse extends AbstractServerResponse {

    /**
     * State snapshot for board/seat #1. May be null if unoccupied.
     */
    private TableStateBroadcastResponse tableState1;

    /** State snapshot for board/seat #2. May be null if unoccupied. */
    private TableStateBroadcastResponse tableState2;

    /** State snapshot for board/seat #3. May be null if unoccupied. */
    private TableStateBroadcastResponse tableState3;

    /** State snapshot for board/seat #4. May be null if unoccupied. */
    private TableStateBroadcastResponse tableState4;

    /** State snapshot for board/seat #5. May be null if unoccupied. */
    private TableStateBroadcastResponse tableState5;

    /** State snapshot for board/seat #6. May be null if unoccupied. */
    private TableStateBroadcastResponse tableState6;

    /** State snapshot for board/seat #7. May be null if unoccupied. */
    private TableStateBroadcastResponse tableState7;

    /** State snapshot for board/seat #8. May be null if unoccupied. */
    private TableStateBroadcastResponse tableState8;
}
