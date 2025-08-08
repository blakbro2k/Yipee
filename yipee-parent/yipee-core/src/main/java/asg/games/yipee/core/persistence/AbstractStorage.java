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

import asg.games.yipee.core.objects.YipeeObject;
import asg.games.yipee.core.tools.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Abstract base class for all storage backends (e.g., database, file, memory) in the Yipee game system.
 * <p>
 * Provides common functionality for resolving objects by ID or name via reflection, and defines
 * abstract operations that subclasses must implement to persist and retrieve {@link YipeeObject}s.
 *
 * <p>All implementing classes must define concrete behavior for saving, fetching, committing, and rolling back objects.
 *
 * @author Blakbro2k
 */
public abstract class AbstractStorage implements Storage {
    private static final Logger logger = LoggerFactory.getLogger(AbstractStorage.class);

    /**
     * Method name used to access the object's name via reflection.
     */
    private static final String STORAGE_NAME_METHOD = "getName";

    /**
     * Method name used to access the object's ID via reflection.
     */
    private static final String STORAGE_ID_METHOD = "getId";

    /**
     * Retrieves an object of the given class by its unique name.
     *
     * @param clazz the object type to retrieve
     * @param name the name to match
     * @param <T> the type of {@link YipeeObject}
     * @return the matching object, or {@code null} if not found
     */
    @Override
    public abstract <T extends YipeeObject> T getObjectByName(Class<T> clazz, String name);

    /**
     * Retrieves an object of the given class by its unique ID.
     *
     * @param clazz the object type to retrieve
     * @param id the unique identifier
     * @param <T> the type of {@link YipeeObject}
     * @return the matching object, or {@code null} if not found
     */
    @Override
    public abstract <T extends YipeeObject> T getObjectById(Class<T> clazz, String id);

    /**
     * Retrieves all objects of the given type.
     *
     * @param clazz the type of object to list
     * @param <T> the type of {@link YipeeObject}
     * @return an iterable list of all stored objects of the given type
     */
    @Override
    public abstract <T extends YipeeObject> Iterable<T> getAllObjects(Class<T> clazz);

    /**
     * Persists or updates the specified object in storage.
     *
     * @param object the object to save
     * @param <T> the type of {@link YipeeObject}
     */
    @Override
    public abstract <T extends YipeeObject> void saveObject(T object);

    /**
     * Commits all pending transactions or changes to persistent storage.
     */
    @Override
    public abstract void commitTransactions();

    /**
     * Rolls back any pending changes in the current transaction scope.
     */
    @Override
    public abstract void rollBackTransactions();

    /**
     * Attempts to invoke {@code getName()} or {@code getId()} reflectively on the provided object.
     * <p>
     * Useful when working with objects that implement {@link YipeeObject} but are typed loosely.
     *
     * @param o        the object instance to inspect
     * @param getName  if {@code true}, attempts to invoke {@code getName()}; otherwise {@code getId()}
     * @return the result of the invoked method as a string, or {@code null} if the result is null
     * @throws RuntimeException if the method does not exist or is inaccessible
     */
    protected String getNameOrIdFromInstance(Object o, boolean getName) {
        try {
            String methodName = getName ? "getName" : "getId";
            Method method = o.getClass().getMethod(methodName);
            Object result = method.invoke(o);
            return Util.otos(result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to reflectively access method", e);
        }
    }
}
