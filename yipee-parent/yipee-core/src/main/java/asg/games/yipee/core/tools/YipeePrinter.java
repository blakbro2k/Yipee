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
package asg.games.yipee.core.tools;

import asg.games.yipee.core.game.YipeeBlockEval;
import asg.games.yipee.core.game.YipeeGameBoard;
import asg.games.yipee.core.objects.YipeeBlock;
import asg.games.yipee.core.objects.YipeeGameBoardState;
import asg.games.yipee.core.objects.YipeePiece;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
@Setter
public class YipeePrinter {
    public static final YipeePrinter INSTANCE = new YipeePrinter();

    private int logLevel = 0;

    private YipeePrinter() {
    }

    public boolean isLogOff() {
        return logLevel < 1;
    }

    public boolean isLogWarn() {
        return logLevel < 2;
    }

    public boolean isLogInfo() {
        return logLevel < 3;
    }

    public boolean isLogDebug() {
        return logLevel >= 4;
    }


    public static YipeePrinter getInstance() {
        return INSTANCE;
    }

    public static void printYipeeBoard(YipeeGameBoard board) {
        if (board != null) {
            printYipeeBoardState(board.exportGameState());
        }
    }

    public static void printYipeeBoardState(YipeeGameBoardState gameState) {
        getInstance().internalPrintYipeeBoardState(gameState);
    }

    public void internalPrintYipeeBoardState(YipeeGameBoardState gameState) {
        System.out.println(getYipeeBoardStateOutput(gameState));
    }

    public String getYipeeBoardOutput(YipeeGameBoard board) {
        if (board != null) {
            return getYipeeBoardStateOutput(board.exportGameState());
        }
        return "";
    }

    public String getYipeeBoardStateOutput(YipeeGameBoardState gameState) {
        return stateToString(gameState);
    }

    // Print State
    public String stateToString(YipeeGameBoardState gameState) {
        return stateToString(gameState, 0);
    }

    public String stateToString(YipeeGameBoardState gameState, int depth) {
        if (gameState == null) throw new IllegalArgumentException("GameState cannot be null.");
        if (depth > 1) return "(partner omitted to prevent circular reference)";

        StringBuilder out = new StringBuilder();
        if (isLogInfo()) {
            out.append("#################").append("\n");
            out.append("Server Game Start Time: ").append(gameState.getServerGameStartTime()).append("\n");
            out.append("Current Game Time: ").append(gameState.getCurrentStateTimeStamp()).append("\n");
            out.append("Previous Game Time: ").append(gameState.getPreviousStateTimeStamp()).append("\n");
            out.append("Piece Fall Out Timer: ").append(gameState.getPieceFallTimer()).append("\n");
            out.append("#################").append("\n");
        }

        if (isLogWarn()) {
            out.append("#################").append("\n");
            out.append("Current 3Piece: ").append(gameState.getPiece()).append("\n");
            out.append("Next 3Piece: ").append(gameState.getNextPiece()).append("\n");
            out.append("#################").append("\n");
            out.append("Current Phase: ").append(gameState.getCurrentPhase()).append("\n");
            out.append("#################").append("\n");
        }

        if (isLogDebug()) {
            out.append("Debug Info: ").append("\n");
            out.append("#################").append("\n");
            out.append("Fall Animation Timer: ").append(gameState.getBlockAnimationTimer()).append("\n");
            out.append("LockOut Timer: ").append(gameState.getPieceLockTimer()).append("\n");
            out.append("Yahoo Drop Count: ").append(gameState.getYahooDuration()).append("\n");
            out.append("#################").append("\n");
            out.append("Vector of broken cells: ").append(gameState.getBrokenCells()).append("\n");
            out.append("Cells to drop: ").append(gameState.getCellsToDrop()).append("\n");
            out.append("#################").append("\n");
            out.append("Broken Block Count [Y,A,H,O,0,!]): ").append(Arrays.toString(gameState.getCountOfBreaks())).append("\n");
            out.append("Powers Break Count [Y,A,H,O,0,!]): ").append(Arrays.toString(gameState.getPowersKeep())).append("\n");
            out.append("Boolean Ids: ").append(Arrays.toString(gameState.getIds())).append("\n");
            out.append("idIndex: ").append(gameState.getIdIndex()).append("\n");
            out.append("#################").append("\n");
        }

        if (isLogWarn()) {
            out.append("#################").append("\n");
            if (gameState.getPiece() != null) {
                out.append("player piece pos(").append(gameState.getPiece().column).append(",").append(gameState.getPiece().row).append(")").append("\n");
            }
            out.append("#################").append("\n");
            addPrintLine(out);
            for (int r = YipeeGameBoard.MAX_ROWS - 1; r > -1; r--) {
                printRow(out, r, gameState, depth);
                printRowReturn(out);
            }
            addPrintLine(out);
            printRowReturn(out);
            out.append("#################").append("\n");
        }

        return out.toString();
    }

