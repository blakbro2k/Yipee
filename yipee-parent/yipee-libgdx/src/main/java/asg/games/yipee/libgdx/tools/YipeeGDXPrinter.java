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
package asg.games.yipee.libgdx.tools;

import asg.games.yipee.common.game.GameBoardState;
import asg.games.yipee.libgdx.game.YipeeBlockEvalGDX;
import asg.games.yipee.libgdx.game.YipeeGameBoardGDX;
import asg.games.yipee.libgdx.objects.YipeeBlockGDX;
import asg.games.yipee.libgdx.objects.YipeeGameBoardStateGDX;
import asg.games.yipee.libgdx.objects.YipeePieceGDX;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class YipeeGDXPrinter {
    public static final YipeeGDXPrinter INSTANCE = new YipeeGDXPrinter();

    private int logLevel = 0;

    private YipeeGDXPrinter() {
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

    public static YipeeGDXPrinter getInstance() {
        return INSTANCE;
    }

    public static void printYipeeBoard(YipeeGameBoardGDX board) {
        if (board != null) {
            printYipeeBoardState(board.exportGameState());
        }
    }

    public static String toStringYipeeBoard(YipeeGameBoardGDX board) {
        String output = "";
        if (board != null) {
            printYipeeBoardState(board.exportGameState());
        }
        return output;
    }

    public static void printYipeeBoardState(GameBoardState gameState) {
        getInstance().internalPrintYipeeBoardState(gameState);
    }

    public static String getYipeeBoardStateString(GameBoardState gameState) {
        return getInstance().getYipeeBoardStateOutput(gameState);
    }

    public void internalPrintYipeeBoardState(GameBoardState gameState) {
        System.out.println(getYipeeBoardStateOutput(gameState));
    }

    public String getYipeeBoardOutput(YipeeGameBoardGDX board) {
        if (board != null) {
            return getYipeeBoardStateOutput(board.exportGameState());
        }
        return "";
    }

    public String getYipeeBoardStateOutput(GameBoardState gameState) {
        return stateToString(gameState);
    }

    // Print State
    public String stateToString(GameBoardState gameState) {
        return stateToString(gameState, 0);
    }

    public String stateToString(GameBoardState inState, int depth) {
        if (inState == null) throw new IllegalArgumentException("GameState cannot be null.");
        if (depth > 1) return "(partner omitted to prevent circular reference)";

        YipeeGameBoardStateGDX gameState = (YipeeGameBoardStateGDX) inState;

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
            YipeePieceGDX piece = NetUtil.fromJsonClient(gameState.getPiece(), YipeePieceGDX.class);
            if (piece != null) {
                out.append("player piece pos(").append(piece.column).append(",").append(piece.row).append(")").append("\n");
            }
            out.append("#################").append("\n");
            addPrintLine(out);
            for (int r = YipeeGameBoardGDX.MAX_ROWS - 1; r > -1; r--) {
                printRow(out, r, gameState, depth);
                printRowReturn(out);
            }
            addPrintLine(out);
            printRowReturn(out);
            out.append("#################").append("\n");
        }

        return out.toString();
    }

    private int[][] getPartnerCells(YipeeGameBoardStateGDX partnerBoardState) {
        int[][] cells = new int[YipeeGameBoardGDX.MAX_ROWS][YipeeGameBoardGDX.MAX_COLS];

        if (partnerBoardState != null) {
            cells = partnerBoardState.getPlayerCells();
        }
        return cells;
    }

    private int[][] getPartnerCells(YipeeGameBoardStateGDX partner, int depth) {
        if (partner != null && partner.getPlayerCells() != null) {
            return partner.getPlayerCells();
        }
        return new int[YipeeGameBoardGDX.MAX_ROWS][YipeeGameBoardGDX.MAX_COLS];
    }

    private void printRow(StringBuilder out, int r, YipeeGameBoardStateGDX gameState, int depth) {
        boolean isPartnerRight = gameState.isPartnerRight();
        int[][] playerCells = gameState.getPlayerCells();
        int[][] partnerCells = gameState.getPartnerCells();

        if (isPartnerRight) {
            printPlayerRows(playerCells, partnerCells, r, out, gameState);
        } else {
            printPlayerRows(partnerCells, playerCells, r, out, gameState);
        }
    }

    private void printPlayerRows(int[][] cellsLeft, int[][] cellsRight, int r, StringBuilder out, YipeeGameBoardStateGDX gameState) {
        boolean isPartnerRight = gameState.isPartnerRight();
        for (int c = 0; c < YipeeGameBoardGDX.MAX_COLS * 2; c++) {
            int block;
            if (c == YipeeGameBoardGDX.MAX_COLS) {
                out.append('|');
            }
            if (c < YipeeGameBoardGDX.MAX_COLS) {
                block = isPieceBlock(r, c, gameState) && isPartnerRight ? getPieceBlock(r, gameState) : getPieceValue(cellsLeft, c, r);
                printGameLine(out, block);
            } else {
                block = isPieceBlock(r, c - YipeeGameBoardGDX.MAX_COLS, gameState) && !isPartnerRight ? getPieceBlock(r, gameState) : getPieceValue(cellsRight, c - YipeeGameBoardGDX.MAX_COLS, r);
                printGameLine(out, block);
            }
        }
        out.append('|');
    }

    private void printGameLine(StringBuilder out, int block) {
        if (block == YipeeBlockGDX.CLEAR_BLOCK) {
            out.append('|').append(' ');
        } else {
            if (YipeeBlockEvalGDX.hasPowerBlockFlag(block)) {
                out.append('|').append(YipeeBlockEvalGDX.getPowerLabel(block));
            } else {
                out.append('|').append(YipeeBlockEvalGDX.getNormalLabel(block));
            }
        }
    }

    private void addPrintLine(StringBuilder sb) {
        for (int a = 0; a < YipeeGameBoardGDX.MAX_COLS * 2; a++) {
            sb.append("+");
            if (a == YipeeGameBoardGDX.MAX_COLS) {
                sb.append("+");
            }
            sb.append("-");
        }
        sb.append('+').append('\n');
    }

    private void printRowReturn(StringBuilder out) {
        out.append("\n");
    }

    private boolean isPieceBlock(int row, int col, YipeeGameBoardStateGDX gameState) {
        YipeePieceGDX piece = NetUtil.fromJsonClient(gameState.getPiece(), YipeePieceGDX.class);
        return piece != null && piece.column == col && (piece.row == row || piece.row + 1 == row || piece.row + 2 == row);
    }

    private int getPieceBlock(int row, YipeeGameBoardStateGDX gameState) {
        YipeePieceGDX piece = NetUtil.fromJsonClient(gameState.getPiece(), YipeePieceGDX.class);
        return piece.getValueAt(Math.abs(2 - (row - piece.row)));
    }

    private int getPieceValue(int[][] cells, int c, int r) {
        return YipeeBlockEvalGDX.getCellFlag(cells[r][c]);
    }
}
