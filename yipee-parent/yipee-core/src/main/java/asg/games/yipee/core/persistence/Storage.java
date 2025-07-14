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
package asg.games.yipee.core.persistence;

import asg.games.yipee.core.objects.Disposable;

/**
 * The Storage interface provides a unified contract for managing
 * persistence, resource cleanup, and object resolution within the
 * Yipee game. It combines the functionality of the following interfaces:
 * <p>
 * - {@link Saveable}: Handles saving, committing, and rolling back changes
 * to persistent objects.
 * - {@link Disposable}: Ensures proper cleanup of resources when they are
 * no longer needed.
 * - {@link Resolver}: Facilitates object resolution, such as retrieving
 * objects by name, ID, or type, and performing related queries.
 * <p>
 * Implementations of this interface are expected to handle all aspects of
 * persistence and resource management in a cohesive manner.
 * <p>
 * Typical use cases include:
 * - Storing and retrieving game state or player data.
 * - Cleaning up resources during server shutdown or game session termination.
 * - Resolving objects dynamically during runtime.
 */
public interface Storage extends Saveable, Disposable, Resolver {
}
