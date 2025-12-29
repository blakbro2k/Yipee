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
package asg.games.yipee.common.dto;

import asg.games.yipee.common.enums.YipeeSerializable;

/**
 * Represents a basic network-serializable Yipee object with metadata fields such as ID, name,
 * creation timestamp, and modification timestamp.
 *
 * <p>This interface is intended to be implemented by all game-related data objects
 * that are transmitted across the network, ensuring consistent serialization and
 * lifecycle tracking across platforms (e.g., server, LibGDX, GWT).
 *
 * <p>Extends {@link YipeeSerializable} to mark it as part of the shared packet contract.
 *
 * @author Blakbro2k
 */
public interface NetYipeeObject extends YipeeSerializable {
    /**
     * Sets the unique identifier for this object.
     *
     * @param id the string ID to assign
     */
    void setId(String id);

    /**
     * Returns the unique identifier of this object.
     *
     * @return the string ID
     */
    String getId();

    /**
     * Sets the display or logical name of this object.
     *
     * @param name the name to assign
     */
    void setName(String name);

    /**
     * Returns the name of this object.
     *
     * @return the name string
     */
    String getName();

    /**
     * Sets the timestamp when this object was created.
     *
     * @param dateTime the epoch millisecond timestamp of creation
     */
    void setCreated(long dateTime);

    /**
     * Returns the creation timestamp of this object.
     *
     * @return the epoch millisecond timestamp of creation
     */
    long getCreated();

    /**
     * Sets the timestamp when this object was last modified.
     *
     * @param dateTime the epoch millisecond timestamp of modification
     */
    void setModified(long dateTime);

    /**
     * Returns the last modification timestamp of this object.
     *
     * @return the epoch millisecond timestamp of last modification
     */
    long getModified();
}
