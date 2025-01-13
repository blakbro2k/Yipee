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
package asg.games.yipee.objects;

import asg.games.yipee.game.YipeeBlockEval;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created by Blakbro2k on 12/29/2017.
 */
/**
 * Represents an individual block in the Yipee game, which may have unique
 * properties and power intensities. Each {@code YipeeBlock} has a specific
 * type, position, and potentially a power level that determines its effect in the game.
 *
 * <p>Blocks can be categorized into various types such as offensive, defensive,
 * and special blocks. The type and power level of a block influence its behavior
 * and interactions during gameplay.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Maintains a {@code blockType} that identifies the block's role or effect.</li>
 *   <li>Tracks position on the game grid with {@code x} and {@code y} coordinates.</li>
 *   <li>Supports power intensities ranging from minor to mega, which can be adjusted
 *       within predefined bounds.</li>
 *   <li>Includes reset functionality to reinitialize the block to a default state.</li>
 * </ul>
 *
 * <h2>Block Types:</h2>
 * <ul>
 *   <li>{@link #CLEAR_BLOCK} - Represents an empty or clear block.</li>
 *   <li>{@link #OFFENSIVE_MINOR}, {@link #DEFENSIVE_MINOR}, etc. -
 *       Represent blocks with varying levels of offensive or defensive power.</li>
 *   <li>{@link #MEDUSA}, {@link #TOP_MIDAS} - Special types of blocks.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 *     // Create a new block of a specific type
 *     YipeeBlock block = new YipeeBlock(5, 7, YipeeBlock.OFFENSIVE_MINOR);
 *
 *     // Set power intensity
 *     block.setPowerIntensity(5);
 *
 *     // Check if the block has power
 *     if (block.hasPower()) {
 *         System.out.println("Block has active power.");
 *     }
 *
 *     // Reset the block
 *     block.reset();
 * </pre>
 *
 * <h2>Reset Functionality:</h2>
 * <p>The {@code reset()} method resets the block to its default state,
 * including position, block type, and power intensity. This is useful when
 * reusing blocks during gameplay.</p>
 *
 * @author Blakbro2k
 * @version 1.0
 * @see AbstractYipeeObject
 * @see asg.games.yipee.game.YipeeBlockEval
 */
@Getter
@Setter
public class YipeeBlock extends AbstractYipeeObject implements Disposable {
    private static final Logger logger = LoggerFactory.getLogger(YipeeBlock.class);

    public static final int Y_BLOCK = 0;
    public static final int A_BLOCK = 1;
    public static final int H_BLOCK = 2;
    public static final int Op_BLOCK = 3;
    public static final int Oy_BLOCK = 4;
    public static final int EX_BLOCK = 5;
    public static final int CLEAR_BLOCK = 6;
    public static final int STONE = 7;
    public static final int MEDUSA = 9;
    public static final int ACTIVE_MEDUSA = 2105;
    public static final int TOP_MIDAS = 8;
    public static final int ACTIVE_TOP_MIDAS = 2088;
    public static final int MID_MIDAS = 10;
    public static final int ACTIVE_MID_MIDAS = 2090;
    public static final int BOT_MIDAS = 11;
    public static final int ACTIVE_BOT_MIDAS = 2091;
    public static final int OFFENSIVE_MINOR = 3;
    public static final int OFFENSIVE_REGULAR = 5;
    public static final int OFFENSIVE_MEGA = 7;
    public static final int DEFENSIVE_MINOR = 2;
    public static final int DEFENSIVE_REGULAR = 4;
    public static final int DEFENSIVE_MEGA = 6;
    public static final int SPECIAL_BLOCK_1 = 1024;
    public static final int SPECIAL_BLOCK_2 = 1026;
    public static final int SPECIAL_BLOCK_3 = 1028;
    public static final int OFFENSIVE_Y_BLOCK_MINOR = 2096;
    public static final int OFFENSIVE_Y_BLOCK_REGULAR = 2128;
    public static final int OFFENSIVE_Y_BLOCK_MEGA = 2160;
    public static final int DEFENSIVE_Y_BLOCK_MINOR = 2080;
    public static final int DEFENSIVE_Y_BLOCK_REGULAR = 2112;
    public static final int DEFENSIVE_Y_BLOCK_MEGA = 2144;
    public static final int OFFENSIVE_O_BLOCK_MINOR = 2097;
    public static final int OFFENSIVE_O_BLOCK_REGULAR = 2129;
    public static final int OFFENSIVE_O_BLOCK_MEGA = 2161;
    public static final int DEFENSIVE_O_BLOCK_MINOR = 2081;
    public static final int DEFENSIVE_O_BLOCK_REGULAR = 2113;
    public static final int DEFENSIVE_O_BLOCK_MEGA = 2145;
    public static final int OFFENSIVE_K_BLOCK_MINOR = 2098;
    public static final int OFFENSIVE_K_BLOCK_REGULAR = 2030;
    public static final int OFFENSIVE_K_BLOCK_MEGA = 2130;
    public static final int DEFENSIVE_K_BLOCK_MINOR = 2082;
    public static final int DEFENSIVE_K_BLOCK_REGULAR = 2114;
    public static final int DEFENSIVE_K_BLOCK_MEGA = 2146;
    public static final int OFFENSIVE_E_BLOCK_MINOR = 2099;
    public static final int OFFENSIVE_E_BLOCK_REGULAR = 2131;
    public static final int OFFENSIVE_E_BLOCK_MEGA = 2163;
    public static final int DEFENSIVE_E_BLOCK_MINOR = 2083;
    public static final int DEFENSIVE_E_BLOCK_REGULAR = 2115;
    public static final int DEFENSIVE_E_BLOCK_MEGA = 2147;
    public static final int OFFENSIVE_L_BLOCK_MINOR = 2100;
    public static final int OFFENSIVE_L_BLOCK_REGULAR = 2132;
    public static final int OFFENSIVE_L_BLOCK_MEGA = 2164;
    public static final int DEFENSIVE_L_BLOCK_MINOR = 2084;
    public static final int DEFENSIVE_L_BLOCK_REGULAR = 2116;
    public static final int DEFENSIVE_L_BLOCK_MEGA = 2148;
    public static final int OFFENSIVE_BASH_BLOCK_MINOR = 2101;
    public static final int OFFENSIVE_BASH_BLOCK_REGULAR = 2133;
    public static final int OFFENSIVE_BASH_BLOCK_MEGA = 2165;
    public static final int DEFENSIVE_BASH_BLOCK_MINOR = 2085;
    public static final int DEFENSIVE_BASH_BLOCK_REGULAR = 2117;
    public static final int DEFENSIVE_BASH_BLOCK_MEGA = 2149;
    private static final int POWER_INTENSITY_LOWER_BOUNDS = 3;
    private static final int POWER_INTENSITY_UPPER_BOUNDS = 7;

