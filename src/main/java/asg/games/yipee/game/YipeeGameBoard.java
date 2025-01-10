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

import asg.games.yipee.objects.Disposable;
import asg.games.yipee.objects.YipeeBlock;
import asg.games.yipee.objects.YipeeBlockMove;
import asg.games.yipee.objects.YipeeBrokenBlock;
import asg.games.yipee.objects.YipeeClock;
import asg.games.yipee.objects.YipeeGameBoardState;
import asg.games.yipee.objects.YipeePiece;
import asg.games.yipee.tools.RandomUtil;
import asg.games.yipee.tools.TimeUtils;
import asg.games.yipee.tools.Util;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

/**
 * Represents the game board.
 * All logic happens here
 *
 * @author Blakbro2k
 */
@Getter
@Setter
public class YipeeGameBoard implements Disposable {
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

    //private final YokelPiece MEDUSA_PIECE = new YokelPiece(0, YokelBlock.MEDUSA, YokelBlock.MEDUSA, YokelBlock.MEDUSA);
    //private final YokelPiece MIDAS_PIECE = new YokelPiece(0, YokelBlock.BOT_MIDAS, YokelBlock.MID_MIDAS, YokelBlock.TOP_MIDAS);

    private int[][] cells;
    private boolean[] ids;
    private int idIndex;
    private static final int[] targetRows = new int[MAX_COLS];
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

    private YipeePiece piece;
    private YipeePiece nextPiece;
    private YipeeClock gameClock;

    private float pieceFallTimer;
    private float pieceLockTimer;
    private float blockAnimationTimer;
    private RandomUtil.RandomNumberArray nextBlocks;
    private int currentBlockPointer = -1;
    private boolean fastDown;
    private Queue<Integer> powers = new LinkedList<>();
    private Queue<Integer> specialPieces = new LinkedList<>();
    Queue<YipeeBrokenBlock> brokenCells = new LinkedList<>();
    Queue<YipeeBlockMove> cellsToDrop = new LinkedList<>();


    private int yahooDuration = 0;
    private int brokenBlockCount = 0;
    private boolean hasGameStarted = false;

    private YipeeGameBoard partnerBoard = null;
    private boolean isPartnerRight = true;
    private boolean debug = false;
    private String name = null;

    private static final YipeeGameBoardState state = new YipeeGameBoardState();

    //Empty Constructor required for Json.Serializable
    public YipeeGameBoard() {
    }

    public YipeeGameBoard(long seed) {
        cells = new int[MAX_ROWS][MAX_COLS];
        ids = new boolean[128];
        powers = new LinkedList<>();
        specialPieces = new LinkedList<>();
        gameClock = new YipeeClock();
        reset(seed);
        setGameState();
    }

    private void loadFromState(YipeeGameBoardState state) {
        if (state != null) {
            setBrokenBlockCount(state.getBrokenBlockCount());
            setFastDown(state.isFastDown());
            setCurrentBlockPointer(state.getCurrentBlockPointer());
            setNextBlocks(state.getNextBlocks());
            setCountOfBreaks(state.getCountOfBreaks());
            setPowersKeep(state.getPowersKeep());
            setGameClock(state.getGameClock());
            setIds(state.getIds());
            setIdIndex(state.getIdIndex());
            setDebug(state.isDebug());
            setName(state.getName());
            setPiece(state.getPiece());
            setNextPiece(state.getNextPiece());
            setCells(state.getPlayerCells());
            setPieceFallTimer(state.getPieceFallTimer());
            setPieceLockTimer(state.getPieceLockTimer());
            setBlockAnimationTimer(state.getBlockAnimationTimer());
            setYahooDuration(state.getYahooDuration());
            setPartnerRight(state.isPartnerRight());
            setPowers(state.getPowers());
            setBrokenCells(state.getBrokenCells());
            setSpecialPieces(state.getSpecialPieces());
            setHasGameStarted(state.isHasGameStarted());
            setPartnerBoard(state.getPartnerBoard());
        }
    }

    private void setGameState() {
        state.setBrokenBlockCount(brokenBlockCount);
        state.setFastDown(fastDown);
        state.setCurrentBlockPointer(currentBlockPointer);
        state.setNextBlocks(nextBlocks);
        state.setCountOfBreaks(countOfBreaks);
        state.setPowersKeep(powersKeep);
        state.setGameClock(gameClock);
        state.setIds(ids);
        state.setIdIndex(idIndex);
        state.setDebug(debug);
        state.setName(name);
        state.setCurrentStateTimeStamp(TimeUtils.nanoTime());
        state.setPiece(piece);
        state.setNextPiece(nextPiece);
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
        if (partnerBoard != null) {
            state.setPartnerBoard(partnerBoard.getGameState());
        } else {
            state.setPartnerBoard(null);
        }
    }

