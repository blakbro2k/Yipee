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
package asg.games.yipee.common.enums;

/**
 * Base interface representing any identifiable and serializable object within the Yipee
 * ecosystem.
 *
 * <p>All network-transferred or persistable objects—such as players, tables, seats, and
 * game components—implement this interface to expose a minimal identity contract shared
 * across server and client layers. Implementations must also satisfy
 * {@link YipeeSerializable}, ensuring compatibility with the Yipee networking model.
 *
 * <p>The fields exposed here include unique identifiers, display names, and lifecycle
 * metadata (creation and modification timestamps), all of which support synchronization,
 * logging, persistence, and debugging across platforms.
 */
public interface YipeeObject extends YipeeSerializable {

    /**
     * Assigns a unique identifier to this object.
     *
     * <p>The identifier should remain stable for the lifetime of the object and is
     * commonly used for serialization, persistence, and cross-referencing between
     * game components.
     *
     * @param id the unique ID associated with this object
     */
    void setId(String id);

    /**
     * Returns the unique identifier associated with this object.
     *
     * @return the object's unique ID
     */
    String getId();

    /**
     * Sets the displayable name for this object.
     *
     * <p>Names are intended for human-readable purposes such as UI elements,
     * debugging logs, or administrative tools.
     *
     * @param name the name to assign
     */
    void setName(String name);

    /**
     * Returns the displayable name associated with this object.
     *
     * @return the object's name
     */
    String getName();

    /**
     * Sets the timestamp representing when this object was first created.
     *
     * <p>This value is typically expressed as epoch milliseconds and is used for
     * auditing, sorting, and consistency checks.
     *
     * @param dateTime the creation timestamp in milliseconds
     */
    void setCreated(long dateTime);

    /**
     * Returns the timestamp representing when this object was first created.
     *
     * @return the creation timestamp in milliseconds
     */
    long getCreated();

    /**
     * Sets the timestamp representing the last modification to this object.
     *
     * <p>This value is typically expressed as epoch milliseconds and should be
     * updated whenever significant state changes occur.
     *
     * @param dateTime the modification timestamp in milliseconds
     */
    void setModified(long dateTime);

    /**
     * Returns the timestamp representing the last modification to this object.
     *
     * @return the modification timestamp in milliseconds
     */
    long getModified();
}
