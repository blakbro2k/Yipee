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
 * Defines the visibility and join permissions for a Yipee game table.
 *
 * <p>Access types determine who may join or observe a table within a room. This
 * information is shared across server and client boundaries via the common DTO
 * layer to maintain consistent table-selection logic.
 *
 * <ul>
 *     <li>{@code PUBLIC} — Anyone may join or watch the table.</li>
 *     <li>{@code PRIVATE} — Only invited players may join; watchers may be restricted.</li>
 *     <li>{@code PROTECTED} — Visible to the room, but joinable only under special rules
 *         such as passwords, minimum rating requirements, or host confirmation.</li>
 * </ul>
 */
public enum ACCESS_TYPE {

    /**
     * A restricted-access table type where only invited or explicitly authorized
     * players may join. Watcher permissions may also be limited depending on server policy.
     */
    PRIVATE(Constants.ENUM_VALUE_PRIVATE),

    /**
     * A fully open table that can be joined or observed by any player in the room.
     */
    PUBLIC(Constants.ENUM_VALUE_PUBLIC),

    /**
     * A partially restricted table that is visible to all players but may require
     * specific conditions to join, such as a password or host approval.
     */
    PROTECTED(Constants.ENUM_VALUE_PROTECTED);

    /**
     * Canonical string value representing this access type.
     *
     * <p>These values are defined in {@link Constants} to centralize string usage
     * across serialization, persistence, and UI-display logic.
     */
    private final String accessType;

    /**
     * Constructs a new {@code ACCESS_TYPE} with the provided canonical string value.
     *
     * @param accessType the string representation associated with this access level
     */
    ACCESS_TYPE(String accessType) {
        this.accessType = accessType;
    }

    /**
     * Returns the canonical string value associated with this access type.
     *
     * <p>This method is typically used during serialization, configuration
     * matching, or translating access rules to human-readable forms.
     *
     * @return the string representation of this access type
     */
    public String getValue() {
        return accessType;
    }
}
