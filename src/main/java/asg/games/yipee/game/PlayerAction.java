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
package asg.games.yipee.game;

import asg.games.yipee.net.YipeeSerializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an action initiated by a game board (player), targeting another board.
 * Used during game ticks for attacks, bonuses, or system-triggered effects.
 */
@Getter
@Setter
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
        Y_ADD_ROW,
        Y_REMOVE_ROW,

        A_DITHER,
        A_CLUMP,

        H_ADD_STONE,
        H_DROP_STONE,

        O_DEFUSE,
        O_COLOR_BLAST,

        O_MEDUSA,
        O_MIDAS,

        I_REMOVE_POWERS,
        I_COLOR_REMOVE,

        S_SPEED,
        S_POWERS,
        S_STONES,

        YAHOO_ADD_BLOCK
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

    public PlayerAction() {
        this(-1, null, -2, null);
    }

    public PlayerAction(int initiatingBoardId, ActionType actionType, int targetBoardId, Object actionData) {
        this.initiatingBoardId = initiatingBoardId;
        this.actionType = actionType;
        this.targetBoardId = targetBoardId;
        this.actionData = actionData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PlayerAction that = (PlayerAction) obj;

        return initiatingBoardId == that.initiatingBoardId &&
            targetBoardId == that.targetBoardId &&
            actionType == that.actionType && // assuming this is an enum
            (actionData == null ? that.actionData == null : actionData.equals(that.actionData));
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(initiatingBoardId);
        result = 31 * result + Integer.hashCode(targetBoardId);
        result = 31 * result + (actionType != null ? actionType.hashCode() : 0);
        result = 31 * result + (actionData != null ? actionData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
            "initiatingBoardId=" + initiatingBoardId +
            ", actionType=" + actionType +
            ", targetBoardId=" + targetBoardId +
            ", actionData=" + actionData +
            '}';
    }
}