    private int[][] getPartnerCells(YipeeGameBoardState partnerBoardState) {
        int[][] cells = new int[YipeeGameBoard.MAX_ROWS][YipeeGameBoard.MAX_COLS];

        if (partnerBoardState != null) {
            cells = partnerBoardState.getPlayerCells();
        }
        return cells;
    }

    private int[][] getPartnerCells(YipeeGameBoardState partner, int depth) {
        if (partner != null && partner.getPlayerCells() != null) {
            return partner.getPlayerCells();
        }
        return new int[YipeeGameBoard.MAX_ROWS][YipeeGameBoard.MAX_COLS];
    }

    private void printRow(StringBuilder out, int r, @NotNull YipeeGameBoardState gameState, int depth) {
        boolean isPartnerRight = gameState.isPartnerRight();
        int[][] playerCells = gameState.getPlayerCells();
        int[][] partnerCells = gameState.getPartnerCells();

        if (isPartnerRight) {
            printPlayerRows(playerCells, partnerCells, r, out, gameState);
        } else {
            printPlayerRows(partnerCells, playerCells, r, out, gameState);
        }
    }

    private void printPlayerRows(int[][] cellsLeft, int[][] cellsRight, int r, StringBuilder out, @NotNull YipeeGameBoardState gameState) {
        boolean isPartnerRight = gameState.isPartnerRight();
        for (int c = 0; c < YipeeGameBoard.MAX_COLS * 2; c++) {
            int block;
            if (c == YipeeGameBoard.MAX_COLS) {
                out.append('|');
            }
            if (c < YipeeGameBoard.MAX_COLS) {
                block = isPieceBlock(r, c, gameState) && isPartnerRight ? getPieceBlock(r, gameState) : getPieceValue(cellsLeft, c, r);
                printGameLine(out, block);
            } else {
                block = isPieceBlock(r, c - YipeeGameBoard.MAX_COLS, gameState) && !isPartnerRight ? getPieceBlock(r, gameState) : getPieceValue(cellsRight, c - YipeeGameBoard.MAX_COLS, r);
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

    private void printRowReturn(@NotNull StringBuilder out) {
        out.append("\n");
    }

    private boolean isPieceBlock(int row, int col, @NotNull YipeeGameBoardState gameState) {
        YipeePiece piece = gameState.getPiece();
        return piece != null && piece.column == col && (piece.row == row || piece.row + 1 == row || piece.row + 2 == row);
    }

    private int getPieceBlock(int row, @NotNull YipeeGameBoardState gameState) {
        YipeePiece piece = gameState.getPiece();
        return piece.getValueAt(Math.abs(2 - (row - piece.row)));
    }

    @Contract(pure = true)
    private int getPieceValue(int[] @NotNull [] cells, int c, int r) {
        return YipeeBlockEval.getCellFlag(cells[r][c]);
    }
}
