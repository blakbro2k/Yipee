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
package asg.games.yipee.common.dto;

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
 * <p>All implementations must also implement {@link NetYipeeObject}, ensuring a consistent
 * ID-bearing contract across network transfers.
 */
public interface NetYipeeSeat extends NetYipeeObject {
    /**
     * Returns the index of this seat within its table.
     *
     * <p>Seat numbering is zero-based and typically ranges from {@code 0} to
     * {@code MAX_SEATS - 1}. The index determines which player positions form
     * pairs or opposing teams within the game's table layout.
     *
     * @return the zero-based seat number
     */
    int getSeatNumber();

    /**
     * Indicates whether this seat is currently marked as "ready" for game start.
     *
     * <p>A seat is usually set ready when a player has confirmed they are prepared
     * to begin the match. This flag is used by table logic such as
     * {@code isTableStartReady()} to determine if enough players are ready.
     *
     * @return {@code true} if the seat is marked ready; {@code false} otherwise
     */
    boolean isSeatReady();
}
