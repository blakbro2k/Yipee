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
package asg.games.yipee.core.persistence;

import asg.games.yipee.core.objects.YipeeObject;

/**
 * The Saveable interface defines methods for managing the persistence of
 * YipeeObject instances. It provides functionality for saving objects,
 * committing changes, and rolling back transactions, ensuring robust
 * and consistent database operations.
 * <p>
 * Typical use cases include managing game data persistence, such as
 * saving player progress, updating game state, and handling transactional
 * operations with the underlying database.
 */
public interface Saveable {

    /**
     * Saves a persistent object to the database.
     *
     * @param object the YipeeObject instance to be saved
     * @param <T>    the type of the object, extending YipeeObject
     */
    <T extends YipeeObject> void saveObject(T object);

    /**
     * Deletes a persistent object to the database.
     *
     * @param object the YipeeObject instance to be deleted
     * @param <T>    the type of the object, extending YipeeObject
     */
    <T extends YipeeObject> boolean deleteObject(T object);

    /**
     * Commits all pending transactions to make changes permanent.
     * Typically called after a series of save operations to ensure
     * data integrity and consistency in the database.
     */
    void commitTransactions();

    /**
     * Rolls back all pending transactions, discarding any changes
     * made since the last commit. This ensures no partial or invalid
     * data is persisted if an error occurs during the transaction process.
     */
    void rollBackTransactions();
}
