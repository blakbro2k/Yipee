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
package asg.games.yipee.common.tools;

import java.util.Arrays;

/**
 * Utility class for performing static operations on arrays and matrices.
 */
public class StaticArrayUtils {
    public static final int[][] EMPTY_2D_INT_ARRAY = new int[0][0];

    /**
     * Private instantiation to hide public instantiation og a utility class
     */
    private StaticArrayUtils(){}

    /**
     * Creates a deep copy of a 2D integer matrix.
     *
     * @param original the original matrix to copy
     * @return a new matrix with the same values
     */
    public static int[][] copyIntMatrix(int[][] original) {
        if (original == null) return EMPTY_2D_INT_ARRAY;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = (original[i] != null) ? Arrays.copyOf(original[i], original[i].length) : null;
        }
        return copy;
    }
}
