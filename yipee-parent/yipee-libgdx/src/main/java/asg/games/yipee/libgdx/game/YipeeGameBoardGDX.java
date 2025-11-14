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
package asg.games.yipee.libgdx.game;

import asg.games.yipee.common.game.CommonRandomNumberArray;
import asg.games.yipee.common.game.GameBoardState;
import asg.games.yipee.common.game.GamePhase;
import asg.games.yipee.common.game.PlayerAction;
import asg.games.yipee.libgdx.objects.YipeeBlockGDX;
import asg.games.yipee.libgdx.objects.YipeeBlockMoveGDX;
import asg.games.yipee.libgdx.objects.YipeeBrokenBlockGDX;
import asg.games.yipee.libgdx.objects.YipeeClockGDX;
import asg.games.yipee.libgdx.objects.YipeeGameBoardStateGDX;
import asg.games.yipee.libgdx.objects.YipeePieceGDX;
import asg.games.yipee.libgdx.tools.LibGDXRandomUtil;
import asg.games.yipee.libgdx.tools.LibGDXUtil;
import asg.games.yipee.libgdx.tools.NetUtil;
import asg.games.yipee.libgdx.tools.YipeeGDXPrinter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents the game board.
 * All logic happens here
 *
 * @author Blakbro2k
 */
@Getter
@Setter
public class YipeeGameBoardGDX implements Disposable {
    public static final int MAX_RANDOM_BLOCK_NUMBER = 2048;
    public static final int MAX_COLS = 6;
    public static final int MAX_ROWS = 16;
    public static final int MAX_PLAYABLE_ROWS = MAX_ROWS - 3;
    public static final int HORIZONTAL_HOO_TIME = 2;
    public static final int VERTICAL_HOO_TIME = 4;
    public static final int DIAGONAL_HOO_TIME = 3;
    public static final float FALL_RATE = 0.04f;
    public static final float FAST_FALL_RATE = 0.496f;
    private static final int MAX_FALL_VALUE = 1;
    private static final int CONST_ROW_ADD = 1;
    private GamePhase currentPhase = GamePhase.SPAWN_NEXT;

    //private final YokelPiece MEDUSA_PIECE = new YokelPiece(0, YokelBlock.MEDUSA, YokelBlock.MEDUSA, YokelBlock.MEDUSA);
    //private final YokelPiece MIDAS_PIECE = new YokelPiece(0, YokelBlock.BOT_MIDAS, YokelBlock.MID_MIDAS, YokelBlock.TOP_MIDAS);

