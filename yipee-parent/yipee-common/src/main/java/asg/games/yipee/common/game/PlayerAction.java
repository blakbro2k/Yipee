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

import asg.games.yipee.common.enums.YipeeSerializable;
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
 *   <li>The target board affected (can be the same as the initiator)</li>
 *   <li>Optional payload data (e.g., block color or power info)</li>
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
        // ---------------------------------------------------------------------
        // Row Manipulation
        // ---------------------------------------------------------------------

        /**
         * Adds a row to a board.
         */
        Y_ADD_ROW,
        /** Removes a row from a board. */
        Y_REMOVE_ROW,

        // ---------------------------------------------------------------------
        // Color Manipulation
        // ---------------------------------------------------------------------

        /** Separates like colors to make matching more difficult. */
        A_DITHER,
        /** Groups similar colors together to make matching easier. */
        A_CLUMP,


        // ---------------------------------------------------------------------
        // Stone Manipulation
        // ---------------------------------------------------------------------

        /** Adds a stone to a board **/
        H_ADD_STONE,
        /** Drops a stone to the bottom of board **/
        H_DROP_STONE,

        // ---------------------------------------------------------------------
        // Block Manipulation
        // ---------------------------------------------------------------------

        /** Change the block color to a stone **/
        O_DEFUSE,
        /** Converts all the block's adjacent blocks **/
        O_COLOR_BLAST,

        // ---------------------------------------------------------------------
        // Special 3-piece blocks
        // ---------------------------------------------------------------------

        /** Adds the Medusa 3Piece as the board nextPiece **/
        O_MEDUSA,
        /** Adds the Midas 3Piece as the board nextPiece **/
        O_MIDAS,

        // ---------------------------------------------------------------------
        // Special Board Manipulation
        // ---------------------------------------------------------------------

        /** Adds half of powers queue to bottom of board **/
        I_REMOVE_POWERS,
        /** Removes all of a single color from board **/
        I_COLOR_REMOVE,

        // ---------------------------------------------------------------------
        // Special Power Blocks
        // ---------------------------------------------------------------------

        /** Increases the fall speed of the player’s board. */
        S_SPEED,

        /** Removes all powers currently stored on the board. */
        S_POWERS,

        /** Removes all stones from the player's board. */
        S_STONES,

        /** Adds a block to a user's board from a yahoo! **/
        YAHOO_ADD_BLOCK,

        // ---------------------------------------------------------------------
        // Player Input / Direct Control
        // ---------------------------------------------------------------------

        /** Move the active 3-piece block left. */
        P_MOVE_LEFT,

        /** Move the active 3-piece block right. */
        P_MOVE_RIGHT,

        /** Begin fast-dropping the active piece. */
        P_MOVE_DOWN_START,

        /** Stop fast-dropping the active piece. */
        P_MOVE_DOWN_END,

        /** Cycle the current 3-piece upward. */
        P_CYCLE_UP,

        /** Cycle the current 3-piece downward. */
        P_CYCLE_DOWN,

        // ---------------------------------------------------------------------
        // Targeted Attacks
        // ---------------------------------------------------------------------

        /** Attack a random enemy board. */
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

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /**
     * The board ID that originated this action.
     *
     * <p>This identifies which game board (0–7) created the action. The ID is used
     * server-side to validate ownership and apply proper prediction reconciliation.</p>
     */
    private final int initiatingBoardId;

    /**
     * The type of action being performed.
     */
    private final ActionType actionType;

    /**
     * The board ID that is the target of this action.
     *
     * <p>May equal {@code initiatingBoardId} for self-affecting actions or differ
     * for attacks and powers.</p>
     */
    private final int targetBoardId;

    /**
     * Optional payload attached to this action.
     *
     * <p>This may contain color data, block indexes, effect intensity, or any other
     * lightweight, GWT-safe value. The server determines how to interpret the payload
     * depending on {@link #actionType}.</p>
     */
    private Object actionData;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Creates a default no-op {@code PlayerAction}.
     *
     * <p>This is used by Kryo and GWT during deserialization and should not normally
     * be instantiated by gameplay logic.</p>
     */
    public PlayerAction() {
        this(-1, null, -2, null);
    }

    /**
     * Creates a fully-specified gameplay action.
     *
     * @param initiatingBoardId ID of the board/player issuing the action
     * @param actionType        the action type to perform
     * @param targetBoardId     the board affected by this action
     * @param actionData        optional payload with action-specific details
     */
    public PlayerAction(int initiatingBoardId, ActionType actionType,
                        int targetBoardId, Object actionData) {

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
