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

import asg.games.yipee.common.enums.YipeeObject;

/**
 * Represents the full state of a Yipee game board at a specific point in time.
 *
 * <p>This interface is used for both authoritative server-side state and
 * predicted client-side state during rollback/prediction cycles. Implementations
 * should encapsulate all information needed to render the board, simulate gameplay,
 * synchronize with the server, and animate transitions.</p>
 *
 * <p>Because this interface is shared across all platforms (server, desktop, mobile,
 * and GWT clients), it contains only primitive types, arrays, and lightweight DTOs
 * to ensure full serialization compatibility.</p>
 */
public interface GameBoardState {

    /**
     * Sets the timestamp (in milliseconds) for when this state snapshot was generated.
     *
     * @param l epoch millisecond timestamp
     */
    void setCurrentStateTimeStamp(long l);

    /**
     * Returns the current game phase associated with this board state.
     *
     * @return the current {@link GamePhase}
     */
    GamePhase getCurrentPhase();

    /**
     * Sets the current phase of the game.
     *
     * @param currentPhase the new phase to assign
     */
    void setCurrentPhase(GamePhase currentPhase);

    /**
     * Returns the number of broken blocks currently awaiting cleanup or animation.
     *
     * @return count of broken blocks
     */
    int getBrokenBlockCount();

    /**
     * Sets the number of broken blocks currently tracked in this board state.
     *
     * @param brokenBlockCount number of broken blocks
     */
    void setBrokenBlockCount(int brokenBlockCount);

    /**
     * Indicates whether the player is actively fast-dropping the active piece.
     *
     * @return {@code true} if fast-drop is engaged
     */
    boolean isFastDown();

    /**
     * Enables or disables fast-drop for the active piece.
     *
     * @param fastDown {@code true} if fast-drop is active
     */
    void setFastDown(boolean fastDown);

    /**
     * Returns the pointer/index to the currently falling block in the piece sequence.
     *
     * @return index of the current block
     */
    int getCurrentBlockPointer();

    /**
     * Sets the index of the current block in the piece sequence.
     *
     * @param currentBlockPointer pointer index
     */
    void setCurrentBlockPointer(int currentBlockPointer);

    /**
     * Returns the deterministic random-number array for upcoming block pieces.
     *
     * <p>Used for synchronized gameplay and client prediction.</p>
     *
     * @return a common random-number array
     */
    CommonRandomNumberArray getNextBlocks();

    /**
     * Sets the random-number provider for block generation.
     *
     * @param nextBlocks deterministic random block sequence
     */
    void setNextBlocks(CommonRandomNumberArray nextBlocks);

    /**
     * Returns an array representing how many blocks were broken per category or type.
     *
     * @return array of break counts
     */
    int[] getCountOfBreaks();

    /**
     * Sets the break-count array.
     *
     * @param countOfBreaks array of break counts
     */
    void setCountOfBreaks(int[] countOfBreaks);

    /**
     * Returns the powers the player has retained for later use.
     *
     * @return array of power identifiers
     */
    int[] getPowersKeep();

    /**
     * Sets the retained powers for the player.
     *
     * @param powersKeep array of powers
     */
    void setPowersKeep(int[] powersKeep);

    /**
     * Returns the formatted game clock value (e.g., "02:31").
     *
     * @return game clock string
     */
    String getGameClock();

    /**
     * Sets the formatted game clock value.
     *
     * @param gameClock time string
     */
    void setGameClock(String gameClock);

    /**
     * Returns a boolean ID array typically used for sequence tracking, availability,
     * or internal flags.
     *
     * @return an array of boolean flags
     */
    boolean[] getIds();

    /**
     * Sets the boolean ID array.
     *
     * @param ids the boolean array of flags
     */
    void setIds(boolean[] ids);

    /**
     * Returns the current index into the ID array.
     *
     * @return ID index value
     */
    int getIdIndex();

    /**
     * Sets the index into the ID array.
     *
     * @param idIndex index value
     */
    void setIdIndex(int idIndex);

    /**
     * Indicates whether debugging or diagnostic mode is enabled for this board state.
     *
     * @return {@code true} if debug mode is active
     */
    boolean isDebug();

    /**
     * Enables or disables debug mode.
     *
     * @param debug true to enable debug mode
     */
    void setDebug(boolean debug);

    /**
     * Returns the name associated with this board or board owner.
     *
     * @return board/player name
     */
    String getName();

    /**
     * Sets the name associated with this board.
     *
     * @param name board/player name
     */
    void setName(String name);

    /**
     * Returns the currently active piece identifier.
     *
     * @return current piece string
     */
    String getPiece();

    /**
     * Sets the currently active piece identifier.
     *
     * @param piece piece string
     */
    void setPiece(String piece);

