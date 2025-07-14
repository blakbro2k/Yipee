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
package asg.games.yipee.common.packets;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an action initiated by a game board (player), targeting another board.
 * Used during game ticks for attacks, bonuses, or system-triggered effects.
 */
@Getter
@Setter
@EqualsAndHashCode
public class PlayerAction implements YipeeSerializable {
    /**
     * Enum of supported in-game actions categorized by:
     * - Y = Row powers
     * - A = Color block matching powers
     * - H = Stone powers
     * - O = Offensive powers
     * - 0 = Special block powers
     * - ! = board and powers manipulation powers
     * - S = Special powers
     */
    public enum ActionType {
        /**
         * Adds a row to a board
         **/
        Y_ADD_ROW,
        /** Removes a row to a board **/
        Y_REMOVE_ROW,

        /** Separates like colors to make matches difficult **/
        A_DITHER,
        /** Combines like colors to make matches easier **/
        A_CLUMP,

        /** Adds a stone to a board **/
        H_ADD_STONE,
        /** Drops a stone to the bottom of board **/
        H_DROP_STONE,

        /** Change the block color to a stone **/
        O_DEFUSE,
        /** Converts all the block's adjacent blocks **/
        O_COLOR_BLAST,

        /** Adds the Medusa 3Piece as the board nextPiece **/
        O_MEDUSA,
        /** Adds the Midas 3Piece as the board nextPiece **/
        O_MIDAS,

        /** Adds half of powers queue to bottom of board **/
        I_REMOVE_POWERS,
        /** Removes all of a single color from board **/
        I_COLOR_REMOVE,

        S_SPEED,
        S_POWERS,
        S_STONES,

        YAHOO_ADD_BLOCK,

        P_MOVE_LEFT,
        P_MOVE_RIGHT,
        P_MOVE_DOWN_START,
        P_MOVE_DOWN_END,
        P_CYCLE_UP,
        P_CYCLE_DOWN,
        P_ATTACK_RANDOM,
        P_ATTACK_TARGET1,
        P_ATTACK_TARGET2,
        P_ATTACK_TARGET3,
        P_ATTACK_TARGET4,
        P_ATTACK_TARGET5,
        P_ATTACK_TARGET6,
        P_ATTACK_TARGET7,
        P_ATTACK_TARGET8
    }

    /**
     * The board that initiated the action.
     */
    private final int initiatingBoardId;

    /**
     * The type of action performed.
     */
    private final ActionType actionType;

    /**
     * The board being targeted by this action (may be self).
     */
    private final int targetBoardId;

    /**
     * Optional payload for the action (e.g., block type, color, power level).
     */
    private Object actionData;

    /**
     * Game loop tick when this action was created.
     */
    private int tick;

    /**
     * Timestamp when this action was created.
     */
    private long timestamp;

    public PlayerAction() {
        this(-1, null, -2, 1, null);
    }

    public PlayerAction(int initiatingBoardId, ActionType actionType, int targetBoardId, int tick, Object actionData) {
        this.initiatingBoardId = initiatingBoardId;
        this.actionType = actionType;
        this.targetBoardId = targetBoardId;
        this.actionData = actionData;
        this.tick = tick;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
            "initiatingBoardId=" + initiatingBoardId +
            ", actionType=" + actionType +
            ", targetBoardId=" + targetBoardId +
            ", actionData=" + actionData +
            ", tick=" + tick +
            ", timestamp=" + timestamp +
            '}';
    }
}
