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

import asg.games.yipee.common.enums.ACCESS_TYPE;

/**
 * Network-facing representation of a Yipee game table.
 *
 * <p>This interface enables polymorphic serialization of platform-specific table
 * implementations (e.g., core or LibGDX) within the networking layer, without introducing
 * cross-platform dependencies or GWT-incompatible types.
 *
 * <p>Primarily used in multiplayer synchronization, matchmaking, and table-state exchange
 * between server and client.
 *
 * @param <S> seat type, implementing {@link NetYipeeSeat}
 * @param <W> watcher/player type, implementing {@link NetYipeePlayer}
 */
public interface NetYipeeTable<
    S extends NetYipeeSeat,
    W extends NetYipeePlayer>
    extends NetYipeeObject {

    /**
     * Sets the access type (visibility/availability) of this table.
     *
     * @param accessType the {@link ACCESS_TYPE} to assign
     */
    void setAccessType(ACCESS_TYPE accessType);

    /**
     * Returns the access type (visibility/availability) of this table.
     *
     * @return the {@link ACCESS_TYPE}
     */
    ACCESS_TYPE getAccessType();

    /**
     * Sets the collection of watchers (non-seated players) observing this table.
     *
     * <p>The provided iterable will typically be copied into an internal collection
     * (e.g., {@code Set} on the server or {@code ObjectSet} on the client).
     *
     * @param watchers iterable of watcher instances
     */
    void setWatchers(Iterable<W> watchers);

    /**
     * Returns the collection of watchers (non-seated players) observing this table.
     *
     * @return iterable of watcher instances
     */
    Iterable<W> getWatchers();

    /**
     * Sets the collection of seats associated with this table.
     *
     * <p>The provided iterable will typically be copied into an internal collection
     * (e.g., {@code Set} on the server or {@code ObjectSet} on the client).
     *
     * @param seats iterable of seat instances
     */
    void setSeats(Iterable<S> seats);

    /**
     * Returns the collection of seats associated with this table.
     *
     * @return iterable of seat instances
     */
    Iterable<S> getSeats();

    /**
     * Sets the numeric table identifier within a room or lobby.
     *
     * @param tableNumber the table number to assign
     */
    void setTableNumber(int tableNumber);

    /**
     * Returns the numeric table identifier within a room or lobby.
     *
     * @return the table number
     */
    int getTableNumber();

    /**
     * Enables or disables rated mode for this table.
     *
     * @param rating {@code true} if the table is rated, {@code false} otherwise
     */
    void setRated(boolean rating);

    /**
     * Indicates whether this table is in rated mode.
     *
     * @return {@code true} if rated, {@code false} otherwise
     */
    boolean isRated();

    /**
     * Enables or disables sound for this table.
     *
     * @param soundOn {@code true} to enable sound, {@code false} to disable
     */
    void setSoundOn(boolean soundOn);

    /**
     * Indicates whether sound is currently enabled for this table.
     *
     * @return {@code true} if sound is on, {@code false} otherwise
     */
    boolean isSoundOn();
}
