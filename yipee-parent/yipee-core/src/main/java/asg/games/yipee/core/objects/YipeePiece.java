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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a game piece controlled by a {@link YipeePlayer} in the Yipee game.
 * A {@code YipeePiece} is composed of three individual {@link YipeeBlock}s,
 * which are stored in an internal array for management and manipulation.
 *
 * <p>The piece has a specific position on the game board, defined by its {@code row}
 * and {@code column}, and includes methods to cycle the blocks within the piece
 * either upwards or downwards, changing their order.</p>
 *
 * <p>This class provides methods for accessing and modifying the blocks,
 * as well as setting and validating the position of the piece on the game board.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Encapsulation of three {@link YipeeBlock}s, allowing for easy management of blocks within the piece.</li>
 *   <li>Ability to cycle blocks up or down to change their arrangement.</li>
 *   <li>Position tracking using {@code row} and {@code column} attributes.</li>
 *   <li>Validation of position to ensure values are within allowable ranges.</li>
 * </ul>
 *
 * <h2>Constants:</h2>
 * <ul>
 *   <li>{@link #MEDUSA_GAME_PIECE} - Identifier for a Medusa game piece type.</li>
 *   <li>{@link #MIDAS_GAME_PIECE} - Identifier for a Midas game piece type.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 *     // Create a new YipeePiece with three blocks
 *     YipeePiece piece = new YipeePiece(1, block1, block2, block3);
 *
 *     // Set its position on the game board
 *     piece.setPosition(2, 5);
 *
 *     // Cycle blocks within the piece
 *     piece.cycleDown();
 *
 *     // Access individual blocks
 *     int block1Value = piece.getTopBlock();
 * </pre>
 *
 * @author Blakbro2k
 * @version 1.0
 * @see YipeeBlock
 * @see AbstractYipeeObject
 */
@Getter
@Setter
public class YipeePiece extends AbstractYipeeObject implements Copyable<YipeePiece>{
    private static final Logger logger = LoggerFactory.getLogger(YipeePiece.class);

    /**
     * Identifier for Medusa game pieces.
     */
    @JsonIgnore
    public static final int MEDUSA_GAME_PIECE = 1;

    /**
     * Identifier for Midas game pieces.
     */
    @JsonIgnore
    public static final int MIDAS_GAME_PIECE = 2;

    /**
     * Internal array of 3 integers representing the block values of the piece.
     * Index 2 is the top block, index 1 is the middle block, and index 0 is the bottom block.
     */
    private final int[] cells = new int[3];

    /**
     * Optional index to identify or classify the piece (e.g. for debugging or piece type).
     */
    private int index;

    /**
     * The row position of the piece on the game board.
     */
    public int row;

    /**
     * The column position of the piece on the game board.
     */
    public int column;


    /**
     * Default constructor required for JSON serialization.
     */
    public YipeePiece() {
    }

    /**
     * Constructs a new YipeePiece with specified index and block values.
     *
     * @param index       the identifier for the piece
     * @param topBlock    the value of the top block
     * @param midBlock    the value of the middle block
     * @param bottomBlock the value of the bottom block
     */
    public YipeePiece(int index, int topBlock, int midBlock, int bottomBlock) {
        setIndex(index);
        setTopBlock(topBlock);
        setMidBlock(midBlock);
        setBottomBlock(bottomBlock);
    }

    /**
     * @return the value of the top block (index 2)
     */
    public int getTopBlock() {
        return cells[2];
    }

    /**
     * Sets the value of the top block.
     *
     * @param block the value to assign
     */
    public void setTopBlock(int block) {
        cells[2] = block;
    }

    /**
     * @return the value of the middle block (index 1)
     */
    public int getMidBlock() {
        return cells[1];
    }

    /**
     * Sets the value of the middle block.
     *
     * @param block the value to assign
     */
    public void setMidBlock(int block) {
        cells[1] = block;
    }

    /**
     * @return the value of the bottom block (index 0)
     */
    public int getBottomBlock() {
        return cells[0];
    }

    /**
     * Sets the value of the bottom block.
     *
     * @param block the value to assign
     */
    public void setBottomBlock(int block) {
        cells[0] = block;
    }

    /**
     * Sets the row and column position of the piece.
     *
     * @param r the row index (must be ≥ 0)
     * @param c the column index (must be ≥ 0)
     * @throws RuntimeException if either coordinate is negative
     */
    public void setPosition(int r, int c) {
        if (r < 0) {
            logger.error("Row value cannot be less than zero!");
            throw new RuntimeException("Row value cannot be less than zero!");
        }
        if (c < 0) {
            logger.error("Column value cannot be less than zero!");
            throw new RuntimeException("Column value cannot be less than zero!");
        }
        this.row = r;
        this.column = c;
    }

    /**
     * Rotates the piece's blocks downward:
     * top → middle, middle → bottom, bottom → top.
     */
    public void cycleDown() {
        int tempBlock1 = getTopBlock();
        int tempBlock2 = getMidBlock();
        int tempBlock3 = getBottomBlock();
        setTopBlock(tempBlock3);
        setMidBlock(tempBlock1);
        setBottomBlock(tempBlock2);
    }

    /**
     * Rotates the piece's blocks upward:
     * top → bottom, middle → top, bottom → middle.
     */
    public void cycleUp() {
        int tempBlock1 = getTopBlock();
        int tempBlock2 = getMidBlock();
        int tempBlock3 = getBottomBlock();
        setTopBlock(tempBlock2);
        setMidBlock(tempBlock3);
        setBottomBlock(tempBlock1);
    }

    /**
     * Returns the value of the block at the specified internal index.
     *
     * @param i the index in the cells array (0 to 2)
     * @return the block value
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public int getValueAt(int i) {
        if (i < 0 || i >= cells.length) {
            logger.error("Invalid index for getValueAt: {}", i);
            throw new IllegalArgumentException("Index out of bounds for getValueAt.");
        }
        return cells[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeePiece that = (YipeePiece) o;
        return getIndex() == that.getIndex() && row == that.row && column == that.column && Arrays.equals(getCells(), that.getCells());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), getIndex(), row, column);
        result = 31 * result + Arrays.hashCode(getCells());
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "[row=" + row + ", column=" + column + ", cells=" + Arrays.toString(cells) + "]";
    }

    /**
     * Creates a shallow copy of this piece, duplicating its state and block values.
     *
     * @return a new {@code YipeePiece} with identical values
     */
    @Override
    public YipeePiece copy() {
        YipeePiece copy = new YipeePiece();
        copy.index = this.index;
        copy.row = this.row;
        copy.column = this.column;
        System.arraycopy(this.cells, 0, copy.cells, 0, this.cells.length);
        return copy;
    }

    /**
     * Creates a deep copy of this piece.
     * Since only primitive data is used, this is equivalent to {@link #copy()}.
     *
     * @return a fully independent copy of this piece
     */
    @Override
    public YipeePiece deepCopy() {
        // identical here, since cells is the only mutable content needing true duplication
        return this.copy();
    }
}
