package asg.games.yipee.common.game;

/**
 * Represents a single block movement caused by gravity, collapse, or board resolution
 * after one or more blocks have been broken.
 *
 * <p>A {@code BlockMove} describes the transition of a block from its original
 * board position to a target row. It is primarily used for:
 * <ul>
 *   <li>Animating falling blocks on the client</li>
 *   <li>Synchronizing collapse behavior between server and clients</li>
 *   <li>Supporting deterministic replay and client-side prediction</li>
 * </ul>
 *
 * <p>This interface is part of the {@code yipee-common} module and intentionally
 * contains only primitive accessors to ensure:
 * <ul>
 *   <li>Cross-platform compatibility (server, desktop, mobile, GWT)</li>
 *   <li>Serialization safety</li>
 *   <li>Isolation from platform-specific collections or rendering types</li>
 * </ul>
 *
 * <p>Concrete implementations may be mutable internally, but consumers of this
 * interface should treat instances as immutable state descriptors unless explicitly
 * documented otherwise.</p>
 */
public interface BlockMove {
    /**
     * Returns the integer identifier representing the block type being moved.
     *
     * @return block type or ID
     */
    int getBlock();

    /**
     * Returns the original row position of the block before the move.
     *
     * @return source row index
     */
    int getRow();

    /**
     * Returns the column position of the block.
     *
     * @return column index
     */
    int getCol();

    /**
     * Returns the unique cell identifier associated with this block, if applicable.
     *
     * <p>This value may be used for animation tracking, ordering guarantees,
     * or debugging purposes.</p>
     *
     * @return cell identifier
     */
    int getCellId();

    /**
     * Returns the destination row that the block is moving toward.
     *
     * <p>The target row represents the final resting position after gravity
     * or collapse resolution.</p>
     *
     * @return target row index
     */
    int getTargetRow();

    /**
     * Sets the integer identifier representing the block type being moved.
     *
     * @param block block type or ID
     */
    void setBlock(int block);

    /**
     * Sets the original row position of the block before the move.
     *
     * @param row source row index
     */
    void setRow(int row);

    /**
     * Sets the unique cell identifier associated with this move, if applicable.
     *
     * @param cellId cell identifier
     */
    void setCellId(int cellId);

    /**
     * Sets the destination row that the block is moving toward.
     *
     * @param targetRow target row index
     */
    void setTargetRow(int targetRow);
}