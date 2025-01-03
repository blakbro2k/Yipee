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
package asg.games.yipee.objects;

import java.util.Arrays;
import java.util.Queue;

public class YipeeGameBoardState extends AbstractYipeeObject {
    private long serverGameStartTime = 0;
    private long currentStateTimeStamp = 0;
    private long previousStateTimeStamp = 0;
    private YipeePiece piece = null;
    private YipeePiece nextPiecePreview = null;
    private int[][] playerCells = new int[YipeeGameBoard.MAX_ROWS][YipeeGameBoard.MAX_COLS];
    private int[][] partnerCells = new int[YipeeGameBoard.MAX_ROWS][YipeeGameBoard.MAX_COLS];
    private Iterable<YipeeBrokenBlock> brokenCells = null;
    private Iterable<YipeeBlockMove> cellsToDrop = null;
    private Iterable<Integer> powersQueue = null;
    private int yahooDuration;
    private float pieceFallTimer;
    private float pieceLockTimer;
    private boolean isPartnerRight;
    private float blockAnimationTimer;
    private boolean isPieceSet;
    private Queue<Integer> specialPieces;
    private int[] countOfBreaks;
    private boolean isDebug;
    private int[] powersKeep;
    private boolean[] ids;
    private int idIndex;

    public YipeeGameBoardState() {}

    public long getServerGameStartTime() {
        return serverGameStartTime;
    }

    public void setServerGameStartTime(long serverTime) {
        this.serverGameStartTime = serverTime;
    }

    public long getCurrentStateTimeStamp() {
        return currentStateTimeStamp;
    }

    public void setCurrentStateTimeStamp(long currentStateTimeStamp) {
        setPreviousStateTimeStamp(currentStateTimeStamp);
        this.currentStateTimeStamp = currentStateTimeStamp;
    }

    public long getPreviousStateTimeStamp() {
        return previousStateTimeStamp;
    }

    public void setPreviousStateTimeStamp(long previousStateTimeStamp) {
        this.previousStateTimeStamp = previousStateTimeStamp;
    }

    public YipeePiece getNextPiecePreview() {
        return nextPiecePreview;
    }

    public void setNextPiecePreview(YipeePiece nextPiecePreview) {
        this.nextPiecePreview = nextPiecePreview;
    }


    public YipeePiece getPiece() {
        return piece;
    }

    public void setPiece(YipeePiece piece) {
        this.piece = piece;
    }

    public Iterable<YipeeBrokenBlock> getBrokenCells() {
        return brokenCells;
    }

    public void setBrokenCells(Iterable<YipeeBrokenBlock> brokenCells) {
        this.brokenCells = brokenCells;
    }

    public void setCellsToDrop(Iterable<YipeeBlockMove> cellsToDrop) {
        this.cellsToDrop = cellsToDrop;
    }

    public void setPowersQueue(Iterable<Integer> powersQueue) {
        this.powersQueue = powersQueue;
    }

    public Iterable<Integer> getPowersQueue() {
        return powersQueue;
    }

    public Iterable<YipeeBlockMove> getCellsToDrop() {
        return this.cellsToDrop;
    }

    public void setYahooDuration(int yahooDuration) {
        this.yahooDuration = yahooDuration;
    }

    public int getYahooDuration() {
        return this.yahooDuration;
    }

    public void setPieceFallTimer(float pieceFallTimer) {
        this.pieceFallTimer = pieceFallTimer;
    }

    public float getPieceFallTimer() {
        return this.pieceFallTimer;
    }

    public void setPieceLockTimer(float pieceLockTimer) {
        this.pieceLockTimer = pieceLockTimer;
    }

    public float getPieceLockTimer() {
        return this.pieceLockTimer;
    }

    public void setPartnerCells(int[][] partnerBoard) {
        this.partnerCells = partnerBoard;
    }

    public int[][] getPartnerCells() {
        return partnerCells;
    }

    public void setPlayerCells(int[][] playerCells) {
        this.playerCells = playerCells;
    }

    public int[][] getPlayerCells() {
        return playerCells;
    }

    public void setPartnerIsRight(boolean isPartnerRight) {
        this.isPartnerRight = isPartnerRight;
    }

    public boolean getPartnerIsRight() {
        return isPartnerRight;
    }

    public void setPlayerPiece(YipeePiece piece) {
        this.piece = piece;
    }

    public YipeePiece getPlayerPiece() {
        return piece;
    }

    // Print State
    public String toString() {
        StringBuilder out = new StringBuilder();
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
        out.append("Next 3Piece: ").append(getNextPiecePreview()).append("\n");
        out.append("#################").append("\n");

        if(isDebug()) {
            out.append("Debug Info: ").append("\n");
            out.append("#################").append("\n");
            out.append("Broken Block Count [Y,A,H,O,0,!]): ").append(Arrays.toString(getBrokenBlockCount())).append("\n");
            out.append("Powers Break Count [Y,A,H,O,0,!]): ").append(Arrays.toString(getPowersCount())).append("\n");
            out.append("Boolean Ids: ").append(Arrays.toString(getBlockIds())).append("\n");
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

    private boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    private void printRow(StringBuilder out, int r) {
        if (isPartnerRight) {
            printPlayerRows(playerCells, partnerCells, r, out);
        } else {
            printPlayerRows(partnerCells, playerCells, r, out);
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

    public void setBlockAnimationTimer(float blockAnimationTimer) {
        this.blockAnimationTimer = blockAnimationTimer;
    }

    public float getBlockAnimationTimer() {
        return blockAnimationTimer;
    }

    public void setIsPieceSet(boolean isPieceSet) {
        this.isPieceSet = isPieceSet;
    }

    public boolean getIsPieceSet() {
        return isPieceSet;
    }

    public void setSpecialPieces(Queue<Integer> specialPieces) {
        this.specialPieces = specialPieces;
    }

    public Queue<Integer> getSpecialPieces() {
        return specialPieces;
    }

    public void setBrokenBlockCount(int[] countOfBreaks) {
        this.countOfBreaks = countOfBreaks;
    }

    public int[] getBrokenBlockCount() {
        return countOfBreaks;
    }

    public void setPowersCount(int[] powersKeep) {
        this.powersKeep = powersKeep;
    }

    public int[] getPowersCount() {
        return powersKeep;
    }

    public void setBlockIds(boolean[] ids) {
        this.ids = ids;
    }

    public boolean[] getBlockIds() {
        return ids;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdIndex() {
        return idIndex;
    }
}
