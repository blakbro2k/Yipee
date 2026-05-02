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

/**
 * Represents a single block that has been broken (matched or destroyed) on a game board.
 *
 * <p>A {@code BrokenBlock} provides a minimal, cross-platform description of a block
 * that has been removed from the board, including its block type and original grid
 * position. It is primarily used for:
 * <ul>
 *   <li>Client-side break and explosion animations</li>
 *   <li>Server-to-client state synchronization</li>
 *   <li>Deterministic replay and client-side prediction</li>
 * </ul>
 *
 * <p>This interface is part of the {@code yipee-common} module and intentionally
 * exposes only primitive accessors to ensure:
 * <ul>
 *   <li>Compatibility across server, desktop, mobile, and GWT clients</li>
 *   <li>Safe serialization and deserialization</li>
 *   <li>Isolation from platform-specific data structures</li>
 * </ul>
 *
 * <p>Implementations may be mutable internally; however, consumers of this interface
 * should treat instances as immutable snapshots of game state unless explicitly
 * documented otherwise.</p>
 */
public interface BrokenBlock {

    /**
     * Returns the integer identifier representing the block type.
     *
     * @return block type or ID
     */
    int getBlock();

    /**
     * Returns the row position where the block was located before breaking.
     *
     * @return row index
     */
    int getRow();

    /**
     * Returns the column position where the block was located before breaking.
     *
     * @return column index
     */
    int getCol();

    /**
     * Sets the integer identifier representing the broken block type.
     *
     * @param block block type or ID
     */
    void setBlock(int block);

    /**
     * Sets the row position where the block was located before breaking.
     *
     * @param row row index
     */
    void setRow(int row);

    /**
     * Sets the column position where the block was located before breaking.
     *
     * @param col column index
     */
    void setCol(int col);
}