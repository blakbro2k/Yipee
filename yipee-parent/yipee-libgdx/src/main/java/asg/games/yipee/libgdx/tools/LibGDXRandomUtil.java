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
package asg.games.yipee.libgdx.tools;

import asg.games.yipee.common.game.CommonRandomNumberArray;
import com.badlogic.gdx.utils.TimeUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Provides deterministic random number utilities for the Yipee game,
 * ensuring that random sequences can be reproduced from a shared seed.
 * <p>
 * Supports attack/defense section constants, seeded number generation,
 * and reusable random enum selection.
 * </p>
 *
 * @author Blakbro2k
 * @since 1.0
 */
public class LibGDXRandomUtil {
    private static final Logger logger = Logger.getLogger(LibGDXRandomUtil.class.getSimpleName());

    /**
     * Number of blocks in an attack section.
     */
    public static final int ATTACK_SECTION = 6;

    /** Total number of section groups. */
    public static final int SECTION_GROUP_NUM = 6;

    /** Number of blocks in a defense section. */
    public static final int DEFENSE_SECTION = 12;

    /**
     * Utility for selecting random enum values.
     * @param <E> Enum type to randomize.
     */
    private static class RandomEnum<E extends Enum> {
        private static final Random RND = new Random();
        private final E[] values;

        /**
         * Constructs a random enum generator for the given enum class.
         * @param token the enum class to randomize.
         */
        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

        /**
         * Returns a random value from the enum constants.
         * @return a random enum value.
         */
        public E random() {
            return values[RND.nextInt(values.length)];
        }

        /**
         * Returns a random enum value within a bounded index range.
         * Uses a fixed section group size.
         *
         * @param min the starting index within the enum constants.
         * @return a random enum value from [min, min + SECTION_GROUP_NUM).
         */
        public E random(int min) {
            return values[min + RND.nextInt(SECTION_GROUP_NUM)];
        }
    }

    /**
     * Deterministic pseudo-random number generator using a custom seed.
     * Used for reproducible randomness across different game clients.
     */
    public static final class RandomNumber {
        private long seed;

        /**
         * Initializes the generator using the current time in milliseconds.
         */
        public RandomNumber() {
            this(TimeUtils.millis());
        }

        /**
         * Initializes the generator with a given seed value.
         * @param l the seed.
         */
        public RandomNumber(long l) {
            generateSeedByKey(l);
        }

        /**
         * Generates an internal seed using a given key, similar to Java's {@link Random}.
         * @param l seed base.
         */
        public synchronized void generateSeedByKey(long l) {
            seed = (l ^ 0x5deece66dL) & ((1L << 48) - 1L);
        }

        /**
         * Advances the internal seed and returns a value.
         * @param i number of bits to shift.
         * @return the shifted output value.
         */
        protected synchronized int moveSeed(int i) {
            long l = seed * 25214903917L + 11L & ((1L << 48) - 1L);
            seed = l;
            return (int) (l >>> (48 - i));
        }

        /**
         * Generates a capped 32-bit number from the current seed.
         * @return capped pseudo-random number.
         */
        public int generateCappedNumber() {
            return moveSeed(32);
        }

        /**
         * Returns a bounded random integer in the range [0, i).
         * @param i the upper bound (exclusive).
         * @return pseudo-random value in range.
         */
        public int next(int i) {
            return (generateCappedNumber() & 0x7fffffff) % i;
        }

        /**
         * Writes the current seed to a binary output stream.
         * @param dataoutputstream the output stream.
         * @throws IOException if an I/O error occurs.
         */
        public void printOut(DataOutputStream dataoutputstream) throws IOException {
            dataoutputstream.writeLong(seed);
        }

        /**
         * Reads and sets the seed from a binary input stream.
         * @param datainputstream the input stream.
         * @throws IOException if an I/O error occurs.
         */
        public void readIn(DataInputStream datainputstream) throws IOException {
            seed = datainputstream.readLong();
        }
    }

    /**
     * Pre-generates an array of random numbers using a shared seed and max value.
     * Useful for deterministic block spawning or event scheduling.
     */
    public static class RandomNumberArray implements CommonRandomNumberArray, Serializable {
        int[] randomNumbers;

        /**
         * Constructs an array of pseudo-random integers with a shared seed.
         *
         * @param byteLength the size of the array.
         * @param seed the seed for random generation.
         * @param maxValue the upper bound for values (exclusive).
         */
        public RandomNumberArray(int byteLength, long seed, int maxValue) {
            randomNumbers = new int[byteLength];
            RandomNumber numberGenerator = new RandomNumber(seed);
            for (int i = 0; i < byteLength; i++)
                randomNumbers[i] = numberGenerator.next(maxValue);
        }

        /**
         * Retrieves a random number at the given index (with bounds wrapping).
         * @param index the index of the value.
         * @return the pseudo-random number at that index.
         */
        public int getRandomNumberAt(int index) {
            if (index < 0)
                System.out.println("Assertion failure: invalid random index " + index);
            return randomNumbers[index % randomNumbers.length];
        }
    }
}
