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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a visualized broken block on the game board for client-side animation and effects.
 * <p>
 * Used in animations where a block explodes, shatters, or fades after a match or combo.
 * Stores the block's type, and its original position on the board grid.
 * </p>
 *
 * <p>This class is GWT- and JSON-serialization friendly and extends {@link AbstractYipeeObjectGDX}
 * to provide a common base for LibGDX renderable game data structures.</p>
 *
 * @author See AUTHORS
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeBrokenBlockGDX extends AbstractYipeeObjectGDX {

    /**
     * The integer code representing the block type.
     */
    private int block;

    /**
     * The row position of the broken block on the board.
     */
    private int row;

    /**
     * The column position of the broken block on the board.
     */
    private int col;

    /**
     * Default constructor required for JSON serialization and framework instantiation.
     */
    public YipeeBrokenBlockGDX() {
    }

    /**
     * Constructs a new {@code YipeeBrokenBlockGDX} instance with the specified
     * block type and board coordinates.
     *
     * @param block the block value/type identifier
     * @param row   the row where the block was located
     * @param col   the column where the block was located
     */
    public YipeeBrokenBlockGDX(int block, int row, int col) {
        setBlock(block);
        setRow(row);
        setCol(col);
    }

    /**
     * Returns a human-readable representation of the broken block,
     * including its type and position.
     *
     * @return a formatted string describing the block
     */
    @Override
    public String toString() {
        return super.toString() +
            "{Block: [" + block + "]" +
            YipeeBlockGDX.printReaderFriendlyBlock(block) +
            "@, row: " + row + ", col: " + col + "}";
    }
}
