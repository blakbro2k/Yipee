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

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeBrokenBlockGDX extends AbstractYipeeObjectGDX {
    private int block, row, col;

    /**
     * Default constructor required for JSON serialization.
     * Initializes the block in its default state.
     */
    public YipeeBrokenBlockGDX() {
    }

    public YipeeBrokenBlockGDX(int block, int row, int col) {
        setBlock(block);
        setRow(row);
        setCol(col);
    }

    @Override
    public String toString() {
        return super.toString() + "{Block: [" + block + "]" + YipeeBlockGDX.printReaderFriendlyBlock(block) + "@, row: " + row + ", col: " + col + "}";
    }
}
