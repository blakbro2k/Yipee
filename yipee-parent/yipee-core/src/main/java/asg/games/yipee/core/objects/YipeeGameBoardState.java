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
package asg.games.yipee.core.objects;

import asg.games.yipee.common.enums.Copyable;
import asg.games.yipee.common.game.CommonRandomNumberArray;
import asg.games.yipee.common.game.GameBoardState;
import asg.games.yipee.common.game.GamePhase;
import asg.games.yipee.common.tools.StaticArrayUtils;
import asg.games.yipee.core.game.YipeeBlockEval;
import asg.games.yipee.core.game.YipeeGameBoard;
import asg.games.yipee.core.tools.Util;
import asg.games.yipee.core.tools.YipeePrinter;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a full snapshot of the state of a single game board in Yipee,
 * including active and next pieces, board contents, timers, animation state,
 * partner cells, and debug metadata.
 *
 * <p>Fields such as {@code playerCells}, {@code brokenCells}, {@code pieceFallTimer},
 * and {@code currentPhase} represent the real-time progression of the match and
 * are updated on each game loop tick.
 *
 * @see YipeeGameBoard
 * @see YipeeBlockEval
 */
// NOTE: This class must remain Kryo-serializable for network synchronization
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class YipeeGameBoardState extends AbstractYipeeObject implements GameBoardState, Copyable<YipeeGameBoardState> {
    private static final Logger logger = LoggerFactory.getLogger(YipeeGameBoardState.class);

    /**
     * The current game phase (e.g. SPAWN_NEXT, COLLAPSING, etc.).
     */
    private GamePhase currentPhase;

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
    private String piece;

    /**
     * The next piece to be spawned after the current piece locks.
     */
    private String nextPiece;

    /**
     * Clock object that tracks total elapsed time and pauses.
     */
    private String gameClock;

    /**
     * The main board grid of the current player.
     */
    private int[][] playerCells;

    /**
     * The main board grid of the player's partner.
     */
    private int[][] partnerCells;

    /**
     * Blocks that have just broken and are waiting for animation.
     */
    private Queue<YipeeBrokenBlock> brokenCells = new LinkedList<>();

    /**
     * List of blocks that need to fall downward due to breaks.
     */
    private Queue<YipeeBlockMove> cellsToDrop = new LinkedList<>();

    /**
     * Queued power-up or attack actions available to the player.
     */
    private Queue<Integer> powers = new LinkedList<>();

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
    private Queue<Integer> specialPieces = new LinkedList<>();

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
    private CommonRandomNumberArray nextBlocks;

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

    @Override
    public void setPowers(Iterable<Integer> powers) {
        this.powers = Util.iterableToLinkeListQueue(powers);
    }

    @Override
    public void setBrokenCells(Object brokenCells) {

    }

    @Override
    public void setSpecialPieces(Iterable<Integer> specialPieces) {
        this.specialPieces = Util.iterableToLinkeListQueue(specialPieces);
    }

    public Iterable<Object> getCellsToDrop() {
        return null;
    }


    // Print State
    public String toString() {
        try {
            return YipeePrinter.getYipeeBoardStateString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public YipeeGameBoardState copy() {
        YipeeGameBoardState copy = new YipeeGameBoardState();

        copyParent(copy);
        copy.currentPhase = this.currentPhase;
        copy.serverGameStartTime = this.serverGameStartTime;
        copy.currentStateTimeStamp = this.currentStateTimeStamp;
        copy.previousStateTimeStamp = this.previousStateTimeStamp;
        copy.piece = this.piece;
        copy.nextPiece = this.nextPiece;
        copy.gameClock = this.gameClock;
        copy.playerCells = this.playerCells;
        copy.partnerCells = this.partnerCells;
        copy.brokenCells = this.brokenCells;
        copy.cellsToDrop = this.cellsToDrop;
        copy.powers = this.powers;
        copy.yahooDuration = this.yahooDuration;
        copy.pieceFallTimer = this.pieceFallTimer;
        copy.pieceLockTimer = this.pieceLockTimer;
        copy.isPartnerRight = this.isPartnerRight;
        copy.blockAnimationTimer = this.blockAnimationTimer;
        copy.isPieceSet = this.isPieceSet;
        copy.specialPieces = this.specialPieces;
        copy.countOfBreaks = this.countOfBreaks;
        copy.isDebug = this.isDebug;
        copy.powersKeep = this.powersKeep;
        copy.ids = this.ids;
        copy.idIndex = this.idIndex;
        copy.randomColumnIndices = this.randomColumnIndices;
        copy.nextBlocks = this.nextBlocks;
        copy.currentBlockPointer = this.currentBlockPointer;
        copy.fastDown = this.fastDown;
        copy.brokenBlockCount = this.brokenBlockCount;
        copy.hasGameStarted = this.hasGameStarted;
        copy.name = this.name;
        copy.tick = this.tick;
        copy.boardNumber = this.boardNumber;

        return copy;
    }

    @Override
    public YipeeGameBoardState deepCopy() {
        YipeeGameBoardState copy = copy();

        copy.playerCells = StaticArrayUtils.copyIntMatrix(this.playerCells);
        copy.partnerCells = StaticArrayUtils.copyIntMatrix(this.partnerCells);

        Queue<YipeeBrokenBlock> nuBrokenCells = new LinkedList<>();
        for(YipeeBrokenBlock brokenCell : Util.safeIterable(this.brokenCells)) {
            YipeeBrokenBlock nuBrokenBlock = new YipeeBrokenBlock(brokenCell.getBlock(), brokenCell.getRow(), brokenCell.getCol());
            nuBrokenCells.add(nuBrokenBlock);
        }
        copy.brokenCells = nuBrokenCells;

        Queue<YipeeBlockMove> nuCellsToDrop = new LinkedList<>();
        for(YipeeBlockMove cellToDrop : Util.safeIterable(this.cellsToDrop)) {
            YipeeBlockMove nuCellToDrop = new YipeeBlockMove(cellToDrop.getCellId(), cellToDrop.getBlock(), cellToDrop.getCol(), cellToDrop.getRow(), cellToDrop.getTargetRow());
            nuCellsToDrop.add(nuCellToDrop);
        }
        copy.cellsToDrop = nuCellsToDrop;

        // queues of primitives don't need deep copy
        copy.powers = (this.powers != null) ? new LinkedList<>(this.powers) : null;
        copy.specialPieces = (this.specialPieces != null) ? new LinkedList<>(this.specialPieces) : null;

        // arrays
        copy.countOfBreaks = (this.countOfBreaks != null) ? Arrays.copyOf(this.countOfBreaks, this.countOfBreaks.length) : null;
        copy.powersKeep = (this.powersKeep != null) ? Arrays.copyOf(this.powersKeep, this.powersKeep.length) : null;
        copy.ids = (this.ids != null) ? Arrays.copyOf(this.ids, this.ids.length) : null;
        copy.randomColumnIndices = (this.randomColumnIndices != null) ? Arrays.copyOf(this.randomColumnIndices, this.randomColumnIndices.length) : null;

        return copy;
    }
}
