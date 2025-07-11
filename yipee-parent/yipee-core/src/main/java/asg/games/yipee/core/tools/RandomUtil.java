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
package asg.games.yipee.core.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

/**
 * Provides the methods necessary to have deterministic random blocks given a common seed
 * <p>
 *
 * @since 1.0
 * @author Blakbro2k on 12/29/2017
 */
public class RandomUtil {
    private static final Logger logger = LoggerFactory.getLogger(RandomUtil.class);

    public static final int ATTACK_SECTION = 6;
    public static final int SECTION_GROUP_NUM = 6;
    public static final int DEFENSE_SECTION = 12;

    /**
     * @see <a href="http://stackoverflow.com/a/1973018">Stack Overflow</a>
     */
    private static class RandomEnum<E extends Enum> {

        private static final Random RND = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

        /**
         * @return a random value for the given enums
         */
        public E random() {
            return values[RND.nextInt(values.length)];
        }

        /**
         * @return a random value for the given enums and a min and max range
         */
        public E random(int min) {
            return values[min + RND.nextInt(SECTION_GROUP_NUM)];
        }
    }

    public static final class RandomNumber {
        private long seed;
        //private boolean c = false;
        //private static final long f = (1L << 48) - 1L;

        public RandomNumber() {
            this(TimeUtils.millis());
        }

        public RandomNumber(long l) {
            generateSeedByKey(l);
        }

        public synchronized void generateSeedByKey(long l) {
            seed = (l ^ 0x5deece66dL) & (1L << 48) - 1L;
            //c = false;
        }

        protected synchronized int moveSeed(int i) {
            long l = seed * 25214903917L + 11L & (1L << 48) - 1L;
            seed = l;
            return (int) (l >>> 48 - i);
        }

        public int generateCappedNumber() {
            return moveSeed(32);
        }

        public int next(int i) {
            return (generateCappedNumber() & 0x7fffffff) % i;
        }

        public void printOut(DataOutputStream dataoutputstream) throws IOException {
            dataoutputstream.writeLong(seed);
        }

        public void readIn(DataInputStream datainputstream) throws IOException {
            seed = datainputstream.readLong();
        }
    }

    public static class RandomNumberArray implements Serializable {
        int[] randomNumbers;

        public RandomNumberArray(int byteLength, long seed, int maxValue) {
            randomNumbers = new int[byteLength];
            RandomNumber numberGenerator = new RandomNumber(seed);
            for (int i = 0; i < byteLength; i++)
                randomNumbers[i] = numberGenerator.next(maxValue);
        }

        public int getRandomNumberAt(int index) {
            if (index < 0)
                System.out.println("Assertion failure: invalid random index " + index);
            return randomNumbers[index % randomNumbers.length];
        }
    }

}