    private int[][] cells;
    private int[][] partnerCells;
    private boolean[] ids;
    @Getter
    private int idIndex;
    private final int[] randomColumnIndices = new int[MAX_COLS];
    private final boolean[][] colorBlastGrid
        = {new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS],
        new boolean[MAX_COLS]};
    private static final int[] targetRows = new int[MAX_COLS];
    private final int[] pushRowOrder = {0, 1, 2, 2, 1, 0};
    private final int[] pushColumnOrder = {2, 3, 1, 4, 0, 5};
    private final int[] countOfPieces = new int[MAX_COLS];

    //Added
    private int[] countOfBreaks = new int[MAX_COLS];
    private int[] powersKeep = new int[MAX_COLS];

    private final boolean[] cellMatches = new boolean[7];
    private final int[] cellIndices = {0, 1, 2, 3, 4, 5, 6};
    private final int[] cellHashes = {5, 25, 7, 49, 35, 19, 23};

    private final int[] columnMatchLookup = {-1, -1, 0, 1, 1, 1, 0, -1};
    private final int[] rowMatchLookup = {0, 1, 1, 1, 0, -1, -1, -1};

    private YipeePieceGDX piece;
    private YipeePieceGDX nextPiece;
    private YipeeClockGDX gameClock;

    private float pieceFallTimer;
    private float pieceLockTimer;
    private float blockAnimationTimer;
    private CommonRandomNumberArray nextBlocks;
    private int currentBlockPointer = -1;
    private boolean fastDown;

    @Getter
    private Queue<Integer> powers = LibGDXUtil.newQueue();
    private Queue<Integer> specialPieces = LibGDXUtil.newQueue();
    Queue<YipeeBrokenBlockGDX> brokenCells = LibGDXUtil.newQueue();
    Array<YipeeBlockMoveGDX> cellsToDrop = GdxArrays.newArray();
    private int boardNumber = -1;

    private int yahooDuration = 0;
    private int brokenBlockCount = 0;
    private boolean hasGameStarted = false;

    private boolean hasPartner;
    private boolean isPartnerRight = true;
    private boolean debug = false;
    private String name = null;

    //Empty Constructor required for Json.Serializable
    public YipeeGameBoardGDX() {
    }

    public YipeeGameBoardGDX(long seed) {
        cells = new int[MAX_ROWS][MAX_COLS];
        partnerCells = new int[MAX_ROWS][MAX_COLS];
        ids = new boolean[128];
        gameClock = new YipeeClockGDX();
        reset(seed);
    }

    public void importGameState(GameBoardState state, GameBoardState partnerState) {
        if (state != null) {
            setCurrentPhase(state.getCurrentPhase());
            setBrokenBlockCount(state.getBrokenBlockCount());
            setFastDown(state.isFastDown());
            setCurrentBlockPointer(state.getCurrentBlockPointer());
            setNextBlocks(state.getNextBlocks());
            setCountOfBreaks(state.getCountOfBreaks());
            setPowersKeep(state.getPowersKeep());
            setGameClock(NetUtil.fromJsonClient(state.getGameClock(), YipeeClockGDX.class));
            setIds(state.getIds());
            setIdIndex(state.getIdIndex());
            setDebug(state.isDebug());
            setName(state.getName());
            setPiece(NetUtil.fromJsonClient(state.getPiece(), YipeePieceGDX.class));
            setNextPiece(NetUtil.fromJsonClient(state.getNextPiece(), YipeePieceGDX.class));
            setCells(state.getPlayerCells());
            setPieceFallTimer(state.getPieceFallTimer());
            setPieceLockTimer(state.getPieceLockTimer());
            setBlockAnimationTimer(state.getBlockAnimationTimer());
            setYahooDuration(state.getYahooDuration());
            setPartnerRight(state.isPartnerRight());
            setPowers(LibGDXUtil.iterableToQueue(state.getPowers()));
            //setBrokenCells(state.getBrokenCells());
            setSpecialPieces(LibGDXUtil.iterableToQueue(state.getSpecialPieces()));
            setHasGameStarted(state.isHasGameStarted());
            setBoardNumber(state.getBoardNumber());

            if (partnerState != null) {
                hasPartner = true;
                setPartnerCells(partnerState.getPlayerCells());
            } else {
                hasPartner = false;
            }
        }
    }

    public GameBoardState exportGameState() {
        GameBoardState state = new YipeeGameBoardStateGDX();
        state.setCurrentPhase(currentPhase);
        state.setBrokenBlockCount(brokenBlockCount);
        state.setFastDown(fastDown);
        state.setCurrentBlockPointer(currentBlockPointer);
        state.setNextBlocks(nextBlocks);
        state.setCountOfBreaks(countOfBreaks);
        state.setPowersKeep(powersKeep);
        state.setGameClock(NetUtil.toJsonClient(gameClock));
        state.setIds(ids);
        state.setIdIndex(idIndex);
        state.setDebug(debug);
        state.setName(name);
        state.setCurrentStateTimeStamp(TimeUtils.nanoTime());
        state.setPiece(NetUtil.toJsonClient(piece));
        state.setNextPiece(NetUtil.toJsonClient(nextPiece));
        state.setPlayerCells(cells);
        state.setPieceFallTimer(pieceFallTimer);
        state.setPieceLockTimer(pieceLockTimer);
        state.setBlockAnimationTimer(blockAnimationTimer);
        state.setYahooDuration(yahooDuration);
        state.setPartnerRight(isPartnerRight);
        state.setPowers(powers);
        state.setBrokenCells(brokenCells);
        state.setSpecialPieces(specialPieces);
        state.setHasGameStarted(hasGameStarted);
        state.setBoardNumber(boardNumber);

        if (hasPartner) {
            state.setPartnerCells(partnerCells);
        }
        return state;
    }

    private void resetPieceFallTimer() {
        pieceFallTimer = MAX_FALL_VALUE;
    }

    private void resetLockOutTimer() {
        pieceLockTimer = MAX_FALL_VALUE;
    }

    private void resetAnimationTimer() {
        blockAnimationTimer = MAX_FALL_VALUE;
    }

    private void resetPiece() {
        resetPieceFallTimer();
        resetLockOutTimer();
        piece = null;
    }

    public void setPartnerCells(YipeeGameBoardGDX partnerB, boolean isRight) {
        setPartnerBoardState(partnerB.exportGameState(), isRight);
    }

    public void setPartnerBoardState(GameBoardState partnerBoardState, boolean isRight) {
        if (partnerBoardState != null) {
            this.partnerCells = partnerBoardState.getPlayerCells();
        }
        this.isPartnerRight = isRight;
    }

    public static class TestRandomBlockArray extends LibGDXRandomUtil.RandomNumberArray {
        int[] testRandomNumbers;

        public TestRandomBlockArray(int byteLength, long seed, int maxValue) {
            super(byteLength, seed, maxValue);
            testRandomNumbers = new int[byteLength];
            int count = 0;
            int triplet = 0;

            for (int i = 0; i < byteLength; i++) {
                testRandomNumbers[i] = count;
                ++triplet;
                if (triplet > 2) {
                    triplet = 0;
                    ++count;
                    if (count > maxValue - 1) {
                        count = 0;
                    }
                }
            }
        }

        @Override
        public int getRandomNumberAt(int index) {
            if (index < 0)
                System.out.println("Assertion failure: invalid random index " + index);
            return testRandomNumbers[index % testRandomNumbers.length];
        }
    }

    public void reset(long seed) {
        if (seed < 0) {
            nextBlocks = new TestRandomBlockArray(MAX_RANDOM_BLOCK_NUMBER, seed, MAX_COLS);
        } else {
            nextBlocks = new LibGDXRandomUtil.RandomNumberArray(MAX_RANDOM_BLOCK_NUMBER, seed, MAX_COLS);
        }

        gameClock.stop();
        clearBoard();
        resetPiece();
        LibGDXUtil.clearArrays(powers, specialPieces, brokenCells, cellsToDrop);
        Arrays.fill(countOfBreaks, 0);
        Arrays.fill(powersKeep, 0);
        end();
    }

    @Override
    public void dispose() {
        LibGDXUtil.clearArrays(powers, specialPieces, brokenCells, cellsToDrop);
        Arrays.fill(countOfBreaks, 0);
        Arrays.fill(powersKeep, 0);
    }

    public void begin() {
        if (!hasGameStarted) {
            gameClock.start();
            hasGameStarted = true;
        }
    }

    public boolean hasGameStarted() {
        return hasGameStarted;
    }

    public void end() {
        resetPiece();
        resetAnimationTimer();
        yahooDuration = 0;
        brokenBlockCount = 0;
        currentBlockPointer = -1;
        hasGameStarted = false;
        piece = null;
    }

    public void setCell(int row, int col, int cell) {
        cells[row][col] = cell;
    }

    public int getPieceValue(int c, int r) {
        return YipeeBlockEvalGDX.getCellFlag(cells[r][c]);
    }

    public int getBlockValueAt(int column, int row) {
        return cells[row][column];
    }

    public void clearBoard() {
        for (int c = 0; c < MAX_COLS; c++) {
            for (int r = 0; r < MAX_ROWS; r++) {
                clearCell(r, c);
            }
        }
        for (int i = 0; i < 128; i++) {
            ids[i] = false;
        }
        idIndex = 0;
    }

    private static boolean isCellInBoard(int c, int r) {
        return c >= 0 && c < MAX_COLS && r >= 0 && r < MAX_ROWS;
    }

    public int getSafeCell(int c, int r) {
        if (!isCellInBoard(c, r))
            return MAX_COLS;

        int value = getPieceValue(c, r);

        if (value >= MAX_COLS) {
            value = MAX_COLS;
        }

        return value;
    }

    int incrementID() {
        do {
            idIndex++;

            if (idIndex == ids.length)
                idIndex = 0;
        } while (ids[idIndex]);

        ids[idIndex] = true;
        return idIndex;
    }

    void releaseID(int index) {
        if (!ids[index])
            System.out.println("Assertion failure: id " + index
                + " released but not held");

        ids[index] = false;
    }

    public boolean isArtificiallyAdded(int column, int row) {
        return YipeeBlockEvalGDX.hasAddedByYahooFlag(cells[row][column]);
    }

    public boolean isCellBroken(int column, int row) {
        return YipeeBlockEvalGDX.hasBrokenFlag(cells[row][column]);
    }

    public void setValueWithID(int column, int row, int value) {
        cells[row][column] = YipeeBlockEvalGDX.setIDFlag(value, incrementID());
    }

    void addRow(int amount) {
        for (int row = MAX_PLAYABLE_ROWS; row >= amount; row--) {
            for (int col = 0; col < MAX_COLS; col++)
                cells[row][col] = cells[row - amount][col];
        }

        for (int row = 0; row < amount; row++) {
            for (int col = 0; col < MAX_COLS; col++)
                cells[row][col] = YipeeBlockGDX.CLEAR_BLOCK;
        }

        int hash = getBoardMakeupHash();

        for (int i = amount - 1; i >= 0; i--) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = getNonAdjacentCell(col, i, hash + i + col);

                value = YipeeBlockEvalGDX.setIDFlag(value, incrementID());

                if (value < 0) {
                    System.out.println("Assertion failure: unable to find non-adjacent cell " + col + "," + i);
                }

                cells[i][col] = value;
            }
        }
        updateBoard();
    }

    void removeRow(int amount) {
        for (int row = 0; row < MAX_PLAYABLE_ROWS - amount; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (row < amount && YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < MAX_COLS)
                    releaseID(YipeeBlockEvalGDX.getID(cells[row][col]));

                cells[row][col] = cells[row + amount][col];
            }
        }

        for (int row = MAX_PLAYABLE_ROWS - amount; row < MAX_PLAYABLE_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                cells[row][col] = YipeeBlockGDX.CLEAR_BLOCK;
            }
        }

        updateBoard();
    }

    void shuffleColumnIndices() {
        for (int i = 0; i < MAX_COLS; i++) {
            randomColumnIndices[i] = i;
        }

        LibGDXRandomUtil.RandomNumber generator = new LibGDXRandomUtil.RandomNumber((long) getBoardMakeupHash());

        for (int i = 0; i < 10; i++) {
            int first = generator.next(MAX_COLS);
            int second = generator.next(MAX_COLS);
            int value = randomColumnIndices[first];
            randomColumnIndices[first] = randomColumnIndices[second];
            randomColumnIndices[second] = value;
        }
    }

    void addStone(int amount) {
        shuffleColumnIndices();

        for (int i = 0; i < amount; i++) {
            int x = randomColumnIndices[i];

            for (int y = 0; y < MAX_PLAYABLE_ROWS; y++) {
                if (getPieceValue(x, y) == MAX_COLS) {
                    cells[y][x] = YipeeBlockGDX.STONE;
                    break;
                }
            }
        }

        updateBoard();
    }

    void dropStone(int amount) {
        int count = 0;

        for (int y = 12; y >= 0; y--) {
            for (int x = 0; x < MAX_COLS; x++) {
                if (getPieceValue(x, y) == YipeeBlockGDX.STONE) {
                    for (int i = y; i >= 1; i--) {
                        cells[i][x] = cells[i - 1][x];
                    }

                    cells[0][x] = YipeeBlockGDX.STONE;

                    if (++count == amount) {
                        return;
                    }
                }
            }
        }

        updateBoard();
    }

    void markColorBlast() {
        // Clear the grid
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                colorBlastGrid[row][col] = false;
            }
        }

        // loop through cells
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {

                // if the piece is purple
                if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) == YipeeBlockGDX.Op_BLOCK
                    // And the piece is a power
                    && YipeeBlockEvalGDX.getPowerFlag(cells[row][col]) != 0) {

                    if (isCellInBoard(col - 1, row - 1))
                        colorBlastGrid[row - 1][col - 1] = true;
                    if (isCellInBoard(col, row - 1))
                        colorBlastGrid[row - 1][col] = true;
                    if (isCellInBoard(col + 1, row - 1))
                        colorBlastGrid[row - 1][col + 1] = true;
                    if (isCellInBoard(col - 1, row))
                        colorBlastGrid[row][col - 1] = true;
                    if (isCellInBoard(col, row))
                        colorBlastGrid[row][col] = true;
                    if (isCellInBoard(col + 1, row))
                        colorBlastGrid[row][col + 1] = true;
                    if (isCellInBoard(col - 1, row + 1))
                        colorBlastGrid[row + 1][col - 1] = true;
                    if (isCellInBoard(col, row + 1))
                        colorBlastGrid[row + 1][col] = true;
                    if (isCellInBoard(col + 1, row + 1))
                        colorBlastGrid[row + 1][col + 1] = true;
                }
            }
        }

        // clear the cell pieces start in
        colorBlastGrid[15][2] = false;
    }

    boolean isColorBlastGridEmpty() {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (colorBlastGrid[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    //    int[] pushRowOrder = { 0, 1, 2, 2, 1, 0 };
    //    int[] pushColumnOrder = { 2, 3, 1, 4, 0, 5 };

    void pushCellToBottomOfBoard(int value) {
        for (int y = -2; y < 8; y++) {
            for (int x = 0; x < MAX_COLS; x++) {
                int col = pushColumnOrder[x];
                int row = y + pushRowOrder[col];

                if (row >= 0 && YipeeBlockEvalGDX.getCellFlag(cells[row][col]) != YipeeBlockGDX.STONE) {
                    if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < MAX_COLS) {
                        releaseID(YipeeBlockEvalGDX.getID(cells[row][col]));
                    }

                    cells[row][col] = YipeeBlockEvalGDX.setIDFlag(value, incrementID());
                    return;
                }
            }
        }

        updateBoard();
    }

    void colorBlast() {
        markColorBlast();

        if (isColorBlastGridEmpty()) {
            pushCellToBottomOfBoard(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Op_BLOCK, YipeeBlockGDX.DEFENSIVE_MINOR)));
        } else {
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (colorBlastGrid[row][col]) {
                        if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < MAX_COLS) {
                            releaseID(YipeeBlockEvalGDX.getID(cells[row][col]));
                        }

                        cells[row][col] = YipeeBlockEvalGDX.setIDFlag(YipeeBlockGDX.Op_BLOCK, incrementID());
                    }
                }
            }

            for (int col = 0; col < MAX_COLS; col++) {
                boolean bool = false;

                for (int row = 15; row >= 0; row--) {
                    if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) == MAX_COLS) {
                        if (bool) {
                            cells[row][col] = YipeeBlockEvalGDX.setIDFlag(YipeeBlockGDX.Op_BLOCK, incrementID());
                        }
                    } else {
                        bool = true;
                    }
                }
            }
        }
        updateBoard();
    }

    void defuse(int intensity) {
        int cellsDefused = 0;

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) == YipeeBlockGDX.Op_BLOCK
                    && YipeeBlockEvalGDX.getPowerFlag(cells[row][col]) != YipeeBlockGDX.Y_BLOCK) {

                    if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < MAX_COLS)
                        releaseID(YipeeBlockEvalGDX.getID(cells[row][col]));

                    if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) != MAX_COLS)
                        cells[row][col] = YipeeBlockGDX.STONE;

                    if (++cellsDefused == intensity)
                        return;
                }
            }
        }

        updateBoard();

        for (int i = cellsDefused; i < intensity; i++) {
            pushCellToBottomOfBoard(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Op_BLOCK, YipeeBlockGDX.OFFENSIVE_MINOR)));
        }
    }

    void removeColorFromBoard() {
        // Clear the count
        Arrays.fill(countOfPieces, 0);

        // Loop through the board and tally up count of cells
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = YipeeBlockEvalGDX.getCellFlag(cells[row][col]);

                if (value < MAX_COLS) {
                    countOfPieces[value]++;
                }
            }
        }

        int index = 0;

        // Loop left to right
        for (int i = 0; i < countOfPieces.length; i++) {
            // If there is a color move the index to the farthest right
            if (countOfPieces[i] > 0) {
                index = i;
            }
        }

        // Loop left to right
        for (int i = 0; i < countOfPieces.length; i++) {
            // Select the color with the fewest amount of cells
            if (countOfPieces[i] > 0 && countOfPieces[i] < countOfPieces[index]) {
                index = i;
            }
        }

        boolean colorRemoved = false;

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) == index) {
                    cells[row][col] = YipeeBlockEvalGDX.addBrokenFlag(cells[row][col]);
                    colorRemoved = true;
                }
            }
        }

        if (colorRemoved) {
            updateBoard();
            //handleBrokenCellDrops();
        }
    }

    //Increments Break Blocks Conter
    //Adds powers
    //Adds broken cells to queue
    //Clears broken cells
    public void handleBrokenCellDrops() {
        for (int col = 0; col < MAX_COLS; col++) {
            int index = 0;

            for (int row = 0; row < MAX_ROWS; row++) {

                if (isCellBroken(col, row)) {
                    if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < MAX_COLS)
                        releaseID(YipeeBlockEvalGDX.getID(cells[row][col]));
                    incrementBreakCount(YipeeBlockEvalGDX.getCellFlag(cells[row][col]));
                    addPowerToQueue(cells[row][col]);
                    brokenCells.addLast(new YipeeBrokenBlockGDX(YipeeBlockEvalGDX.getCellFlag(cells[row][col]), row, col));
                } else {
                    cells[index][col] = cells[row][col];
                    index++;
                }
            }

            for (; index < MAX_ROWS; index++) {
                cells[index][col] = YipeeBlockGDX.CLEAR_BLOCK;
            }
        }
        updateBoard();
    }

    public void flagPowerBlockCells() {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YipeeBlockEvalGDX.hasPowerBlockFlag(cells[row][col])) {
                    cells[row][col] = YipeeBlockEvalGDX.addBrokenFlag(cells[row][col]);
                }
            }
        }
        updateBoard();
    }

    public void flagBoardMatches() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++)
                flagCellForMatches(j, i);
        }
    }

    void flagCellForMatches(int column, int row) {
        if (getPieceValue(column, row) < MAX_COLS) {
            flagCellForMatches(column, row, -1, 1);
            flagCellForMatches(column, row, 0, 1);
            flagCellForMatches(column, row, 1, 1);
            flagCellForMatches(column, row, 1, 0);
        }
    }

    void flagCellForMatches(int x, int y, int _x, int _y) {
        int cell = getPieceValue(x, y);

        int count;
        for (count = 1;
             (isCellInBoard(x + count * _x, y + count * _y)
                 && getPieceValue(x + count * _x, y + count * _y) == cell);
            //&& YokelBlockEval.hasPowerBlockFlag(cells[y + count * _y][x + count * _x]) == false);
             count++) {
            /* empty */
        }

        if (count >= 3) {
            for (int i = 0; i < count; i++) {
                int copy = cells[y + i * _y][x + i * _x];
                copy = YipeeBlockEvalGDX.addBrokenFlag(copy);
                cells[y + i * _y][x + i * _x] = copy;
            }
        }
    }

    public void applyPlayerAction(PlayerAction action) {
        //TODO: handle yahoo add blocks
        if (action != null) {
            if (PlayerAction.ActionType.S_SPEED.equals(action.getActionType())) {
                //TODO: handle speed special
            } else if (PlayerAction.ActionType.O_MEDUSA.equals(action.getActionType())) {
                addSpecialPiece(2);
            } else if (PlayerAction.ActionType.O_MIDAS.equals(action.getActionType())) {
                addSpecialPiece(1);
            } else {
                handlePower(LibGDXUtil.otoi(action.getActionData()));
            }
        }
    }

    public void handlePower(int i) {
        if (YipeeBlockEvalGDX.getPowerFlag(i) == 0) {
            switch (i) {
                case YipeeBlockGDX.SPECIAL_BLOCK_1:
                    removeAllPowersFromBoard();
                    break;
                case YipeeBlockGDX.SPECIAL_BLOCK_2:
                    removeAllStonesFromBoard();
                    break;
                default:
                    System.out.println("Assertion failure: invalid rare attack " + i);
                    break;
            }
        } else {
            boolean isOffensive = YipeeBlockEvalGDX.isOffensive(i);
            int level = YipeeBlockEvalGDX.getPowerLevel(i);

            switch (YipeeBlockEvalGDX.getCellFlag(i)) {
                //Y
                case YipeeBlockGDX.Y_BLOCK:
                    if (isOffensive) {
                        addRow(1);
                    } else {
                        removeRow(1);
                    }

                    break;
                case YipeeBlockGDX.A_BLOCK:
                    if (isOffensive) {
                        dither(2 * Math.min(level, 3));
                    } else {
                        clump(2 * Math.min(level, 3));
                    }

                    break;
                case YipeeBlockGDX.H_BLOCK:
                    if (isOffensive) {
                        addStone(Math.min(level, 3));
                    } else {
                        dropStone(Math.min(level, 3));
                    }

                    break;
                case YipeeBlockGDX.Op_BLOCK:
                    if (isOffensive) {
                        defuse(Math.min(level, 3));
                    } else {
                        colorBlast();
                    }

                    break;
                case YipeeBlockGDX.Oy_BLOCK:
                    System.out.println("Assertion failure: invalid CELL5 board attack " + i);
                    break;
                case YipeeBlockGDX.EX_BLOCK:
                    if (isOffensive) {
                        removePowersFromQueue();
                    } else {
                        removeColorFromBoard();
                    }

                    break;
                default:
                    System.out.println("Assertion failure: invalid attack" + i);
            }
        }
    }

    void removePowersFromQueue() {
        Queue<Integer> powers = new Queue<>();
        Queue<Integer> queue = getPowers();
        int count = LibGDXUtil.sizeOf(queue) / 2;

        Iterator<Integer> iterator = queue.iterator();
        while (iterator.hasNext() && count-- > 0) {
            powers.addFirst(iterator.next());
            iterator.remove();
        }

        addRemovedPowersToBoard(powers);
    }

    public void addRemovedPowersToBoard(Queue<Integer> powers) {
        shuffleColumnIndices();

        int count = 0;

        while (LibGDXUtil.sizeOf(powers) > 0) {
            int value = powers.removeFirst();
            //powers.removeAt(0);

            int i;

            for (i = count; i != (count + MAX_COLS - 1) % MAX_COLS; i = (i + 1) % MAX_COLS) {
                int col = randomColumnIndices[i];

                if (cells[12][col] == MAX_COLS) {
                    for (int row = 15; row >= 1; row--) {
                        cells[row][col] = cells[row - 1][col];
                    }

                    cells[0][col] = value;

                    if (!hasFullMatchInProximity(col, 0)) {
                        cells[0][col] = YipeeBlockEvalGDX.setIDFlag(value, incrementID());
                        break;
                    }

                    for (int row = 0; row < 15; row++) {
                        cells[row][col] = cells[row + 1][col];
                    }

                    cells[15][col] = MAX_COLS;
                }
            }

            count = (i + 1) % MAX_COLS;
        }

        updateBoard();
    }

    void dither(int intensity) {
        int num = 0;

        for (int row = 12; row >= 0; row--) {
            for (int col = 0; col < MAX_COLS; col++) {
                boolean bool = unmatchCell(col, row);

                if (bool && ++num == intensity)
                    return;
            }
        }
    }

    boolean unmatchCell(int col, int row) {
        boolean successfulSwap = false;

        // If there's a matching cell nearby
        if (hasMatchingCellInProximity(col, row)) {
            // Loop through the board
            for (int r = 0; r < MAX_PLAYABLE_ROWS && !successfulSwap; r++) {
                for (int c = 0; c < MAX_COLS; c++) {
                    if (getPieceValue(c, r) < MAX_COLS) {
                        int swap = cells[r][c];

                        // Swap passed cell with another on board
                        cells[r][c] = cells[row][col];
                        cells[row][col] = swap;

                        // If both no longer have cells nearby, it's success
                        if (!hasMatchingCellInProximity(col, row)
                            && !hasMatchingCellInProximity(c, r)) {
                            successfulSwap = true;
                            break;
                        }

                        // Undo the swap
                        swap = cells[r][c];
                        cells[r][c] = cells[row][col];
                        cells[row][col] = swap;
                    }
                }
            }
        }

        updateBoard();
        return successfulSwap;
    }

    void clump(int numberOfCellsToChange) {
        int swapCount = 0;
        // start from the top and work way down
        for (int row = 12; row >= 0; row--) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (getPieceValue(col, row) < MAX_COLS
                    // If there's not a matching cell nearby
                    && !hasMatchingCellInProximity(col, row)) {

                    boolean successfulSwap = false;

                    // Start from the bottom and work up
                    for (int y = 0; y < MAX_PLAYABLE_ROWS && !successfulSwap; y++) {
                        for (int x = 0; x < MAX_COLS; x++) {
                            if (getPieceValue(x, y) < MAX_COLS) {
                                int copy = cells[y][x];

                                // swap the two cells
                                cells[y][x] = cells[row][col];
                                cells[row][col] = copy;

                                // If one of the cells now has a matching cell nearby
                                if ((hasMatchingCellInProximity(col, row) || hasMatchingCellInProximity(x, y))
                                    // and not a full match, since that would be too significant of a change
                                    && !hasFullMatchInProximity(col, row)
                                    && !hasFullMatchInProximity(x, y)) {
                                    // the swap is then successful then break and add to changed pieces
                                    successfulSwap = true;
                                    break;
                                }

                                // undo the swap
                                copy = cells[y][x];
                                cells[y][x] = cells[row][col];
                                cells[row][col] = copy;
                            }
                        }
                    }

                    updateBoard();

                    if (successfulSwap && ++swapCount == numberOfCellsToChange) {
                        return;
                    }
                }
            }
        }
    }

    int getNonAdjacentCell(int x, int y, int hash) {
        Arrays.fill(cellMatches, false);

        cellMatches[getSafeCell(x - 1, y)] = true;
        cellMatches[getSafeCell(x - 1, y + 1)] = true;
        cellMatches[getSafeCell(x, y + 1)] = true;
        cellMatches[getSafeCell(x + 1, y + 1)] = true;
        cellMatches[getSafeCell(x + 1, y)] = true;
        cellMatches[getSafeCell(x + 1, y - 1)] = true;
        cellMatches[getSafeCell(x, y - 1)] = true;
        cellMatches[getSafeCell(x - 1, y - 1)] = true;

        int id = hash % cellHashes.length;
        int value = cellHashes[id];

        for (int col = 0; col < MAX_COLS; col++) {
            if (!cellMatches[(cellIndices[id] + value * col) % MAX_COLS]) {
                return (cellIndices[id] + value * col) % MAX_COLS;
            }
        }

        return -1;
    }

    //int[] colS = { -1, -1, 0, 1, 1,  1,  0, -1 };
    //int[] rowS = {  0,  1, 1, 1, 0, -1, -1, -1 };

    boolean hasMatchingCellInProximity(int col, int row) {
        int value = getPieceValue(col, row);

        if (value >= MAX_COLS) return false;

        for (int i = 0; i < columnMatchLookup.length; i++) {
            if (getSafeCell(col + columnMatchLookup[i], row + rowMatchLookup[i]) == value)
                return true;
        }

        return false;
    }

    boolean hasFullMatchInProximity(int x, int y) {
        int value = getPieceValue(x, y);

        if (value < MAX_COLS) {
            // x = 2, y = 4

            // x + -1, y + 0 = 1, 4
            // x - -1, y + 0 = 3, 4

            // x + -1, y + 0 = 1, 4
            // x + -2, y + 0 = 0, 4

            for (int i = 0; i < columnMatchLookup.length; i++) {
                // If there's a match like XXX
                if (getSafeCell(x + columnMatchLookup[i], y + rowMatchLookup[i]) == value
                    && getSafeCell(x - columnMatchLookup[i], y - rowMatchLookup[i]) == value)
                    return true;

                // If there's a a match like XXOX
                if (getSafeCell(x + columnMatchLookup[i], y + rowMatchLookup[i]) == value
                    && getSafeCell(x + 2 * columnMatchLookup[i], y + 2 * rowMatchLookup[i]) == value)
                    return true;

                // If there's a match like XXOX
                if (getSafeCell(x - columnMatchLookup[i], y - rowMatchLookup[i]) == value
                    && getSafeCell(x - 2 * columnMatchLookup[i], y - 2 * rowMatchLookup[i]) == value)
                    return true;
            }
        }

        return false;
    }

    void removeAllPowersFromBoard() {
        for (int row = 0; row < MAX_PLAYABLE_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = cells[row][col];

                if (YipeeBlockEvalGDX.getPowerFlag(value) != 0) {
                    cells[row][col] = YipeeBlockEvalGDX.setPowerFlag(value, 0);
                }
            }
        }
        updateBoard();
    }

    void removeAllStonesFromBoard() {
        for (int x = 0; x < MAX_COLS; x++) {
            int row = 0;

            for (int y = 0; y < MAX_ROWS; y++) {
                if (YipeeBlockEvalGDX.getCellFlag(cells[y][x]) != 7) {
                    cells[row][x] = cells[y][x];
                    row++;
                }
            }

            for (; row < MAX_ROWS; row++)
                cells[row][x] = MAX_COLS;
        }
        updateBoard();
    }

    int getBoardMakeupHash() {
        int num = 999;

        for (int row = 0; row < MAX_PLAYABLE_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                num += YipeeBlockEvalGDX.removePartnerBreakFlag(cells[row][col]) * (row * MAX_COLS + col);
            }
        }

        return num;
    }

    public int getColumnFill(int column) {
        int row;
        for (row = MAX_ROWS; row > 0; row--) {
            if (getPieceValue(column, row - 1) != MAX_COLS)
                break;
        }
        return row;
    }

    public boolean isDownCellFree(int column, int row) {
        return row > 0 && row < MAX_PLAYABLE_ROWS + 1 && getPieceValue(column, row - 1) == YipeeBlockGDX.CLEAR_BLOCK;
    }

    public boolean isRightCellFree(int column, int row) {
        return column < MAX_COLS - 1 && getPieceValue(column + 1, row) == YipeeBlockGDX.CLEAR_BLOCK;
    }

    public boolean isLeftCellFree(int column, int row) {
        return column > 0 && getPieceValue(column - 1, row) == YipeeBlockGDX.CLEAR_BLOCK;
    }

    public int getColumnWithPossiblePieceMatch(YipeePieceGDX piece) {
        shuffleColumnIndices();

        for (int i = 0; i < MAX_COLS; i++) {
            int x = randomColumnIndices[i];
            int y = getColumnFill(x);

            if (y < 12) {
                for (int j = 0; j < 3; j++) {
                    cells[y][x] = piece.getValueAt(j % 3);
                    cells[y + 1][x] = piece.getValueAt((1 + j) % 3);
                    cells[y + 2][x] = piece.getValueAt((2 + j) % 3);

                    boolean hasFullMatch =
                        (hasFullMatchInProximity(x, y)
                            || hasFullMatchInProximity(x, y + 1)
                            || hasFullMatchInProximity(x, y + 2));

                    cells[y][x] = MAX_COLS;
                    cells[y + 1][x] = MAX_COLS;
                    cells[y + 2][x] = MAX_COLS;

                    updateBoard();

                    if (hasFullMatch)
                        return j << 8 | x;
                }
            }
        }

        int column = getBoardMakeupHash() % MAX_COLS;

        for (int col = 0; col < MAX_COLS; col++) {
            if (getColumnFill(col) < getColumnFill(column)) {
                column = col;
            }
        }

        return column;
    }

    //Place Yahoo power
    public int getColumnToPlaceYahooCell(int value) {
        // pick "random" column to start
        int x = getBoardMakeupHash() % MAX_COLS;

        // loop through columns
        for (int i = 0; i < MAX_COLS; i++) {
            // get the height of the column
            int y = getColumnFill(x);

            // if the height fits in visible rows
            if (y < 12) {
                // put the pending piece in that spot
                cells[y][x] = value;

                // check for a near match
                boolean match = hasMatchingCellInProximity(x, y);

                // reset the cell
                cells[y][x] = MAX_COLS;

                // if there is no match, return the column
                if (!match)
                    return x;
            }

            // move to next column wrapped around column count
            x = (x + 1) % MAX_COLS;
        }

        // If unable to find a column without piece like this nearby,
        // get less picky and only ensure that the piece won't make a break

        // loop through columns again
        for (int i = 0; i < MAX_COLS; i++) {
            // get height of column
            int y = getColumnFill(x);

            if (y < 12) {
                cells[y][x] = value;
                boolean bool = hasFullMatchInProximity(x, y);
                cells[y][x] = MAX_COLS;

                if (!bool)
                    return x;
            }

            x = (x + 1) % MAX_COLS;
        }

        return -1;
    }

    int getYahooDuration() {
        int duration = 0;
        int horizontal = 0;
        int vert = 0;
        int diag = 0;

        //Count Horizontals
        for (int row = 0; row < MAX_PLAYABLE_ROWS; row++) {
            if (checkForNonVerticalYahoo(row, 0)) {
                //duration += HORIZONTAL_HOO_TIME;
                ++horizontal;

                for (int column = 0; column < MAX_COLS; column++) {
                    cells[row][column] = YipeeBlockEvalGDX.addBrokenFlag(cells[row][column]);
                }
            }
        }

        //Count Verticals
        for (int column = 0; column < MAX_COLS; column++) {
            for (int row = 0; row < 10; row++) {
                if (YipeeBlockEvalGDX.getCellFlag(cells[row][column]) == 5) {
                    if (YipeeBlockEvalGDX.getCellFlag(cells[row + 1][column]) == 4) {
                        if (YipeeBlockEvalGDX.getCellFlag(cells[row + 2][column]) != 3) {
                            continue;
                        }
                    } else if (YipeeBlockEvalGDX.getCellFlag(cells[row + 1][column]) != 3
                        || YipeeBlockEvalGDX.getCellFlag(cells[row + 2][column]) != 4) {
                        continue;
                    }

                    if (YipeeBlockEvalGDX.getCellFlag(cells[row + 3][column]) == 2
                        && YipeeBlockEvalGDX.getCellFlag(cells[row + 4][column]) == 1
                        && YipeeBlockEvalGDX.getCellFlag(cells[row + 5][column]) == 0) {

                        //duration += VERTICAL_HOO_TIME;
                        ++vert;

                        for (int i = 0; i < MAX_COLS; i++) {
                            cells[row + i][column] = YipeeBlockEvalGDX.addBrokenFlag(cells[row + i][column]);
                        }
                    }
                }
            }
        }

        //Check Diagonals
        for (int row = 0; row < 8; row++) {
            if (checkForNonVerticalYahoo(row, 1)) {
                //duration += DIAGONAL_HOO_TIME;
                ++diag;

                for (int col = 0; col < MAX_COLS; col++) {
                    cells[row + col][col] = YipeeBlockEvalGDX.addBrokenFlag(cells[row + col][col]);
                }
            }
        }

        for (int row = 5; row < MAX_PLAYABLE_ROWS; row++) {
            if (checkForNonVerticalYahoo(row, -1)) {
                //duration += DIAGONAL_HOO_TIME;
                ++diag;

                for (int col = 0; col < MAX_COLS; col++) {
                    cells[row - col][col] = YipeeBlockEvalGDX.addBrokenFlag(cells[row - col][col]);
                }
            }
        }

        updateBoard();
        return (horizontal + diag + vert - 1) + (horizontal * HORIZONTAL_HOO_TIME) + (diag * DIAGONAL_HOO_TIME) + (vert * VERTICAL_HOO_TIME);
        //return duration;
    }

    // Possible UI function
    public void placeBlockAt(YipeePieceGDX block, int x, int y) {
        this.piece = block;
        int index = block.getIndex();

        int v0 = block.getValueAt(index % 3);
        v0 = YipeeBlockEvalGDX.setIDFlag(v0, incrementID());

        int v1 = block.getValueAt((1 + index) % 3);
        v1 = YipeeBlockEvalGDX.setIDFlag(v1, incrementID());

        int v2 = block.getValueAt((2 + index) % 3);
        v2 = YipeeBlockEvalGDX.setIDFlag(v2, incrementID());

        if (YipeeBlockEvalGDX.getCellFlag(cells[y][x]) != YipeeBlockGDX.CLEAR_BLOCK) {
            //Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                + " isn't empty for piece placement");
        }
        if (YipeeBlockEvalGDX.getCellFlag(cells[y + 1][x]) != YipeeBlockGDX.CLEAR_BLOCK) {
            //Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                + " isn't empty for piece placement");
        }
        if (YipeeBlockEvalGDX.getCellFlag(cells[y + 2][x]) != YipeeBlockGDX.CLEAR_BLOCK) {
            //Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                + " isn't empty for piece placement");
        }
        cells[y][x] = v2;
        cells[y + 1][x] = v1;
        cells[y + 2][x] = v0;

        //flag broken cells and get updated broken count
        updateBoard();
    }

    //Medusa or Midas
    public void handlePlacedPowerBlock(int type) {
        for (int y = 0; y < MAX_ROWS; y++) {
            for (int x = 0; x < MAX_COLS; x++) {
                if (YipeeBlockEvalGDX.hasPowerBlockFlag(cells[y][x])) {
                    if (isCellInBoard(x - 1, y - 1))
                        applyPowerBlockAt(type, x - 1, y - 1);
                    if (isCellInBoard(x, y - 1))
                        applyPowerBlockAt(type, x, y - 1);
                    if (isCellInBoard(x + 1, y - 1))
                        applyPowerBlockAt(type, x + 1, y - 1);
                    if (isCellInBoard(x - 1, y))
                        applyPowerBlockAt(type, x - 1, y);
                    if (isCellInBoard(x, y))
                        applyPowerBlockAt(type, x, y);
                    if (isCellInBoard(x + 1, y))
                        applyPowerBlockAt(type, x + 1, y);
                    if (isCellInBoard(x - 1, y + 1))
                        applyPowerBlockAt(type, x - 1, y + 1);
                    if (isCellInBoard(x, y + 1))
                        applyPowerBlockAt(type, x, y + 1);
                    if (isCellInBoard(x + 1, y + 1))
                        applyPowerBlockAt(type, x + 1, y + 1);
                }
            }
        }
    }

    void applyPowerBlockAt(int value, int col, int row) {
        if (!YipeeBlockEvalGDX.hasPowerBlockFlag(value)) {
            System.out.println("Assertion failure:  cell isn't weird " + value);
        } else if (YipeeBlockEvalGDX.getCellFlag(value) != YipeeBlockGDX.Oy_BLOCK) {
            System.out.println("Assertion failure:  cell isn't weird type " + value);
        } else if (!YipeeBlockEvalGDX.hasPowerBlockFlag(cells[row][col])) {
            boolean isAttack = YipeeBlockEvalGDX.isOffensive(value);

            if (isAttack) {
                if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < YipeeBlockGDX.CLEAR_BLOCK) {
                    releaseID(YipeeBlockEvalGDX.getID(cells[row][col]));
                }

                if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) != YipeeBlockGDX.CLEAR_BLOCK) {
                    cells[row][col] = YipeeBlockGDX.STONE;
                }
            } else if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < YipeeBlockGDX.CLEAR_BLOCK) {
                cells[row][col] = YipeeBlockEvalGDX.addArtificialFlag(YipeeBlockEvalGDX.setValueFlag(cells[row][col], YipeeBlockGDX.Oy_BLOCK));
            }

            updateBoard();
        }
    }

    public void setValueAt(int value, int column, int row) {
        if (YipeeBlockEvalGDX.getCellFlag(cells[row][column]) != MAX_COLS) {
            System.out.println("Assertion failure: grid at " + column + "," + row + " isn't empty for cell placement");
        }
        value = YipeeBlockEvalGDX.setIDFlag(value, incrementID());
        cells[row][column] = value;
        updateBoard();
    }

    public boolean hasPlayerDied() {
        /*
        for (int row = MAX_PLAYABLE_ROWS; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (getPieceValue(col, row) != YokelBlock.CLEAR_BLOCK)
                    return true;
            }
        }*/
        return getPieceValue(2, 12) != YipeeBlockGDX.CLEAR_BLOCK;
    }

    public int getBrokenCellCount() {
        int count = 0;

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                if (YipeeBlockEvalGDX.hasBrokenFlag(cells[i][j]))
                    count++;
            }
        }

        return count;
    }

    public Array<YipeeBlockGDX> getBrokenCells() {
        Array<YipeeBlockGDX> vector = new Array<>();

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                if (YipeeBlockEvalGDX.hasBrokenFlag(cells[i][j])) {
                    int cell = cells[i][j];
                    YipeeBlockGDX block = new YipeeBlockGDX(j, i, YipeeBlockEvalGDX.getCellFlag(cell));
                    if (YipeeBlockEvalGDX.hasPowerBlockFlag(cell)) {
                        block.setPowerIntensity(YipeeBlockEvalGDX.getPowerFlag(cell));
                    }
                    vector.add(block);
                }
            }
        }
        return vector;
    }

    /*
    public void flagBrokenCells(ByteStack stack) {
        int index = 0;

        while (index < stack.length()) {
            int count = 0;

            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                        int id = YokelBlockEval.getID(cells[row][col]);

                        if (stack.getValueAt(index) == id) {
                            count++;
                        }
                    }
                }
            }

            switch (count) {
                default:
                    System.out.println("fucked up, found " + count
                            + " instances of id " + stack.getValueAt(index));
                    /* fall through *//*
                case 0:
                case 1:
                    index++;
            }
        }

        for (int i = 0; i < stack.length(); i++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YokelBlockEval.getCellFlag(cells[row][col]) < MAX_COLS
                            && stack.getValueAt(i) == YokelBlockEval.getID(cells[row][col])) {

                        cells[row][col] = YokelBlockEval.addBrokenFlag(cells[row][col]);
                    }
                }
            }
        }

        updateBoard();
    }*/

    /*
    public ByteStack getBrokenByPartnerCellIDs() {
        ByteStack stack = new ByteStack();

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YokelBlockEval.hasPartnerBreakFlag(cells[row][col])) {
                    stack.push(YokelBlockEval.getID(cells[row][col]));
                }
            }
        }

        return stack;
    }*/

    public Queue<Integer> getBrokenByPartnerCellIDs() {
        Queue<Integer> stack = new Queue<>();

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YipeeBlockEvalGDX.hasPartnerBreakFlag(cells[row][col])) {
                    stack.addFirst(YipeeBlockEvalGDX.getID(cells[row][col]));
                }
            }
        }

        return stack;
    }

    public void flagBrokenCells(Queue<Integer> stack) {
        int index = 0;

        while (index < LibGDXUtil.sizeOf(stack)) {
            int count = 0;

            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < MAX_COLS) {
                        int id = YipeeBlockEvalGDX.getID(cells[row][col]);

                        if (stack.get(index) == id) {
                            count++;
                        }
                    }
                }
            }

            switch (count) {
                default:
                    System.out.println("fucked up, found " + count
                        + " instances of id " + stack.get(index));
                    /* fall through */
                case 0:
                case 1:
                    index++;
            }
        }

        for (int i = 0; i < LibGDXUtil.sizeOf(stack); i++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YipeeBlockEvalGDX.getCellFlag(cells[row][col]) < MAX_COLS
                        && stack.get(i) == YipeeBlockEvalGDX.getID(cells[row][col])) {

                        cells[row][col] = YipeeBlockEvalGDX.addBrokenFlag(cells[row][col]);
                    }
                }
            }
        }

        updateBoard();
    }


    // Possible UI Function
    public void getCellsToBeDropped() {
        Arrays.fill(targetRows, 0);

        for (int y = 0; y < MAX_ROWS; y++) {
            for (int x = 0; x < MAX_COLS; x++) {
                // The important thing to note is that the targetRow will not get
                // incremented when a cell is to be broken.
                if (!isCellBroken(x, y)) {
                    if (targetRows[x] != y && getPieceValue(x, y) != MAX_COLS) {
                        cellsToDrop.add(new YipeeBlockMoveGDX(cells[y][x], YipeeBlockEvalGDX.getCellFlag(cells[y][x]), x, y, targetRows[x]));
                    }
                    targetRows[x]++;
                }
            }
        }
        //state.setCellsToDrop(cellsToDrop);
    }

    public void checkBoardForPartnerBreaks(YipeeGameBoardGDX partner, boolean isPartnerOnRight) {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                checkCellForPartnerBreak(partner, isPartnerOnRight, col, row);
            }
        }
    }

    void checkCellForPartnerBreak(YipeeGameBoardGDX board, boolean isPartnerOnRight, int col, int row) {
        //boolean bool_53_ = true;
        int colOffset;

        if (isPartnerOnRight)
            colOffset = 1;
        else
            colOffset = -1;

        if (getPieceValue(col, row) < MAX_COLS) {
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, -1);
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, 0);
            checkCellForNonVerticalPartnerBreaks(board, col, row, colOffset, 1);
        }
    }

    void checkCellForNonVerticalPartnerBreaks(YipeeGameBoardGDX partner, int col, int row, int _x, int _y) {
        int value = getPieceValue(col, row);

        int matchCount;

        // Tally up matches on this board
        for (matchCount = 1;
             (isCellInBoard(col + matchCount * _x, row + matchCount * _y)
                 && getPieceValue(col + matchCount * _x, row + matchCount * _y) == value
                 && !YipeeBlockEvalGDX.hasPowerBlockFlag(cells[row + matchCount * _y][col + matchCount * _x]));
             matchCount++) {
            /* empty */
        }

        // If we're moving to partner side
        if (!isCellInBoard(col + matchCount * _x, row + matchCount * _y)) {
            int partnerMatchCount = 0;
            int pX;

            if (_x > 0)
                pX = -(matchCount * _x);
            else
                pX = 5 - matchCount * _x;

            for (/**/;
                     (isCellInBoard(pX + matchCount * _x, row + matchCount * _y)
                         && partner.getPieceValue(pX + matchCount * _x, row + matchCount * _y) == value
                         && !YipeeBlockEvalGDX.hasPowerBlockFlag(partner.cells[row + matchCount * _y][pX + matchCount * _x]));
                     matchCount++) {
                partnerMatchCount++;
            }


            if (partnerMatchCount != 0) {
                if (matchCount >= 3) {
                    for (int i = 0; i < matchCount; i++) {
                        int y = row + i * _y;
                        int x = col + i * _x;

                        if (isCellInBoard(x, y)) {
                            int copy = cells[y][x];
                            copy = YipeeBlockEvalGDX.addPartnerBreakFlag(copy);
                            cells[y][x] = copy;
                        } else {
                            x = pX + i * _x;
                            int copy = partner.cells[y][x];
                            copy = YipeeBlockEvalGDX.addPartnerBreakFlag(copy);
                            partner.cells[y][x] = copy;
                        }
                    }
                    updateBoard();
                }
            }
        }
    }

    boolean checkForNonVerticalYahoo(int y, int _y) {
        boolean result = true;

        int row = y;

        if (YipeeBlockEvalGDX.getCellFlag(cells[row][0]) != YipeeBlockGDX.Y_BLOCK) {
            result = false;
        }

        row += _y;

        if (YipeeBlockEvalGDX.getCellFlag(cells[row][1]) != YipeeBlockGDX.A_BLOCK) {
            result = false;
        }

        row += _y;

        if (YipeeBlockEvalGDX.getCellFlag(cells[row][2]) != YipeeBlockGDX.H_BLOCK) {
            result = false;
        }

        row += _y;

        if (YipeeBlockEvalGDX.getCellFlag(cells[row][3]) == YipeeBlockGDX.Op_BLOCK) {
            if (YipeeBlockEvalGDX.getCellFlag(cells[row + _y][4]) != YipeeBlockGDX.Oy_BLOCK)
                result = false;
        } else {
            if (YipeeBlockEvalGDX.getCellFlag(cells[row][3]) != YipeeBlockGDX.Oy_BLOCK)
                result = false;
            if (YipeeBlockEvalGDX.getCellFlag(cells[row + _y][4]) != YipeeBlockGDX.Op_BLOCK)
                result = false;
        }
        row += 2 * _y;
        if (YipeeBlockEvalGDX.getCellFlag(cells[row][5]) != YipeeBlockGDX.EX_BLOCK)
            result = false;

        return result;
    }

    /**
     * Custom Helper methods
     */

    public String toString() {
        return YipeeGDXPrinter.toStringYipeeBoard(this);
    }

    boolean isPieceBlock(int row, int col) {
        return piece != null && piece.column == col && (piece.row == row || piece.row + 1 == row || piece.row + 2 == row);
    }

    int getPieceBlock(int row) {
        return piece.getValueAt(Math.abs(2 - (row - piece.row)));
    }

    private void clearCell(int r, int c) {
        cells[r][c] = YipeeBlockGDX.CLEAR_BLOCK;
    }

    public void updateGameState(float delta, YipeeGameBoardStateGDX state, YipeeGameBoardStateGDX partnerState) {
        importGameState(state, partnerState);
        update(delta);
    }

    private void update(float delta) {
        if (!hasGameStarted || hasPlayerDied()) {
            currentPhase = GamePhase.GAME_OVER;
            return;
        }

        switch (currentPhase) {
            case SPAWN_NEXT:
                if (piece == null) {
                    getNewNextPiece();
                    resetLockOutTimer(); //Important: gives player time to move piece
                    brokenCells.clear();
                    currentPhase = GamePhase.FALLING;
                }
                break;

            case FALLING:
                pieceFallTimer -= getCurrentFallRate();
                if (pieceFallTimer <= 0) {
                    if (isDownCellFree(piece.column, piece.row)) {
                        movePieceDown();
                        pieceFallTimer = MAX_FALL_VALUE;
                    } else {
                        currentPhase = GamePhase.LOCKING;
                        pieceLockTimer = MAX_FALL_VALUE;
                    }
                }
                break;

            case LOCKING:
                pieceLockTimer -= getCurrentFallRate();
                if (pieceLockTimer <= 0) {
                    setNextPiece();
                    piece = null;
                    checkForYahoos();
                    currentPhase = GamePhase.BREAKING;
                }
                break;

            case BREAKING:
                updateBoard();
                brokenBlockCount = getBrokenCellCount();
                if (brokenBlockCount > 0) {
                    handleBrokenCellDrops();
                    getCellsToBeDropped();
                    blockAnimationTimer = 1;
                    currentPhase = GamePhase.COLLAPSING;
                } else {
                    currentPhase = GamePhase.SPAWN_NEXT;
                }
                break;

            case COLLAPSING:
                blockAnimationTimer -= delta;
                if (blockAnimationTimer <= 0) {
                    for (YipeeBlockMoveGDX move : LibGDXUtil.safeIterable(cellsToDrop)) {
                        setCell(move.getTargetRow(), move.getCol(), move.getCellId());
                    }
                    cellsToDrop.clear();
                    resetAnimationTimer();
                    currentPhase = GamePhase.CASCADE_CHECK;
                }
                break;

            case CASCADE_CHECK:
                updateBoard();
                brokenBlockCount = getBrokenCellCount();
                if (brokenBlockCount > 0) {
                    currentPhase = GamePhase.BREAKING;
                } else {
                    currentPhase = GamePhase.SPAWN_NEXT;
                }
                break;

            case GAME_OVER:
                end();
                break;
        }
    }

    /*
    public void update(float delta) {
        //If the player is alive
        if (!hasPlayerDied() && hasGameStarted) {
            //if there are cells to break, handle
            //DROPPING ROWS

            //get block count
            brokenBlockCount = getBrokenCellCount();
            /*
            System.out.println("}}###}}brokenBlockCount=" + brokenBlockCount);
            System.out.println("}}###}}cellsToDrop.size=" + cellsToDrop.size);
            System.out.println("}}###}}blockAnimationTimer=" + blockAnimationTimer);


            if (brokenBlockCount > 0 || LibGDXUtil.size(cellsToDrop) > 0 || blockAnimationTimer < 1) {
                //System.out.println("}}}}drop cell block");
                //Reset Piece Set
                //setPieceSet(false);

                //Get rows to drop
                getCellsToBeDropped();
                //System.out.println("}}}}cellsToDrop size=" + cellsToDrop);

                //COUNT BROKEN BLOCKS AND BLOCKS TO DROP
                if (blockAnimationTimer == 1) {
                    //System.out.println("}}}}Animation = 1");
                    handleBrokenCellDrops();
                    if (LibGDXUtil.size(cellsToDrop) > 0) {
                        blockAnimationTimer -= 0.049;
                        //Clear dropped cell while animating
                        for (YipeeBlockMove move : LibGDXUtil.safeIterable(cellsToDrop)) {
                            if (move != null) {
                                clearCell(move.getRow(), move.getCol());
                            }
                        }
                    }
                } else {
                    //WAIT FOR ANIMATION OF DROPPED BLOCKS
                    if (blockAnimationTimer > 0 && blockAnimationTimer < 1) {
                        //System.out.println("}}}}Animating cells to drop");
                        blockAnimationTimer -= 0.049;
                        brokenCells.clear();
                    } else {
                        //System.out.println("}}}}reset broken Cells");
                        //Put moved cell back now that animation is complete
                        for (YipeeBlockMove move : LibGDXUtil.safeIterable(cellsToDrop)) {
                            if (move != null) {
                                setCell(move.getRow(), move.getCol(), move.getCellId());
                            }
                        }
                        //RESET STATE OF ANIMATIONS AND BROKEN BLOCKS AND CELLS TO DROP
                        cellsToDrop.clear();
                        resetAnimationTimer();
                    }
                }
            } else {
                //DROPPING PIECE
                //Check for more drops
                if (null == piece) {
                    //System.out.println("}}}}new Next Piece");
                    //Get new next piece;
                    getNewNextPiece();
                    resetLockOutTimer();
                    //Clear any broken cells at the start of a new cycle
                    brokenCells.clear();
                }

                //Check if it is time to move Piece down
                if (pieceFallTimer > 0) {
                    //System.out.println("-}}}}The Piece is falling in subpixels");
                    pieceFallTimer -= getCurrentFallRate();
                } else {

                    //Moving PIECE
                    //System.out.println("}}}}The Piece needs to fall to next row");
                    if (isDownCellFree(piece.column, piece.row)) {
                        //System.out.println("}}}}move 3piece down");

                        movePieceDown();
                    } else {
                        //System.out.println("}}}}The Piece CANNOT move down a row");
                        //SETTING PIECE

                        //check if lockOut time has expired
                        if (pieceLockTimer > 0) {
                            //System.out.println("}}}}lockout not met");
                            pieceLockTimer -= getCurrentFallRate();
                        } else {
                            //SET PIECE
                            //System.out.println("}}}}LOCKOUT!!  Setting piece");

                            //add piece to board
                            setNextPiece();
                            piece = null;

                            //Check for yahoos
                            //System.out.println("}}}}Check for yahoos. (adds broken flag)");
                            checkForYahoos();
                        }
                    }
                }
            }
        }
        //setGameState();
    }*/

    private void dropCellRows(Array<YipeeBlockMoveGDX> cellsToDrop) {
        //Remove matches

        if (LibGDXUtil.sizeOf(cellsToDrop) > 0) {
            System.out.println("Time to do some row dropping");

            for (YipeeBlockMoveGDX blockMove : cellsToDrop) {
                System.out.println(blockMove);
                clearCell(blockMove.getRow(), blockMove.getCol());
                cells[blockMove.getTargetRow()][blockMove.getCol()] = blockMove.getBlock();
            }
            //TODO: Do some dropping
        }
    }

    private float getCurrentFallRate() {
        if (fastDown) {
            return FAST_FALL_RATE;
        } else {
            return FALL_RATE;
        }
    }

    private YipeeGameBoardGDX getPartnerBoard() {
        YipeeGameBoardGDX partnerBoard = null;
        if (hasPartner) {
            partnerBoard = new YipeeGameBoardGDX();
            partnerBoard.setCells(partnerCells);
        }
        return partnerBoard;
    }

    private void updateBoard() {
        //System.out.println("flagging board matches");
        flagBoardMatches();

        if (hasPartner) {
            checkBoardForPartnerBreaks(getPartnerBoard(), isPartnerRight);
        } else {
            System.out.println("Assertion Error: Partner Board is null, was it set?");
        }
        //System.out.println("Board matches flagged");
    }

    private void movePieceDown() {
        //logger.error("Enter movePieceDown()");
        //System.out.println("Enter movePieceDown()");
        if (piece != null) {
            //Check if time to move piece to next row
            if (isDownCellFree(piece.column, piece.row)) {
                //logger.debug("DownCell is Free");
                this.pieceFallTimer = MAX_FALL_VALUE;
                piece.setPosition(piece.row - 1, piece.column);
            }
        }
        //System.out.println("Exit movePieceDown()");
        //logger.error("Exit movePieceDown()");
        //return successful;
    }

    public void movePieceRight() {
        if (isMovingValid()) {
            if (isRightCellFree(piece.column, piece.row)) {
                resetLockOutTimer();
                piece.setPosition(piece.row, piece.column + 1);
            }
        }
    }

    public void movePieceLeft() {
        if (isMovingValid()) {
            if (isLeftCellFree(piece.column, piece.row)) {
                resetLockOutTimer();
                piece.setPosition(piece.row, piece.column - 1);
            }
        }
    }

    public void cycleDown() {
        if (isMovingValid()) {
            piece.cycleDown();
        }
    }

    public void cycleUp() {
        if (isMovingValid()) {
            piece.cycleUp();
        }
    }

    private boolean isMovingValid() {
        return piece != null && pieceLockTimer > 0;
    }

    public void startMoveDown() {
        fastDown = true;
    }

    public void stopMoveDown() {
        fastDown = false;
    }

    public int getNextBlock() {
        return nextBlocks.getRandomNumberAt(++currentBlockPointer);
    }

    public void setNextPiece() {
        if (piece != null) {
            int block = YipeeBlockEvalGDX.getCellFlag(piece.getBlock1());

            if (block == YipeeBlockGDX.MEDUSA) {
                piece.setBlock1(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Oy_BLOCK, 3)));
                piece.setBlock2(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Oy_BLOCK, 3)));
                piece.setBlock3(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Oy_BLOCK, 3)));
            } else if (block == YipeeBlockGDX.TOP_MIDAS || block == YipeeBlockGDX.MID_MIDAS || block == YipeeBlockGDX.BOT_MIDAS) {
                piece.setBlock1(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Oy_BLOCK, 2)));
                piece.setBlock2(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Oy_BLOCK, 2)));
                piece.setBlock3(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.Oy_BLOCK, 2)));
            }

            //Place piece
            placeBlockAt(piece, piece.column, piece.row);
            //state.setPieceSet(true);

            //Handle special O then remove powers from placed block so they can be marked broken
            if (block == YipeeBlockGDX.MEDUSA || block == YipeeBlockGDX.TOP_MIDAS || block == YipeeBlockGDX.MID_MIDAS || block == YipeeBlockGDX.BOT_MIDAS) {
                handlePlacedPowerBlock(piece.getBlock1());
                cells[piece.row][piece.column] = YipeeBlockEvalGDX.setIDFlag(YipeeBlockGDX.Oy_BLOCK, YipeeBlockEvalGDX.getID(cells[piece.row][piece.column]));
                cells[piece.row + 1][piece.column] = YipeeBlockEvalGDX.setIDFlag(YipeeBlockGDX.Oy_BLOCK, YipeeBlockEvalGDX.getID(cells[piece.row + 1][piece.column]));
                cells[piece.row + 2][piece.column] = YipeeBlockEvalGDX.setIDFlag(YipeeBlockGDX.Oy_BLOCK, YipeeBlockEvalGDX.getID(cells[piece.row + 2][piece.column]));
            }

            resetPiece();
        }
    }

    public int peekSpecialQueue() {
        return specialPieces.first();
    }

    public void addSpecialPiece(int piece) {
        if (piece > 2 || piece < 1) {
            System.out.println("Assertion Error: invalid special block: " + piece);
            return;
        }
        specialPieces.addLast(piece);
    }

    public void getNewNextPiece() {
        int isSpecial = 0;

        //Pop a special next piece if it exists
        if (!LibGDXUtil.isEmpty(specialPieces)) {
            isSpecial = specialPieces.removeFirst();
        }

        if (nextPiece == null) {
            this.nextPiece = newPieceBock(isSpecial);
        }

        if (isSpecial == 0) {
            this.piece = nextPiece;
            this.nextPiece = newPieceBock(isSpecial);
        } else {
            this.piece = newPieceBock(isSpecial);
        }
    }

    private YipeePieceGDX newPieceBock(int isSpecial) {
        int block1;
        int block2;
        int block3;

        if (isSpecial > 0) {
            if (isSpecial % 2 == 1) {
                block1 = YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.MEDUSA, 3));
                block2 = YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.MEDUSA, 3));
                block3 = YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.MEDUSA, 3));
            } else {
                block1 = YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.BOT_MIDAS, 2));
                block2 = YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.MID_MIDAS, 2));
                block3 = YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(YipeeBlockGDX.TOP_MIDAS, 2));
            }
        } else {
            block1 = powerUpBlock(getNextBlock());
            block2 = powerUpBlock(getNextBlock());
            block3 = powerUpBlock(getNextBlock());
        }

        YipeePieceGDX piece = new YipeePieceGDX(getIdIndex(), block1, block2, block3);
        pieceFallTimer = MAX_FALL_VALUE;
        piece.setPosition(MAX_PLAYABLE_ROWS, 2);
        return piece;
    }

    //TODO: Make this safe
    private int powerUpBlock(int block) {
        //System.out.println("powersk: " + Arrays.toString(powersKeep));
        //System.out.println("countOfBreaks: " + Arrays.toString(countOfBreaks));
        int breakCount = 4;
        if (countOfBreaks[block] > breakCount - 1) {
            //powerUp
            countOfBreaks[block] -= breakCount;
            ++powersKeep[block];
            block = getBlockPower(block, powersKeep[block]);
        }
        return block;
    }

    private int getBlockPower(int block, int intensity) {
        if (intensity == 1) {
            intensity = 3;
        }
        if (intensity > 7) {
            intensity = 7;
        }
        return YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(block, intensity));
    }

    public void incrementBreakCount(int type) {
        if (type < 0 || type > MAX_COLS) return;
        ++countOfBreaks[type];
    }

    public void addPowerToQueue(int block) {
        //logger.debug("Enter addPowerToQueue(block=" + block + ")");
        //int b = block.getBlockType();
        if (YipeeBlockEvalGDX.hasPowerBlockFlag(block)) {
            int intensity = YipeeBlockEvalGDX.getPowerFlag(block);
            block = YipeeBlockEvalGDX.removeBrokenFlag(block);
            //logger.debug("intensity=" + intensity);
            powers.addLast(YipeeBlockEvalGDX.addPowerBlockFlag(YipeeBlockEvalGDX.setPowerFlag(block, intensity)));
        }
        //logger.debug("current queue=" + powers);
        //logger.debug("Exit addPowerToQueue()");
    }


    public int popPowerFromQueue() {
        int powerBlock = -1;
        if (!LibGDXUtil.isEmpty(powers)) {
            powerBlock = powers.removeFirst();
        }
        return powerBlock;
    }


    private void checkForYahoos() {
        int tempDuration = getYahooDuration();

        //If Yahoo is on when next piece is set, reduce count
        if (yahooDuration > 0) {
            System.out.println("Subtracting Yahoo Count by 1");
            yahooDuration--;
        }

        if (tempDuration > 0) {
            System.out.println("Adding to current Yahoo Count");
            yahooDuration += (tempDuration - 1);
        }
    }
}
