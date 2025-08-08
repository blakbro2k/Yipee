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
 * Represents a discrete action initiated by a game board (typically a player),
 * which may affect the same or another game board.
 *
 * <p>Used during each game loop tick to drive gameplay events like attacks, power usage,
 * or movement. PlayerAction is the core event type exchanged between client and server
 * for both prediction and authoritative game state resolution.
 *
 * <p>Each action specifies:
 * <ul>
 *   <li>The board initiating the action</li>
 *   <li>The action type being performed</li>
 *   <li>The target board affected (may be the same as the initiator)</li>
 *   <li>Optional payload data (e.g., block color or power info)</li>
 *   <li>The tick and timestamp of execution</li>
 * </ul>
 *
 * <p>Compatible with KryoNet and GWT-safe serialization, making it suitable for real-time
 * multiplayer gameplay over Web or desktop clients.
 *
 * @author Blakbro2k
 */
@Getter
@Setter
@EqualsAndHashCode
public class PlayerAction implements YipeeSerializable {
    /**
     * Defines all supported in-game action types.
     *
     * <p>Grouped by categories:
     * <ul>
     *   <li><b>Y_*</b>: Row manipulation powers</li>
     *   <li><b>A_*</b>: Color reorganization powers</li>
     *   <li><b>H_*</b>: Stone-related powers</li>
     *   <li><b>O_*</b>: Offensive special powers</li>
     *   <li><b>I_*</b>: Item-based or board-clearing powers</li>
     *   <li><b>S_*</b>: Temporary boost powers</li>
     *   <li><b>P_*</b>: Player movement and attack targeting</li>
     *   <li><b>YAHOO_*</b>: Legacy Yahoo-style actions</li>
     * </ul>
     */
    public enum ActionType {
        // Row Manipulation
        /**
         * Adds a row to a board
         **/
        Y_ADD_ROW,
        /** Removes a row to a board **/
        Y_REMOVE_ROW,

        // Color Manipulation
        /** Separates like colors to make matches difficult **/
        A_DITHER,
        /** Combines like colors to make matches easier **/
        A_CLUMP,

        // Stone Powers
        /** Adds a stone to a board **/
        H_ADD_STONE,
        /** Drops a stone to the bottom of board **/
        H_DROP_STONE,

        // Block Manipulation
        /** Change the block color to a stone **/
        O_DEFUSE,
        /** Converts all the block's adjacent blocks **/
        O_COLOR_BLAST,

        // Special 3-piece blocks
        /** Adds the Medusa 3Piece as the board nextPiece **/
        O_MEDUSA,
        /** Adds the Midas 3Piece as the board nextPiece **/
        O_MIDAS,

        // Board Manipulation
        /** Adds half of powers queue to bottom of board **/
        I_REMOVE_POWERS,
        /** Removes all of a single color from board **/
        I_COLOR_REMOVE,

        /** Speeds up a user's board **/
        S_SPEED,
        /** Removes all powers from a user's board **/
        S_POWERS,
        /** Remove all stones from a user's board **/
        S_STONES,

        /** Adds a block to a user's board from a yahoo **/
        YAHOO_ADD_BLOCK,

        // Player Movement & Targeting
        /** Move the 3-piece block left. */
        P_MOVE_LEFT,
        /** Move the 3-piece block right. */
        P_MOVE_RIGHT,
        /** Start accelerating the 3-piece block downward. */
        P_MOVE_DOWN_START,
        /** Stop accelerating the 3-piece block downward. */
        P_MOVE_DOWN_END,
        /** Cycle the 3-piece block upward. */
        P_CYCLE_UP,
        /** Cycle the 3-piece block downward. */
        P_CYCLE_DOWN,
        /** Attack a random board. */
        P_ATTACK_RANDOM,
        /** Attack board in seat 1. */
        P_ATTACK_TARGET1,
        /** Attack board in seat 2. */
        P_ATTACK_TARGET2,
        /** Attack board in seat 3. */
        P_ATTACK_TARGET3,
        /** Attack board in seat 4. */
        P_ATTACK_TARGET4,
        /** Attack board in seat 5. */
        P_ATTACK_TARGET5,
        /** Attack board in seat 6. */
        P_ATTACK_TARGET6,
        /** Attack board in seat 7. */
        P_ATTACK_TARGET7,
        /** Attack board in seat 8. */
        P_ATTACK_TARGET8
    }

    /** The board ID that originated this action. */
    private final int initiatingBoardId;

    /** The type of action to execute. */
    private final ActionType actionType;

    /** The target board ID affected by this action (may match initiator for self-effects). */
    private final int targetBoardId;

    /** Optional action payload (e.g., block type, color, index). */
    private Object actionData;

    /** The game loop tick this action was created on. */
    private int tick;

    /** Millisecond timestamp for this action (used for animation sync or lag compensation). */
    private long timestamp;

    /**
     * Creates a default no-op PlayerAction.
     */
    public PlayerAction() {
        this(-1, null, -2, 1, null);
    }

    /**
     * Creates a new PlayerAction with the given parameters.
     *
     * @param initiatingBoardId the ID of the player/board that initiated the action
     * @param actionType        the type of action
     * @param targetBoardId     the ID of the board being targeted
     * @param tick              the game loop tick this occurred on
     * @param actionData        any extra payload needed for execution
     */
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
