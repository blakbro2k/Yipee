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

import asg.games.yipee.common.enums.YipeeObject;

/**
 * The Resolver interface defines methods for resolving and interacting with
 * YipeeObject instances, such as retrieving objects by name or ID, getting all
 * objects of a given type, and counting objects. It provides generic support for
 * different types of YipeeObjects.
 * <p>
 * Typical use cases for this interface include persistence management, querying,
 * and runtime object resolution within the Yipee game.
 */
public interface Resolver {
    /**
     * Retrieves an object of the specified class by its name.
     *
     * @param clazz the class type of the object to retrieve
     * @param name the name of the object to retrieve
     * @param <T> the type parameter that extends YipeeObject
     * @return an object of type T matching the given name, or null if no match is found
     */
    <T extends YipeeObject> T getObjectByName(Class<T> clazz, String name);

    /**
     * Retrieves an object of the specified class by its unique ID.
     *
     * @param clazz the class type of the object to retrieve
     * @param id    the unique ID of the object to retrieve
     * @param <T>   the type parameter that extends YipeeObject
     * @return an object of type T matching the given ID, or null if no match is found
     * @throws Exception if the retrieval process encounters an error
     */
    <T extends YipeeObject> T getObjectById(Class<T> clazz, String id) throws Exception;

    /**
     * Retrieves all objects of the specified class.
     *
     * @param clazz the class type of objects to retrieve
     * @param <T> the type parameter that extends YipeeObject
     * @return an Iterable collection of all objects of type T
     */
    <T extends YipeeObject> Iterable<T> getAllObjects(Class<T> clazz);

    /**
     * Returns the total count of all objects of the specified class.
     *
     * @param clazz the class type of objects to count
     * @param <T> the type parameter that extends YipeeObject
     * @return the number of objects of type T
     */
    <T extends YipeeObject> int countAllObjects(Class<T> clazz);
}
