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
package asg.games.yipee.core.objects;

/**
 * Interface for objects that can produce shallow and deep copies of themselves.
 *
 * <p>Implementing this interface allows game objects to support controlled cloning:
 * <ul>
 *   <li><b>Shallow copy</b>: creates a new instance with references to the same sub-objects.</li>
 *   <li><b>Deep copy</b>: creates a fully independent copy with recursively duplicated fields.</li>
 * </ul>
 *
 * <p>Use cases include duplicating game state for rollback, prediction, and network synchronization.
 *
 * @param <T> The type of the object being copied.
 */
public interface Copyable<T> {
    /**
     * Creates a shallow copy of this object.
     * <p>
     * The new instance will share references to any mutable sub-objects.
     * Suitable when only the top-level object needs duplication.
     *
     * @return A new instance with shared internal references.
     */
    T copy();

    /**
     * Creates a deep copy of this object.
     * <p>
     * The new instance will recursively copy all fields,
     * producing a fully independent clone safe for modification
     * without affecting the original.
     *
     * @return A new instance with fully duplicated internal state.
     */
    T deepCopy();
}