    public void setDebug(boolean isDebug) {
        this.debug = isDebug;
    }

    public YipeeGameBoardState getGameState() {
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

    public void setPartnerBoard(YipeeGameBoard partnerB, boolean b) {
        this.partnerBoard = partnerB;
        this.isPartnerRight = b;
    }

    public void setPartnerBoard(YipeeGameBoardState partnerB) {
        this.partnerBoard.updateState(partnerB);
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class TestRandomBlockArray extends RandomUtil.RandomNumberArray {
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
            nextBlocks = new RandomUtil.RandomNumberArray(MAX_RANDOM_BLOCK_NUMBER, seed, MAX_COLS);
        }

        gameClock.stop();
        clearBoard();
        resetPiece();
        Util.clearArrays(powers);
        end();
    }

    @Override
    public void dispose() {
        Util.clearArrays(specialPieces, powers);
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

    public YipeePiece getNextPiece() {
        return nextPiece;
    }

    public int[][] getCells() {
        return cells;
    }

    public void setCell(int row, int col, int cell) {
        cells[row][col] = cell;
    }

    public int getPieceValue(int c, int r) {
        return YipeeBlockEval.getCellFlag(cells[r][c]);
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
        return YipeeBlockEval.hasAddedByYahooFlag(cells[row][column]);
    }

    public boolean isCellBroken(int column, int row) {
        return YipeeBlockEval.hasBrokenFlag(cells[row][column]);
    }

    public void setValueWithID(int column, int row, int value) {
        cells[row][column] = YipeeBlockEval.setIDFlag(value, incrementID());
    }

    void addRow(int amount) {
        for (int row = MAX_PLAYABLE_ROWS; row >= amount; row--) {
            for (int col = 0; col < MAX_COLS; col++)
                cells[row][col] = cells[row - amount][col];
        }

        for (int row = 0; row < amount; row++) {
            for (int col = 0; col < MAX_COLS; col++)
                cells[row][col] = YipeeBlock.CLEAR_BLOCK;
        }

        int hash = getBoardMakeupHash();

        for (int i = amount - 1; i >= 0; i--) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = getNonAdjacentCell(col, i, hash + i + col);

                value = YipeeBlockEval.setIDFlag(value, incrementID());

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
                if (row < amount && YipeeBlockEval.getCellFlag(cells[row][col]) < MAX_COLS)
                    releaseID(YipeeBlockEval.getID(cells[row][col]));

                cells[row][col] = cells[row + amount][col];
            }
        }

        for (int row = MAX_PLAYABLE_ROWS - amount; row < MAX_PLAYABLE_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                cells[row][col] = YipeeBlock.CLEAR_BLOCK;
            }
        }

