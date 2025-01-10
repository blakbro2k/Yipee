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

import asg.games.yipee.game.YipeeBlockEval;
import asg.games.yipee.game.YipeeGameBoard;
import asg.games.yipee.tools.RandomUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Queue;

@Getter
@Setter
public class YipeeGameBoardState extends AbstractYipeeObject {

    private long serverGameStartTime = 0;
    private long currentStateTimeStamp = 0;
    private long previousStateTimeStamp = 0;
    private YipeePiece piece = null;
    private YipeePiece nextPiece = null;
    private YipeeClock gameClock = null;
    private int[][] playerCells = new int[YipeeGameBoard.MAX_ROWS][YipeeGameBoard.MAX_COLS];
    private YipeeGameBoardState partnerBoard;
    private Iterable<YipeeBrokenBlock> brokenCells = null;
    private Iterable<YipeeBlockMove> cellsToDrop = null;
    private Iterable<Integer> powers = null;
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
    private int[] randomColumnIndices = new int[YipeeGameBoard.MAX_COLS];
    private RandomUtil.RandomNumberArray nextBlocks = null;
    private int currentBlockPointer = -1;
    private boolean fastDown;
    private int brokenBlockCount = 0;
    private boolean hasGameStarted = false;
    private String name;

    public YipeeGameBoardState() {
    }

    public void setCurrentStateTimeStamp(long currentStateTimeStamp) {
        setPreviousStateTimeStamp(currentStateTimeStamp);
        this.currentStateTimeStamp = currentStateTimeStamp;
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
        out.append("Next 3Piece: ").append(getNextPiece()).append("\n");
        out.append("#################").append("\n");

        if(isDebug()) {
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
        int[][] cells = new int[YipeeGameBoard.MAX_ROWS][YipeeGameBoard.MAX_COLS];
        ;
        if (partnerBoardState != null) {
            cells = partnerBoardState.getPlayerCells();
        }
        return cells;
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
