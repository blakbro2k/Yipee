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

import java.util.Arrays;

/**
 * Represents a game piece controlled by a {@link YipeePlayerGDX} in the Yipee game.
 * A {@code YipeePieceGDX} is composed of three individual {@link YipeeBlockGDX}s,
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
 *   <li>Encapsulation of three {@link YipeeBlockGDX}s, allowing for easy management of blocks within the piece.</li>
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
 *     // Create a new YipeePieceGDX with three blocks
 *     YipeePieceGDX piece = new YipeePieceGDX(1, block1, block2, block3);
 *
 *     // Set its position on the game board
 *     piece.setPosition(2, 5);
 *
 *     // Cycle blocks within the piece
 *     piece.cycleDown();
 *
 *     // Access individual blocks
 *     int block1Value = piece.getBlock1();
 * </pre>
 *
 * @author Blakbro2k
 * @version 1.0
 * @see YipeeBlockGDX
 * @see AbstractYipeeObjectGDX
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeePieceGDX extends AbstractYipeeObjectGDX {
    /**
     * Identifier for Medusa game pieces.
     */
    public static final int MEDUSA_GAME_PIECE = 1;

    /**
     * Identifier for Midas game pieces.
     */
    public static final int MIDAS_GAME_PIECE = 2;

    private final int[] cells = new int[3];
    private int index;
    public int row;
    public int column;

    //Empty Constructor required for Json.Serializable
    public YipeePieceGDX() {
    }

    public YipeePieceGDX(int index, int block1, int block2, int block3) {
        setIndex(index);
        setBlock1(block1);
        setBlock2(block2);
        setBlock3(block3);
    }

    public int getBlock1() {
        return cells[2];
    }

    public void setBlock1(int block) {
        cells[2] = block;
    }

    public int getBlock2() {
        return cells[1];
    }

    public void setBlock2(int block) {
        cells[1] = block;
    }

    public int getBlock3() {
        return cells[0];
    }

    public void setBlock3(int block) {
        cells[0] = block;
    }

    public void setPosition(int r, int c) {
        if (r < 0) {
            throw new RuntimeException("Row value cannot be less than zero!");
        }
        if (c < 0) {
            throw new RuntimeException("Column value cannot be less than zero!");
        }
        this.row = r;
        this.column = c;
    }

    public void cycleDown() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock3);
        setBlock2(tempBlock1);
        setBlock3(tempBlock2);
    }

    public void cycleUp() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock2);
        setBlock2(tempBlock3);
        setBlock3(tempBlock1);
    }

    public int getValueAt(int i) {
        if (i < 0 || i >= cells.length) {
            throw new IllegalArgumentException("Index out of bounds for getValueAt.");
        }
        return cells[i];
    }

    @Override
    public String toString() {
        return super.toString() + "[row=" + row + ", column=" + column + ", cells=" + Arrays.toString(cells) + "]";
    }
}
