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
package asg.games.yipee.common.game;

/**
 * Represents the current phase of the Yipee board's update cycle.
 *
 * <p>Each phase corresponds to a specific portion of the gameplay loop during which
 * the server or client performs deterministic logic such as spawning pieces, applying
 * gravity, locking blocks, handling break events, or animating cascades.</p>
 *
 * <p>Both server authoritative logic and client-side prediction rely on
 * {@code GamePhase} to interpret and animate state transitions consistently.</p>
 */
public enum GamePhase {

    /**
     * A new piece is being spawned onto the board.
     *
     * <p>During this phase, the active block is selected (using the deterministic
     * {@link CommonRandomNumberArray}) and placed at the
     * spawn position. The board then transitions to the {@link #FALLING} phase.</p>
     */
    SPAWN_NEXT,

    /**
     * The active piece is currently falling due to gravity.
     *
     * <p>Timers such as the piece fall timer are active during this phase. Player input
     * (left, right, rotate, fast-drop) is accepted and applied.</p>
     */
    FALLING,

    /**
     * The active piece has touched a surface and is waiting to lock into place.
     *
     * <p>During this phase, the lock timer is counting down. Movement is still allowed
     * until the lock delay expires—after which the piece becomes part of the board.</p>
     */
    LOCKING,

    /**
     * One or more groups of blocks are breaking as the result of a match.
     *
     * <p>The board may apply scoring rules, power triggers, or animation timers before
     * transitioning to {@link #COLLAPSING}.</p>
     */
    BREAKING,

    /**
     * Block fragments or remaining cells are falling down to fill empty spaces.
     *
     * <p>This phase handles gravity-based collapse animations following a break event.
     * Once all blocks settle, the board transitions to {@link #CASCADE_CHECK}.</p>
     */
    COLLAPSING,

    /**
     * The board checks whether any new breaks or matches have formed after the collapse.
     *
     * <p>If additional breaks are found, the state returns to {@link #BREAKING};
     * otherwise, a new piece is spawned through {@link #SPAWN_NEXT}.</p>
     */
    CASCADE_CHECK,

    /**
     * The game has ended for this board.
     *
     * <p>This phase indicates that overflow has occurred or a win/loss condition has been met.
     * No further input is accepted for this board aside from UI transitions.</p>
     */
    GAME_OVER
}
