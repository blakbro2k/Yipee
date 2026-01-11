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
package asg.games.yipee.net.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Network-serializable representation of a seat within a Yipee game table.
 *
 * <p>Seats serve as logical positions at a table and represent where a player may sit or
 * whether that position is currently occupied or ready. This interface abstracts away
 * platform-specific implementations (e.g., core entities vs. LibGDX UI objects) while
 * exposing the minimal information required for table synchronization.
 *
 * <p>Typical usage includes:
 * <ul>
 *     <li>Lobby/table displays</li>
 *     <li>Seat assignment during matchmaking</li>
 *     <li>Server–client synchronization of ready states</li>
 * </ul>
 *
 * ID-bearing contract across network transfers.
 */
@Data
@NoArgsConstructor
public class NetYipeeSeatDTO extends AbstractNetObjectDTO {
    private String seatedPlayerId;
    private int seatNumber;
    private String parentTableId;
}