    /**
     * Returns the next upcoming piece identifier.
     *
     * @return next piece string
     */
    String getNextPiece();

    /**
     * Sets the next piece identifier.
     *
     * @param yipeePiece piece string
     */
    void setNextPiece(String yipeePiece);

    /**
     * Returns the 2D grid of player cells representing the board state.
     *
     * @return 2D cell matrix
     */
    int[][] getPlayerCells();

    /**
     * Sets the player's cell grid.
     *
     * @param cells 2D matrix of board cells
     */
    void setPlayerCells(int[][] cells);

    /**
     * Returns the timer controlling piece descent speed.
     *
     * @return fall timer value
     */
    float getPieceFallTimer();

    /**
     * Sets the piece fall timer.
     *
     * @param pieceFallTimer timer value
     */
    void setPieceFallTimer(float pieceFallTimer);

    /**
     * Returns the timer tracking when a piece locks into place.
     *
     * @return lock timer
     */
    float getPieceLockTimer();

    /**
     * Sets the piece lock timer.
     *
     * @param pieceLockTimer timer value
     */
    void setPieceLockTimer(float pieceLockTimer);

    /**
     * Returns the animation timer for block effects (breaks, collapses, etc.).
     *
     * @return animation timer
     */
    float getBlockAnimationTimer();

    /**
     * Sets the block animation timer.
     *
     * @param blockAnimationTimer timer value
     */
    void setBlockAnimationTimer(float blockAnimationTimer);

    /**
     * Returns the duration of Yahoo-mode effects.
     *
     * @return duration in ticks or milliseconds
     */
    int getYahooDuration();

    /**
     * Sets the duration of Yahoo-mode effects.
     *
     * @param yahooDuration duration value
     */
    void setYahooDuration(int yahooDuration);

    /**
     * Indicates whether this board's partner is located on the right side.
     *
     * @return true if partner is on the right
     */
    boolean isPartnerRight();

    /**
     * Sets whether the partner is positioned to the right.
     *
     * @param isPartnerRight the partner orientation flag
     */
    void setPartnerRight(boolean isPartnerRight);

    /**
     * Returns the list of active powers currently applied to this board.
     *
     * @return iterable of power identifiers
     */
    Iterable<Integer> getPowers();

    /**
     * Sets the list of active powers.
     *
     * @param powers iterable of power IDs
     */
    void setPowers(Iterable<Integer> powers);

    /**
     * Returns the structure describing broken cells for animation or cleanup.
     *
     * @return implementation-defined broken cell data
     */
    Iterable<? extends YipeeObject> getBrokenCells();

    /**
     * Sets the broken cell data.
     *
     * @param brokenCells implementation-defined representation of broken cells
     */
    void setBrokenCells(Iterable<? extends YipeeObject> brokenCells);

    /**
     * Returns the current list of special pieces affecting gameplay.
     *
     * @return iterable of special piece IDs
     */
    Iterable<Integer> getSpecialPieces();

    /**
     * Sets the list of special pieces for this state.
     *
     * @param specialPieces iterable of special piece IDs
     */
    void setSpecialPieces(Iterable<Integer> specialPieces);

    /**
     * Indicates whether the game has officially started.
     *
     * @return true if the game has begun
     */
    boolean isHasGameStarted();

    /**
     * Marks whether the game has officially started.
     *
     * @param hasGameStarted flag value
     */
    void setHasGameStarted(boolean hasGameStarted);

    /**
     * Returns the board index associated with this state (0–7).
     *
     * @return board number
     */
    int getBoardNumber();

    /**
     * Sets the board index for this state.
     *
     * @param boardNumber board number
     */
    void setBoardNumber(int boardNumber);

    /**
     * Sets the partner board’s cell matrix.
     *
     * @param partnerCells 2D matrix of partner cells
     */
    void setPartnerCells(int[][] partnerCells);

    /**
     * Returns the timestamp indicating when this state was produced.
     *
     * @return timestamp in milliseconds
     */
    long getCurrentStateTimeStamp();

    /**
     * Returns the timestamp when the server’s game session started.
     *
     * @return start timestamp in milliseconds
     */
    long getServerGameStartTime();

    /**
     * Returns the timestamp of the previous state snapshot.
     *
     * @return previous state timestamp
     */
    long getPreviousStateTimeStamp();

    /**
     * Returns the list of cells that are currently in a dropping animation or transition.
     *
     * @return iterable of cell descriptors
     */
    Iterable<Object> getCellsToDrop();

    /**
     * Returns the partner board’s cell matrix.
     *
     * @return 2D matrix of partner cells
     */
    int[][] getPartnerCells();
}
