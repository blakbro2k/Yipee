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
package asg.games.yipee.libgdx.objects;

import asg.games.yipee.common.game.BlockMove;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a single block move within the Yipee game for LibGDX rendering.
 * <p>
 * This class stores the block type, its current cell ID, position (column and row),
 * and its target destination row for animated movement on the client side.
 * <p>
 * Typical use: describing a block falling from one row to another,
 * supporting animation interpolation in LibGDX UI rendering.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeBlockMoveGDX extends AbstractYipeeObjectGDX implements BlockMove {

    /**
     * The type or ID of the block being moved.
     */
    private int block;

    /**
     * The unique cell identifier for the block in the grid.
     */
    private int cellId;

    /**
     * The column position of the block in the grid.
     */
    private int col;

    /**
     * The current row position of the block in the grid.
     */
    private int row;

    /**
     * The target row position the block is moving to (e.g. for animation).
     */
    private int targetRow;

    /**
     * Default no-argument constructor required for JSON serialization.
     * Initializes the block in its default state with unassigned values.
     */
    public YipeeBlockMoveGDX() {
    }

    /**
     * Constructs a new YipeeBlockMoveGDX with the given parameters.
     *
     * @param cellID    the unique identifier for the cell containing the block
     * @param block     the type or ID of the block
     * @param col       the column position in the grid
     * @param row       the current row position in the grid
     * @param targetRow the target row position to move to
     */
    public YipeeBlockMoveGDX(int cellID, int block, int col, int row, int targetRow) {
        setBlock(block);
        setCellId(cellID);
        setCol(col);
        setRow(row);
        this.targetRow = targetRow;
    }

    /**
     * Returns a human-readable string representation of this block move,
     * useful for debugging and logging.
     *
     * @return a string describing the block move details
     */
    @Override
    public String toString() {
        return super.toString() + "{[cellId: " + cellId + "]" +
            block + "@(col: " + col + ", row: " + row + ") to targetRow: " + targetRow + "}";
    }
}