        updateBoard();
    }

    void shuffleColumnIndices() {
        for (int i = 0; i < MAX_COLS; i++) {
            randomColumnIndices[i] = i;
        }

        RandomUtil.RandomNumber generator = new RandomUtil.RandomNumber((long) getBoardMakeupHash());

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
                    cells[y][x] = YipeeBlock.STONE;
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
                if (getPieceValue(x, y) == YipeeBlock.STONE) {
                    for (int i = y; i >= 1; i--) {
                        cells[i][x] = cells[i - 1][x];
                    }

                    cells[0][x] = YipeeBlock.STONE;

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
                if (YipeeBlockEval.getCellFlag(cells[row][col]) == YipeeBlock.Op_BLOCK
                        // And the piece is a power
                        && YipeeBlockEval.getPowerFlag(cells[row][col]) != 0) {

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

                if (row >= 0 && YipeeBlockEval.getCellFlag(cells[row][col]) != YipeeBlock.STONE) {
                    if (YipeeBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                        releaseID(YipeeBlockEval.getID(cells[row][col]));
                    }

                    cells[row][col] = YipeeBlockEval.setIDFlag(value, incrementID());
                    return;
                }
            }
        }

        updateBoard();
    }

    void colorBlast() {
        markColorBlast();

        if (isColorBlastGridEmpty()) {
            pushCellToBottomOfBoard(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.DEFENSIVE_MINOR)));
        } else {
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (colorBlastGrid[row][col]) {
                        if (YipeeBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                            releaseID(YipeeBlockEval.getID(cells[row][col]));
                        }

                        cells[row][col] = YipeeBlockEval.setIDFlag(YipeeBlock.Op_BLOCK, incrementID());
                    }
                }
            }

            for (int col = 0; col < MAX_COLS; col++) {
                boolean bool = false;

                for (int row = 15; row >= 0; row--) {
                    if (YipeeBlockEval.getCellFlag(cells[row][col]) == MAX_COLS) {
                        if (bool) {
                            cells[row][col] = YipeeBlockEval.setIDFlag(YipeeBlock.Op_BLOCK, incrementID());
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
                if (YipeeBlockEval.getCellFlag(cells[row][col]) == YipeeBlock.Op_BLOCK
                        && YipeeBlockEval.getPowerFlag(cells[row][col]) != YipeeBlock.Y_BLOCK) {

                    if (YipeeBlockEval.getCellFlag(cells[row][col]) < MAX_COLS)
                        releaseID(YipeeBlockEval.getID(cells[row][col]));

                    if (YipeeBlockEval.getCellFlag(cells[row][col]) != MAX_COLS)
                        cells[row][col] = YipeeBlock.STONE;

                    if (++cellsDefused == intensity)
                        return;
                }
            }
        }

        updateBoard();

        for (int i = cellsDefused; i < intensity; i++) {
            pushCellToBottomOfBoard(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Op_BLOCK, YipeeBlock.OFFENSIVE_MINOR)));
        }
    }

    void removeColorFromBoard() {
        // Clear the count
        Arrays.fill(countOfPieces, 0);

        // Loop through the board and tally up count of cells
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                int value = YipeeBlockEval.getCellFlag(cells[row][col]);

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
                if (YipeeBlockEval.getCellFlag(cells[row][col]) == index) {
                    cells[row][col] = YipeeBlockEval.addBrokenFlag(cells[row][col]);
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
        //logger.debug("Enter handleBrokenCellDrops()");
        for (int col = 0; col < MAX_COLS; col++) {
            int index = 0;

            for (int row = 0; row < MAX_ROWS; row++) {

                if (isCellBroken(col, row)) {
                    if (YipeeBlockEval.getCellFlag(cells[row][col]) < MAX_COLS)
                        releaseID(YipeeBlockEval.getID(cells[row][col]));
                    incrementBreakCount(YipeeBlockEval.getCellFlag(cells[row][col]));
                    addPowerToQueue(cells[row][col]);
                    brokenCells.offer(new YipeeBrokenBlock(YipeeBlockEval.getCellFlag(cells[row][col]), row, col));
                } else {
                    cells[index][col] = cells[row][col];
                    index++;
                }
            }

            for (; index < MAX_ROWS; index++) {
                cells[index][col] = YipeeBlock.CLEAR_BLOCK;
            }
        }
        updateBoard();
        //logger.debug("Exit handleBrokenCellDrops()");
    }

    public void flagPowerBlockCells() {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YipeeBlockEval.hasPowerBlockFlag(cells[row][col])) {
                    cells[row][col] = YipeeBlockEval.addBrokenFlag(cells[row][col]);
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

    void flagCellForMatches(int x, int y, int _x, int _y) {//System.out.println("##flagCellForMatches##");
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
                copy = YipeeBlockEval.addBrokenFlag(copy);
                cells[y + i * _y][x + i * _x] = copy;
            }
        }
    }

    public void handlePower(int i) {
        if (YipeeBlockEval.getPowerFlag(i) == 0) {
            switch (i) {
                case YipeeBlock.SPECIAL_BLOCK_1:
                    removeAllPowersFromBoard();
                    break;
                case YipeeBlock.SPECIAL_BLOCK_2:
                    removeAllStonesFromBoard();
                    break;
                default:
                    System.out.println("Assertion failure: invalid rare attack " + i);
                    break;
            }
        } else {
            boolean isOffensive = YipeeBlockEval.isOffensive(i);
            int level = YipeeBlockEval.getPowerLevel(i);

            switch (YipeeBlockEval.getCellFlag(i)) {
                //Y
                case YipeeBlock.Y_BLOCK:
                    if (isOffensive) {
                        addRow(1);
                    } else {
                        removeRow(1);
                    }

                    break;
                case YipeeBlock.A_BLOCK:
                    if (isOffensive) {
                        dither(2 * Math.min(level, 3));
                    } else {
                        clump(2 * Math.min(level, 3));
                    }

                    break;
                case YipeeBlock.H_BLOCK:
                    if (isOffensive) {
                        addStone(Math.min(level, 3));
                    } else {
                        dropStone(Math.min(level, 3));
                    }

                    break;
                case YipeeBlock.Op_BLOCK:
                    if (isOffensive) {
                        defuse(Math.min(level, 3));
                    } else {
                        colorBlast();
                        break;
                    }

                    break;
                case YipeeBlock.Oy_BLOCK:
                    System.out.println("Assertion failure: invalid CELL5 board attack " + i);
                    break;
                case YipeeBlock.EX_BLOCK:
                    removeColorFromBoard();
                    break;
                default:
                    System.out.println("Assertion failure: invalid attack" + i);
            }
        }
    }

    public void addRemovedPowersToBoard(Stack<Integer> powers) {
        shuffleColumnIndices();

        int count = 0;

        while (powers.size() > 0) {
            int value = powers.pop();

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
                        cells[0][col] = YipeeBlockEval.setIDFlag(value, incrementID());
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

                if (YipeeBlockEval.getPowerFlag(value) != 0) {
                    cells[row][col] = YipeeBlockEval.setPowerFlag(value, 0);
                }
            }
        }
        updateBoard();
    }

    void removeAllStonesFromBoard() {
        for (int x = 0; x < MAX_COLS; x++) {
            int row = 0;

            for (int y = 0; y < MAX_ROWS; y++) {
                if (YipeeBlockEval.getCellFlag(cells[y][x]) != 7) {
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
                num += YipeeBlockEval.removePartnerBreakFlag(cells[row][col]) * (row * MAX_COLS + col);
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
        return row > 0 && row < MAX_PLAYABLE_ROWS + 1 && getPieceValue(column, row - 1) == YipeeBlock.CLEAR_BLOCK;
    }

    public boolean isRightCellFree(int column, int row) {
        return column < MAX_COLS - 1 && getPieceValue(column + 1, row) == YipeeBlock.CLEAR_BLOCK;
    }

    public boolean isLeftCellFree(int column, int row) {
        return column > 0 && getPieceValue(column - 1, row) == YipeeBlock.CLEAR_BLOCK;
    }

    public int getColumnWithPossiblePieceMatch(YipeePiece piece) {
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

        //Count Horizontals
        for (int row = 0; row < MAX_PLAYABLE_ROWS; row++) {
            if (checkForNonVerticalYahoo(row, 0)) {
                duration += HORIZONTAL_HOO_TIME;
                //++horizontal;

                for (int column = 0; column < MAX_COLS; column++) {
                    cells[row][column] = YipeeBlockEval.addBrokenFlag(cells[row][column]);
                }
            }
        }

        //Count Verticals
        for (int column = 0; column < MAX_COLS; column++) {
            for (int row = 0; row < 10; row++) {
                if (YipeeBlockEval.getCellFlag(cells[row][column]) == 5) {
                    if (YipeeBlockEval.getCellFlag(cells[row + 1][column]) == 4) {
                        if (YipeeBlockEval.getCellFlag(cells[row + 2][column]) != 3) {
                            continue;
                        }
                    } else if (YipeeBlockEval.getCellFlag(cells[row + 1][column]) != 3
                            || YipeeBlockEval.getCellFlag(cells[row + 2][column]) != 4) {
                        continue;
                    }

                    if (YipeeBlockEval.getCellFlag(cells[row + 3][column]) == 2
                            && YipeeBlockEval.getCellFlag(cells[row + 4][column]) == 1
                            && YipeeBlockEval.getCellFlag(cells[row + 5][column]) == 0) {

                        duration += VERTICAL_HOO_TIME;
                        //++vert;

                        for (int i = 0; i < MAX_COLS; i++) {
                            cells[row + i][column] = YipeeBlockEval.addBrokenFlag(cells[row + i][column]);
                        }
                    }
                }
            }
        }

        //Check Diagonals
        for (int row = 0; row < 8; row++) {
            if (checkForNonVerticalYahoo(row, 1)) {
                duration += DIAGONAL_HOO_TIME;
                //++diag;

                for (int col = 0; col < MAX_COLS; col++) {
                    cells[row + col][col] = YipeeBlockEval.addBrokenFlag(cells[row + col][col]);
                }
            }
        }

        for (int row = 5; row < MAX_PLAYABLE_ROWS; row++) {
            if (checkForNonVerticalYahoo(row, -1)) {
                duration += DIAGONAL_HOO_TIME;
                //++diag;

                for (int col = 0; col < MAX_COLS; col++) {
                    cells[row - col][col] = YipeeBlockEval.addBrokenFlag(cells[row - col][col]);
                }
            }
        }

        updateBoard();
        //return (horizontal + diag + vert - 1) + (horizontal * HORIZONTAL_HOO_TIME) + (diag * DIAGONAL_HOO_TIME) + (vert * VERTICAL_HOO_TIME);
        return duration;
    }

    public int getIdIndex() {
        return idIndex;
    }

    // Possible UI function
    public void placeBlockAt(YipeePiece block, int x, int y) {
        this.piece = block;
        int index = block.getIndex();

        int v0 = block.getValueAt(index % 3);
        v0 = YipeeBlockEval.setIDFlag(v0, incrementID());

        int v1 = block.getValueAt((1 + index) % 3);
        v1 = YipeeBlockEval.setIDFlag(v1, incrementID());

        int v2 = block.getValueAt((2 + index) % 3);
        v2 = YipeeBlockEval.setIDFlag(v2, incrementID());

        if (YipeeBlockEval.getCellFlag(cells[y][x]) != YipeeBlock.CLEAR_BLOCK) {
            //Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        if (YipeeBlockEval.getCellFlag(cells[y + 1][x]) != YipeeBlock.CLEAR_BLOCK) {
            //Thread.dumpStack();
            System.out.println("Assertion failure: grid at " + x + "," + y
                    + " isn't empty for piece placement");
        }
        if (YipeeBlockEval.getCellFlag(cells[y + 2][x]) != YipeeBlock.CLEAR_BLOCK) {
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
                if (YipeeBlockEval.hasPowerBlockFlag(cells[y][x])) {
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
        System.out.println("Special Type=" + value);
        System.out.println("Special col=" + col);
        System.out.println("Special row=" + row);

        if (!YipeeBlockEval.hasPowerBlockFlag(value)) {
            System.out.println("Assertion failure:  cell isn't weird " + value);
        } else if (YipeeBlockEval.getCellFlag(value) != YipeeBlock.Oy_BLOCK) {
            System.out.println("Assertion failure:  cell isn't weird type " + value);
        } else if (!YipeeBlockEval.hasPowerBlockFlag(cells[row][col])) {
            boolean isAttack = YipeeBlockEval.isOffensive(value);

            if (isAttack) {
                if (YipeeBlockEval.getCellFlag(cells[row][col]) < YipeeBlock.CLEAR_BLOCK) {
                    releaseID(YipeeBlockEval.getID(cells[row][col]));
                }

                if (YipeeBlockEval.getCellFlag(cells[row][col]) != YipeeBlock.CLEAR_BLOCK) {
                    cells[row][col] = YipeeBlock.STONE;
                }
            } else if (YipeeBlockEval.getCellFlag(cells[row][col]) < YipeeBlock.CLEAR_BLOCK) {
                cells[row][col] = YipeeBlockEval.addArtificialFlag(YipeeBlockEval.setValueFlag(cells[row][col], YipeeBlock.Oy_BLOCK));
            }

            updateBoard();
        }
    }

    public void setValueAt(int value, int column, int row) {
        if (YipeeBlockEval.getCellFlag(cells[row][column]) != MAX_COLS) {
            System.out.println("Assertion failure: grid at " + column + "," + row + " isn't empty for cell placement");
        }
        value = YipeeBlockEval.setIDFlag(value, incrementID());
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
        return getPieceValue(2, 12) != YipeeBlock.CLEAR_BLOCK;
    }

    public int getBrokenCellCount() {
        int count = 0;

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                if (YipeeBlockEval.hasBrokenFlag(cells[i][j]))
                    count++;
            }
        }

        return count;
    }

    public Vector<YipeeBlock> getBrokenCells() {
        Vector<YipeeBlock> vector = new Vector<>();

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLS; j++) {
                if (YipeeBlockEval.hasBrokenFlag(cells[i][j])) {
                    int cell = cells[i][j];
                    YipeeBlock block = new YipeeBlock(j, i, YipeeBlockEval.getCellFlag(cell));
                    if (YipeeBlockEval.hasPowerBlockFlag(cell)) {
                        block.setPowerIntensity(YipeeBlockEval.getPowerFlag(cell));
                    }
                    vector.addElement(block);
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

    public Stack<Integer> getBrokenByPartnerCellIDs() {
        Stack<Integer> stack = new Stack<>();

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                if (YipeeBlockEval.hasPartnerBreakFlag(cells[row][col])) {
                    stack.push(YipeeBlockEval.getID(cells[row][col]));
                }
            }
        }

        return stack;
    }

    public void flagBrokenCells(Stack<Integer> stack) {
        int index = 0;

        while (index < stack.size()) {
            int count = 0;

            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YipeeBlockEval.getCellFlag(cells[row][col]) < MAX_COLS) {
                        int id = YipeeBlockEval.getID(cells[row][col]);

                        if (stack.elementAt(index) == id) {
                            count++;
                        }
                    }
                }
            }

            switch (count) {
                default:
                    System.out.println("fucked up, found " + count
                            + " instances of id " + stack.elementAt(index));
                    /* fall through */
                case 0:
                case 1:
                    index++;
            }
        }

        for (int i = 0; i < stack.size(); i++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                for (int col = 0; col < MAX_COLS; col++) {
                    if (YipeeBlockEval.getCellFlag(cells[row][col]) < MAX_COLS
                            && stack.elementAt(i) == YipeeBlockEval.getID(cells[row][col])) {

                        cells[row][col] = YipeeBlockEval.addBrokenFlag(cells[row][col]);
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
                        cellsToDrop.offer(new YipeeBlockMove(cells[y][x], YipeeBlockEval.getCellFlag(cells[y][x]), x, y, targetRows[x]));
                    }
                    targetRows[x]++;
                }
            }
        }
        state.setCellsToDrop(cellsToDrop);
    }

    public void checkBoardForPartnerBreaks(YipeeGameBoard partner, boolean isPartnerOnRight) {
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                checkCellForPartnerBreak(partner, isPartnerOnRight, col, row);
            }
        }
    }

    void checkCellForPartnerBreak(YipeeGameBoard board, boolean isPartnerOnRight, int col, int row) {
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

    void checkCellForNonVerticalPartnerBreaks(YipeeGameBoard partner, int col, int row, int _x, int _y) {
        int value = getPieceValue(col, row);

        int matchCount;

        // Tally up matches on this board
        for (matchCount = 1;
             (isCellInBoard(col + matchCount * _x, row + matchCount * _y)
                     && getPieceValue(col + matchCount * _x, row + matchCount * _y) == value
                     && !YipeeBlockEval.hasPowerBlockFlag(cells[row + matchCount * _y][col + matchCount * _x]));
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
                             && !YipeeBlockEval.hasPowerBlockFlag(partner.cells[row + matchCount * _y][pX + matchCount * _x]));
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
                            copy = YipeeBlockEval.addPartnerBreakFlag(copy);
                            cells[y][x] = copy;
                        } else {
                            x = pX + i * _x;
                            int copy = partner.cells[y][x];
                            copy = YipeeBlockEval.addPartnerBreakFlag(copy);
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

        if (YipeeBlockEval.getCellFlag(cells[row][0]) != YipeeBlock.Y_BLOCK) {
            result = false;
        }

        row += _y;

        if (YipeeBlockEval.getCellFlag(cells[row][1]) != YipeeBlock.A_BLOCK) {
            result = false;
        }

        row += _y;

        if (YipeeBlockEval.getCellFlag(cells[row][2]) != YipeeBlock.H_BLOCK) {
            result = false;
        }

        row += _y;

        if (YipeeBlockEval.getCellFlag(cells[row][3]) == YipeeBlock.Op_BLOCK) {
            if (YipeeBlockEval.getCellFlag(cells[row + _y][4]) != YipeeBlock.Oy_BLOCK)
                result = false;
        } else {
            if (YipeeBlockEval.getCellFlag(cells[row][3]) != YipeeBlock.Oy_BLOCK)
                result = false;
            if (YipeeBlockEval.getCellFlag(cells[row + _y][4]) != YipeeBlock.Op_BLOCK)
                result = false;
        }
        row += 2 * _y;
        if (YipeeBlockEval.getCellFlag(cells[row][5]) != YipeeBlock.EX_BLOCK)
            result = false;

        return result;
    }

    /**
     * Custom Helper methods
     */

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("#################").append("\n");
        out.append("pieceFallTimer: ").append(pieceFallTimer).append("\n")
                .append("lockOutTimer: ").append(pieceLockTimer).append("\n")
                .append("brokenBlockCount: ").append(brokenBlockCount).append("\n")
                .append("Yahoo Count: ").append(yahooDuration).append("\n");

        if (piece != null) {
            out.append("player piece pos(").append(piece.column).append(",").append(piece.row).append(")").append("\n");
        }

        addPrintLine(out);
        for (int r = MAX_ROWS - 1; r > -1; r--) {
            printRow(out, r);
            printRowReturn(out);
        }
        addPrintLine(out);
        printRowReturn(out);
        out.append("#################").append("\n");
        return out.toString();
    }

    private void printRow(StringBuilder out, int r) {
        if (isPartnerRight) {
            printPlayerRows(this, partnerBoard, r, out);
        } else {
            printPlayerRows(partnerBoard, this, r, out);
        }
    }

    private void printPlayerRows(YipeeGameBoard boardLeft, YipeeGameBoard boardRight, int r, StringBuilder out) {
        for (int c = 0; c < MAX_COLS * 2; c++) {
            int block;
            if (c == MAX_COLS) {
                out.append('|');
            }
            if (c < MAX_COLS) {
                block = boardLeft.isPieceBlock(r, c) ? boardLeft.getPieceBlock(r) : boardLeft.getPieceValue(c, r);
                printGameLine(out, block);
            } else {
                block = boardRight.isPieceBlock(r, c - MAX_COLS) ? boardRight.getPieceBlock(r) : boardRight.getPieceValue(c - MAX_COLS, r);
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
        for (int a = 0; a < MAX_COLS * 2; a++) {
            sb.append("+");
            if (a == MAX_COLS) {
                sb.append("+");
            }
            sb.append("-");
        }
        sb.append('+').append('\n');
    }

    private void printRowReturn(StringBuilder out) {
        out.append("\n");
    }

    boolean isPieceBlock(int row, int col) {
        return piece != null && piece.column == col && (piece.row == row || piece.row + 1 == row || piece.row + 2 == row);
    }

    int getPieceBlock(int row) {
        return piece.getValueAt(Math.abs(2 - (row - piece.row)));
    }

    private void clearCell(int r, int c) {
        cells[r][c] = YipeeBlock.CLEAR_BLOCK;
    }

    public void update(YipeeGameBoardState state, float delta) {
        updateState(state);
        updateGame(delta);
    }

    private void updateState(YipeeGameBoardState state) {
        loadFromState(state);
    }

    private void updateGame(float delta) {
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
            */

            if (brokenBlockCount > 0 || Util.size(cellsToDrop) > 0 || blockAnimationTimer < 1) {
                //System.out.println("}}}}drop cell block");
                //Reset Piece Set
                state.setPieceSet(false);

                //Get rows to drop
                getCellsToBeDropped();
                //System.out.println("}}}}cellsToDrop size=" + cellsToDrop);

                //COUNT BROKEN BLOCKS AND BLOCKS TO DROP
                if (blockAnimationTimer == 1) {
                    //System.out.println("}}}}Animation = 1");
                    handleBrokenCellDrops();
                    if (Util.size(cellsToDrop) > 0) {
                        blockAnimationTimer -= 0.049;
                        //Clear dropped cell while animating
                        for (YipeeBlockMove move : Util.safeIterable(cellsToDrop)) {
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
                        for (YipeeBlockMove move : Util.safeIterable(cellsToDrop)) {
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
        setGameState();
    }

    private void dropCellRows(Vector<YipeeBlockMove> cellsToDrop) {
        //Remove matches

        if (cellsToDrop.size() > 0) {
            System.out.println("Time to do some row dropping");

            for (YipeeBlockMove blockMove : cellsToDrop) {
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

    private void updateBoard() {
        //System.out.println("flagging board matches");
        flagBoardMatches();
        if (partnerBoard != null) {
            checkBoardForPartnerBreaks(partnerBoard, isPartnerRight);
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
            int block = YipeeBlockEval.getCellFlag(piece.getBlock1());

            if (block == YipeeBlock.MEDUSA) {
                piece.setBlock1(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, 3)));
                piece.setBlock2(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, 3)));
                piece.setBlock3(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, 3)));
            } else if (block == YipeeBlock.TOP_MIDAS || block == YipeeBlock.MID_MIDAS || block == YipeeBlock.BOT_MIDAS) {
                piece.setBlock1(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, 2)));
                piece.setBlock2(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, 2)));
                piece.setBlock3(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.Oy_BLOCK, 2)));
            }

            //Place piece
            placeBlockAt(piece, piece.column, piece.row);
            state.setPieceSet(true);

            //Handle special O then remove powers from placed block so they can be marked broken
            if (block == YipeeBlock.MEDUSA || block == YipeeBlock.TOP_MIDAS || block == YipeeBlock.MID_MIDAS || block == YipeeBlock.BOT_MIDAS) {
                handlePlacedPowerBlock(piece.getBlock1());
                cells[piece.row][piece.column] = YipeeBlockEval.setIDFlag(YipeeBlock.Oy_BLOCK, YipeeBlockEval.getID(cells[piece.row][piece.column]));
                cells[piece.row + 1][piece.column] = YipeeBlockEval.setIDFlag(YipeeBlock.Oy_BLOCK, YipeeBlockEval.getID(cells[piece.row + 1][piece.column]));
                cells[piece.row + 2][piece.column] = YipeeBlockEval.setIDFlag(YipeeBlock.Oy_BLOCK, YipeeBlockEval.getID(cells[piece.row + 2][piece.column]));
            }

            resetPiece();
        }
    }

    public int peekSpecialQueue() {
        return specialPieces.peek();
    }

    public void addSpecialPiece(int piece) {
        if (piece > 2 || piece < 1) {
            System.out.println("Assertion Error: invalid special block: " + piece);
            return;
        }
        specialPieces.offer(piece);
    }

    public void getNewNextPiece() {
        int isSpecial = 0;

        //Pop a special next piece if it exists
        if (!Util.isEmpty(specialPieces)) {
            isSpecial = specialPieces.poll();
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

    private YipeePiece newPieceBock(int isSpecial) {
        int block1;
        int block2;
        int block3;

        if (isSpecial > 0) {
            if (isSpecial % 2 == 1) {
                block1 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.MEDUSA, 3));
                block2 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.MEDUSA, 3));
                block3 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.MEDUSA, 3));
            } else {
                block1 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.BOT_MIDAS, 2));
                block2 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.MID_MIDAS, 2));
                block3 = YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(YipeeBlock.TOP_MIDAS, 2));
            }
        } else {
            block1 = powerUpBlock(getNextBlock());
            block2 = powerUpBlock(getNextBlock());
            block3 = powerUpBlock(getNextBlock());
        }

        YipeePiece piece = new YipeePiece(getIdIndex(), block1, block2, block3);
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

    public Queue<Integer> getPowers() {
        return powers;
    }

    private int getBlockPower(int block, int intensity) {
        if (intensity == 1) {
            intensity = 3;
        }
        if (intensity > 7) {
            intensity = 7;
        }
        return YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(block, intensity));
    }

    public void incrementBreakCount(int type) {
        if (type < 0 || type > MAX_COLS) return;
        ++countOfBreaks[type];
    }

    public void addPowerToQueue(int block) {
        //logger.debug("Enter addPowerToQueue(block=" + block + ")");
        //int b = block.getBlockType();
        if (YipeeBlockEval.hasPowerBlockFlag(block)) {
            int intensity = YipeeBlockEval.getPowerFlag(block);
            block = YipeeBlockEval.removeBrokenFlag(block);
            //logger.debug("intensity=" + intensity);
            powers.offer(YipeeBlockEval.addPowerBlockFlag(YipeeBlockEval.setPowerFlag(block, intensity)));
        }
        //logger.debug("current queue=" + powers);
        //logger.debug("Exit addPowerToQueue()");
    }


    public int popPowerFromQueue() {
        int powerBlock = -1;
        if (!Util.isEmpty(powers)) {
            powerBlock = powers.poll();
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

    public void testHorizontalYahoo() {
        setValueAt(YipeeBlock.Y_BLOCK, 0, 0);
        setValueAt(YipeeBlock.A_BLOCK, 1, 0);
        setValueAt(YipeeBlock.H_BLOCK, 2, 0);
        setValueAt(YipeeBlock.Op_BLOCK, 3, 0);
        setValueAt(YipeeBlock.Oy_BLOCK, 4, 0);
        setValueAt(YipeeBlock.EX_BLOCK, 5, 0);

        //setValueAt(YokelBlock.Y_BLOCK, 0, 1);
        //setValueAt(YokelBlock.A_BLOCK, 1, 1);
        //setValueAt(YokelBlock.H_BLOCK, 2, 1);
        //setValueAt(YokelBlock.Op_BLOCK, 3, 1);
        //setValueAt(YokelBlock.Oy_BLOCK, 4, 1);
        //setValueAt(YokelBlock.EX_BLOCK, 5, 1);
        //updateBoard();
    }

    public void testVerticalYahoo() {
        setValueAt(YipeeBlock.Y_BLOCK, 5, 6);
        setValueAt(YipeeBlock.A_BLOCK, 5, 5);
        setValueAt(YipeeBlock.H_BLOCK, 5, 4);
        setValueAt(YipeeBlock.Op_BLOCK, 5, 3);
        setValueAt(YipeeBlock.Oy_BLOCK, 5, 2);
        setValueAt(YipeeBlock.EX_BLOCK, 5, 1);
        //updateBoard();
    }
}