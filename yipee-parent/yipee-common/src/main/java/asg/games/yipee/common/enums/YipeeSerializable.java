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

import java.io.Serializable;

/**
 * Marker interface indicating that an object is eligible for serialization in the Yipee
 * networking and persistence layers.
 *
 * <p>This interface extends {@link java.io.Serializable} to ensure compatibility with
 * standard Java serialization frameworks while also providing a lightweight identifier
 * mechanism used throughout the Yipee engine for routing and introspection of network
 * payloads.
 *
 * <p>All network-transferred objects—such as packets, DTOs, game table metadata, and
 * state containers—implement this interface to unify their behavior across server,
 * desktop, mobile, and GWT-based clients.
 */
public interface YipeeSerializable extends Serializable {

    /**
     * Returns the simple class name of this object, used as a logical type identifier
     * for logging, debugging, or network-routing purposes.
     *
     * <p>This default implementation avoids requiring every subclass to implement a
     * redundant getter, while still providing a stable, human-readable identifier.
     *
     * @return the simple name of this class, serving as a packet or object type string
     */
    default String getPacketType() {
        return this.getClass().getSimpleName();
    }
}
