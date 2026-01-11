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

import asg.games.yipee.common.game.BrokenBlock;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents a block that has been broken (e.g., matched or destroyed) on a game board.
 *
 * <p>This object stores the block type along with its position on the board grid.
 * It is typically used during animations or cleanup phases to track visual effects
 * and scoring.
 *
 * <p>This class supports JSON serialization and extends {@link AbstractYipeeObject}.
 *
 * @author Blakbro2k
 */
@Getter
@Setter
public class YipeeBrokenBlock extends AbstractYipeeObject implements BrokenBlock {
    private static final Logger logger = LoggerFactory.getLogger(YipeeBrokenBlock.class);

    /**
     * The type identifier of the broken block (color, power, etc).
     */
    private int block;

    /**
     * The row where the block was located before breaking.
     */
    private int row;

    /**
     * The column where the block was located before breaking.
     */
    private int col;

    /**
     * Default constructor required for JSON serialization.
     * Initializes the broken block with default values.
     */
    public YipeeBrokenBlock() {
    }

    /**
     * Constructs a broken block with the given block type and location.
     *
     * @param block the block ID or type
     * @param row   the row index of the block
     * @param col   the column index of the block
     */
    public YipeeBrokenBlock(int block, int row, int col) {
        this.block = block;
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBrokenBlock that = (YipeeBrokenBlock) o;
        return block == that.block && row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), block, row, col);
    }

    @Override
    public String toString() {
        return super.toString() + "{Block: [" + block + "]" + YipeeBlock.printReaderFriendlyBlock(block) + "@, row: " + row + ", col: " + col + "}";
    }
}