    public int x;
    public int y;
    private int blockType;
    private int powerIntensity = 0;

    //Empty Constructor required for Json.Serializable
    public YipeeBlock() {
    }

    @Override
    public void dispose() {
        reset();
    }

    public YipeeBlock(int x, int y, int blockType) {
        reset();
        this.x = x;
        this.y = y;
        this.blockType = blockType;
    }

    public YipeeBlock(int x, int y) {
        this(x, y, CLEAR_BLOCK);
    }

    public void reset() {
        if (logger.isInfoEnabled()) {
            logger.info("Resetting block by zeroing out values to default");
        }
        this.x = 0;
        this.y = 0;
        this.blockType = CLEAR_BLOCK;
        this.powerIntensity = 0;
    }

    /**
     * Retrieves power "level"
     * <p>
     * - Even represents defensive powers ( 2, 4, 6 )
     * - Odd represents attack powers ( 3, 5, 7 )
     * ( MINOR, REGULAR, MEGA )
     * <p>
     * @return powerIntensity
     */
    public int getPowerIntensity() {
        // Starts at 3 to start with an attack block first
        if (powerIntensity <= 1) {
            if (logger.isInfoEnabled()) {
                logger.info("Setting power to lower bound: [{}]", POWER_INTENSITY_LOWER_BOUNDS);
            }
            powerIntensity = POWER_INTENSITY_LOWER_BOUNDS;
        }
        if (powerIntensity > POWER_INTENSITY_UPPER_BOUNDS) {
            if (logger.isInfoEnabled()) {
                logger.info("Setting power to upper bound: [{}]", POWER_INTENSITY_UPPER_BOUNDS);
            }
            powerIntensity = POWER_INTENSITY_UPPER_BOUNDS;
        }
        return powerIntensity;
    }

    public boolean hasPower(){
        return powerIntensity > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBlock that = (YipeeBlock) o;
        return x == that.x && y == that.y && getBlockType() == that.getBlockType() && getPowerIntensity() == that.getPowerIntensity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), x, y, getBlockType(), getPowerIntensity());
    }

    public static String printReaderFriendlyBlock(int block) {
        if (block == YipeeBlock.CLEAR_BLOCK) {
            return "' '";
        } else {
            if (YipeeBlockEval.hasPowerBlockFlag(block)) {
                return "" + YipeeBlockEval.getPowerLabel(block);
            } else {
                return "" + YipeeBlockEval.getNormalLabel(block);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + "{block: [" + blockType + "]" + YipeeBlock.printReaderFriendlyBlock(blockType) + "}";
    }
}