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

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Getter
@Setter
public class YipeeBrokenBlock extends AbstractYipeeObject {
    private static final Logger logger = LoggerFactory.getLogger(YipeeBrokenBlock.class);

    private int block, row, col;

    /**
     * Default constructor required for JSON serialization.
     * Initializes the block in its default state.
     */
    public YipeeBrokenBlock() {
    }

    public YipeeBrokenBlock(int block, int row, int col) {
        setBlock(block);
        setRow(row);
        setCol(col);
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
