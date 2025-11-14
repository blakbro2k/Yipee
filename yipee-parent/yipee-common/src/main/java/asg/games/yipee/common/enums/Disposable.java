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
package asg.games.yipee.common.enums;

/**
 * Represents an object that can release internal resources when no longer needed.
 *
 * <p>Implementations of this interface typically clear internal collections, detach
 * references, or free platform-specific resources on both client and server. Once a
 * {@code Disposable} object has been disposed, it should be considered unusable and
 * should not be accessed further.
 *
 * <p>This interface provides symmetry with other lifecycle-related interfaces in the
 * Yipee model and is used throughout the serialization, networking, and runtime logic.
 */
public interface Disposable {

    /**
     * Releases all internal resources and performs any necessary cleanup.
     *
     * <p>After invoking this method, the object should transition to an invalid or
     * neutral state and must not retain references that could lead to memory leaks
     * or inconsistent behavior.
     */
    void dispose();
}
