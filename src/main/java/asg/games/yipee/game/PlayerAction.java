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
package asg.games.yipee.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerAction {
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
        YAHOO_ADD_BLOCK;
    }

    private final int initiatingBoardId; // The ID of the board that initiated the action
    private final ActionType actionType; // The type of action being performed
    private final int targetBoardId; // The ID of the target board (can be self for defensive actions)
    private Object actionData; // Additional data related to the action (e.g., attack strength, bonus details)
    private int attackValue = -1;

    public PlayerAction(int initiatingBoardId, ActionType actionType, int targetBoardId, Object actionData) {
        this.initiatingBoardId = initiatingBoardId;
        this.actionType = actionType;
        this.targetBoardId = targetBoardId;
        this.actionData = actionData;
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