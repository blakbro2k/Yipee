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
 * Collection of global constant values shared across the Yipee engine.
 *
 * <p>This class provides identifiers, default sizes, and common static values that
 * are used throughout the common module and across client/server implementations.
 * The constants defined here are purely utility values and contain no mutable state.
 */
public class Constants {

    /**
     * Canonical string value representing the {@code PRIVATE} access type in
     * {@link ACCESS_TYPE}. Used for serialization, comparison, and user interface labels.
     */
    public static final String ENUM_VALUE_PRIVATE = "PRIVATE";

    /**
     * Canonical string value representing the {@code PUBLIC} access type in
     * {@link ACCESS_TYPE}. Used for serialization, comparison, and user interface labels.
     */
    public static final String ENUM_VALUE_PUBLIC = "PUBLIC";

    /**
     * Canonical string value representing the {@code PROTECTED} access type in
     * {@link ACCESS_TYPE}. Used for serialization, comparison, and user interface labels.
     */
    public static final String ENUM_VALUE_PROTECTED = "PROTECTED";

    /**
     * Private constructor to prevent instantiation.
     *
     * <p>This class is intended to function purely as a static constants container.
     */
    private Constants() {
    }
}
