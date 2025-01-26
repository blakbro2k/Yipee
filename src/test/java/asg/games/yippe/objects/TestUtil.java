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
package asg.games.yippe.objects;

import asg.games.yipee.game.YipeeGameBoard;
import asg.games.yipee.tools.RandomUtil;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtil {
    AtomicInteger atomicId = new AtomicInteger(0);

    @Test()
    public void testRandomArray() {
        YipeeGameBoard.TestRandomBlockArray testNextBlocks = new YipeeGameBoard.TestRandomBlockArray(2048, -1, 6);
        RandomUtil.RandomNumberArray nextBlocks = new RandomUtil.RandomNumberArray(2048, 1, 6);

        System.out.println("testArray=" + Arrays.toString(randomToArray(testNextBlocks)));
        System.out.println("realArray=" + Arrays.toString(randomToArray(nextBlocks)));
    }

    private int[] randomToArray(RandomUtil.RandomNumberArray blocksArray) {
        int[] testReturn = new int[2048];
        for (int i = 0; i < 2048; i++) {
            testReturn[i] = blocksArray.getRandomNumberAt(i);
        }
        return testReturn;
    }
}