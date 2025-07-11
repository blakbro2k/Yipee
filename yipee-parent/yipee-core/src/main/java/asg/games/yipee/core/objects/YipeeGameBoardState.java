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
package asg.games.yipee.core.objects;

import asg.games.yipee.common.game.GameBoardState;
import asg.games.yipee.core.game.YipeeBlockEval;
import asg.games.yipee.core.game.YipeeGameBoard;
import asg.games.yipee.core.tools.RandomUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Queue;

/**
 * Represents a full snapshot of the state of a single game board in Yipee,
 * including active and next pieces, board contents, timers, animation state,
 * partner information, and debug metadata.
 *
 *
 * <p>Fields such as {@code playerCells}, {@code brokenCells}, {@code pieceFallTimer},
 * and {@code currentPhase} represent the real-time progression of the match and
 * are updated on each game loop tick.
 *
 * @see YipeeGameBoard
 * @see YipeeBlockEval
 */
// NOTE: This class must remain Kryo-serializable for network synchronization
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class YipeeGameBoardState extends AbstractYipeeObject implements GameBoardState {
    private static final Logger logger = LoggerFactory.getLogger(YipeeGameBoardState.class);

    /**
     * The current game phase (e.g. SPAWN_NEXT, COLLAPSING, etc.).
     */
    private YipeeGameBoard.GamePhase currentPhase;

    /**
     * The server-side timestamp when the game began for this board.
     */
    private long serverGameStartTime;

    /**
     * Timestamp of the current state (used for interpolation or tick validation).
     */
    private long currentStateTimeStamp;

    /**
     * Timestamp of the previous state for delta computation.
     */
    private long previousStateTimeStamp;

    /**
     * The currently falling piece controlled by the player.
     */
    private YipeePiece piece;

    /**
     * The next piece to be spawned after the current piece locks.
     */
    private YipeePiece nextPiece;

    /**
     * Clock object that tracks total elapsed time and pauses.
     */
    private YipeeClock gameClock;

    /**
     * The main board grid of the current player.
     */
    private int[][] playerCells;

    /**
     * The main board grid of the player's partner.
     */
    private int[][] partnerCells;

    /**
     * State reference to the partner board, for symmetrical rendering or logic.
     */
    private YipeeGameBoardState partnerBoard;

    /**
     * Blocks that have just broken and are waiting for animation.
     */
    private Queue<YipeeBrokenBlock> brokenCells;

    /**
     * List of blocks that need to fall downward due to breaks.
     */
    private Queue<YipeeBlockMove> cellsToDrop;

    /**
     * Queued power-up or attack actions available to the player.
     */
    private Queue<Integer> powers;

    /**
     * Number of rows queued for Yahoo! drop animation.
     */
    private int yahooDuration;

    /**
     * Timer tracking how long the current piece has been falling.
     */
    private float pieceFallTimer;

    /**
     * Timer tracking how long the current piece has been locked but not set.
     */
    private float pieceLockTimer;

    /**
     * Indicates whether the player's partner is to their right or left.
     */
    private boolean isPartnerRight;

    /**
     * Timer used to animate block collapse.
     */
    private float blockAnimationTimer;

    /**
     * Whether the current piece has locked into place.
     */
    private boolean isPieceSet;

    /**
     * Queue of special pieces coming up in the game.
     */
    private Queue<Integer> specialPieces;

    /** Number of break events by type. Used for scoring and power-ups. */
    private int[] countOfBreaks;

    /** Flag to enable extended debugging information in the output. */
    private boolean isDebug;

    /** Internal tracking for persistent power types. */
    private int[] powersKeep;

    /**
     * Internal random seed state for deterministic generation.
     */
    private boolean[] ids;

    /**
     * Pointer to current index of {@code ids} being used.
     */
    private int idIndex;

    /**
     * Shuffled column indexes for random piece drop locations.
     */
    private int[] randomColumnIndices;

    /**
     * Pre-generated list of upcoming piece values for this board.
     */
    private RandomUtil.RandomNumberArray nextBlocks;

    /**
     * Pointer to the current index in {@code nextBlocks}.
     */
    private int currentBlockPointer;

    /** Whether the player is currently holding the "fast drop" button. */
    private boolean fastDown;

    /**
     * Total number of blocks broken this tick.
     */
    private int brokenBlockCount;

    /**
     * Whether the game has officially started.
     */
    private boolean hasGameStarted;

    /** Debugging name or identifier for the board. */
    private String name;

    /**
     * The current tick count from the server.
     */
    private int tick;

    /**
     * The tableNumber that this board represents.  Used to help determine which partner this board is.
     */
    private int boardNumber;

    public void setCurrentStateTimeStamp(long currentStateTimeStamp) {
        setPreviousStateTimeStamp(currentStateTimeStamp);
        this.currentStateTimeStamp = currentStateTimeStamp;
    }

    // Print State
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("#################").append("\n");
        out.append("Board Number: ").append(boardNumber).append("\n");
        out.append("#################").append("\n");
        out.append("Server Game Start Time: ").append(getServerGameStartTime()).append("\n");
        out.append("Current Game Time: ").append(getCurrentStateTimeStamp()).append("\n");
        out.append("Previous Game Time: ").append(getPreviousStateTimeStamp()).append("\n");
        out.append("Piece Fall Out Timer: ").append(getPieceFallTimer()).append("\n");
        out.append("Fall Animation Timer: ").append(getBlockAnimationTimer()).append("\n");
        out.append("LockOut Timer: ").append(getPieceLockTimer()).append("\n");
        out.append("Yahoo Drop Count: ").append(getYahooDuration()).append("\n");
        out.append("#################").append("\n");
        out.append("Vector of broken cells: ").append(getBrokenCells()).append("\n");
        out.append("Cells to drop: ").append(getCellsToDrop()).append("\n");
        out.append("#################").append("\n");
        out.append("Current 3Piece: ").append(getPiece()).append("\n");
        out.append("Next 3Piece: ").append(getNextPiece()).append("\n");
        out.append("#################").append("\n");

        if (isDebug()) {
            out.append("Debug Info: ").append("\n");
            out.append("#################").append("\n");
            out.append("Broken Block Count [Y,A,H,O,0,!]): ").append(Arrays.toString(getCountOfBreaks())).append("\n");
            out.append("Powers Break Count [Y,A,H,O,0,!]): ").append(Arrays.toString(getPowersKeep())).append("\n");
            out.append("Boolean Ids: ").append(Arrays.toString(getIds())).append("\n");
            out.append("idIndex: ").append(getIdIndex()).append("\n");
            out.append("#################").append("\n");
        }

        if (piece != null) {
            out.append("player piece pos(").append(piece.column).append(",").append(piece.row).append(")").append("\n");
        }

        addPrintLine(out);
        for (int r = YipeeGameBoard.MAX_ROWS - 1; r > -1; r--) {
            printRow(out, r);
            printRowReturn(out);
        }
        addPrintLine(out);
        printRowReturn(out);
        out.append("#################").append("\n");
        return out.toString();
    }

    private int[][] getPartnerCells(YipeeGameBoardState partnerBoardState) {
        if (partnerBoardState != null) {
            partnerCells = partnerBoardState.getPlayerCells();
        }
        return partnerCells;
    }

    private void printRow(StringBuilder out, int r) {
        if (isPartnerRight) {
            printPlayerRows(playerCells, getPartnerCells(partnerBoard), r, out);
        } else {
            printPlayerRows(getPartnerCells(partnerBoard), playerCells, r, out);
        }
    }

    private void printPlayerRows(int[][] cellsLeft, int[][] cellsRight, int r, StringBuilder out) {
        for (int c = 0; c < YipeeGameBoard.MAX_COLS * 2; c++) {
            int block;
            if (c == YipeeGameBoard.MAX_COLS) {
                out.append('|');
            }
            if (c < YipeeGameBoard.MAX_COLS) {
                block = isPieceBlock(r, c) && isPartnerRight ? getPieceBlock(r) : getPieceValue(cellsLeft, c, r);
                printGameLine(out, block);
            } else {
                block = isPieceBlock(r, c - YipeeGameBoard.MAX_COLS) && !isPartnerRight ? getPieceBlock(r) : getPieceValue(cellsRight, c - YipeeGameBoard.MAX_COLS, r);
                printGameLine(out, block);
            }
        }
        out.append('|');
    }

    private void printGameLine(StringBuilder out, int block) {
        if (block == YipeeBlock.CLEAR_BLOCK) {
            out.append('|').append(' ');
        } else {
            if (YipeeBlockEval.hasPowerBlockFlag(block)) {
                out.append('|').append(YipeeBlockEval.getPowerLabel(block));
            } else {
                out.append('|').append(YipeeBlockEval.getNormalLabel(block));
            }
        }
    }

    private void addPrintLine(StringBuilder sb) {
        for (int a = 0; a < YipeeGameBoard.MAX_COLS * 2; a++) {
            sb.append("+");
            if (a == YipeeGameBoard.MAX_COLS) {
                sb.append("+");
            }
            sb.append("-");
        }
        sb.append('+').append('\n');
    }

    private void printRowReturn(StringBuilder out) {
        out.append("\n");
    }

    private boolean isPieceBlock(int row, int col) {
        return piece != null && piece.column == col && (piece.row == row || piece.row + 1 == row || piece.row + 2 == row);
    }

    private int getPieceBlock(int row) {
        return piece.getValueAt(Math.abs(2 - (row - piece.row)));
    }

    private int getPieceValue(int[][] cells, int c, int r) {
        return YipeeBlockEval.getCellFlag(cells[r][c]);
    }
}
