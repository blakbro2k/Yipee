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

import asg.games.yipee.common.game.BlockMove;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Represents a single block movement action within a Yipee game board.
 *
 * <p>This object is used to track and animate the movement of a block from its current
 * position to a target row. It encapsulates positional data such as column, current row,
 * and destination row, as well as a unique cell ID and the block type.
 *
 * <p>Instances of this class are typically created during collapse or gravity phases
 * where blocks fall down due to matches or row clears.
 *
 * <p>Implements equals/hashCode for proper comparison and supports JSON serialization.
 */
@Getter
@Setter
@Slf4j
public class YipeeBlockMove extends AbstractYipeeObject implements BlockMove {
    /**
     * The type or value of the block being moved (e.g., color index or power ID).
     */
    private int block;

    /** The internal identifier of the block’s cell on the board. */
    private int cellId;

    /** The column where the block is currently located. */
    private int col;

    /** The row where the block is currently located. */
    private int row;

    /** The row the block is moving toward. */
    private int targetRow;

    /**
     * Default constructor required for JSON or Kryo serialization.
     * Initializes the object with default values.
     */
    public YipeeBlockMove() {
    }

    /**
     * Constructs a fully-initialized YipeeBlockMove with specific location data.
     *
     * @param cellID    the unique cell ID of the block
     * @param block     the block value (e.g., color or power type)
     * @param col       the column position
     * @param row       the current row position
     * @param targetRow the row the block is falling or animating toward
     */
    public YipeeBlockMove(int cellID, int block, int col, int row, int targetRow) {
        setBlock(block);
        setCellId(cellID);
        setCol(col);
        setRow(row);
        setTargetRow(targetRow);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBlockMove blockMove = (YipeeBlockMove) o;
        return col == blockMove.getCol() && row == blockMove.getRow() && targetRow == blockMove.getTargetRow() && cellId == blockMove.getCellId() && block == blockMove.getBlock();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), col, row, targetRow, cellId, block);
    }

    @Override
    public String toString() {
        return super.toString() + "{[cellId: " + cellId + "]" + block + "@(col: " + col + ", row: " + row + ") to targetRow: " + targetRow + "}";
    }
}
