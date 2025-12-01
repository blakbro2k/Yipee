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
package asg.games.yipee.common.game;

/**
 * Represents an indexed source of deterministic pseudo-random numbers shared across
 * multiple game systems.
 *
 * <p>This abstraction is used by the Yipee engine to ensure that different players,
 * boards, and clients operate on identical random-number sequences when required,
 * particularly during synchronized gameplay modes. Implementations may wrap a static
 * array, seeded PRNG output, or an externally provided deterministic dataset.
 *
 * <p>The interface intentionally exposes only indexed access (rather than sequential
 * mutation or stateful generation) to guarantee reproducibility and simplify rollback
 * and prediction logic used by clients.
 */
public interface CommonRandomNumberArray {

    /**
     * Returns the random number at the specified index.
     *
     * <p>The index is expected to fall within the bounds of the internal random-number
     * sequence. Implementations may choose to throw exceptions or clamp indices as
     * appropriate for the calling context.
     *
     * @param index the zero-based index into the random-number sequence
     * @return the random number located at the specified index
     */
    int getRandomNumberAt(int index);
}
