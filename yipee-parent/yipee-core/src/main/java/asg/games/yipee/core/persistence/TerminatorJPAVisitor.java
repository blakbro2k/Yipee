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
 * A visitor interface for deleting complex, nested {@link YipeeObject} structures.
 * <p>
 * Implementing classes should define how to handle deletion of child or related entities through
 * the provided {@link YipeeObjectTerminatorAdapter}. This is typically used in persistence layers
 * to cascade deletes in a controlled, domain-aware manner rather than relying entirely on JPA cascade options.
 * </p>
 *
 * <p>Follows the Visitor design pattern to decouple deletion logic from the entity structure.</p>
 *
 * @author Blakbro2k
 */
public interface TerminatorJPAVisitor {

    /**
     * Accepts a {@link YipeeObjectTerminatorAdapter} to perform domain-specific delete operations.
     *
     * @param terminatorAdapter the adapter responsible for managing child deletion logic
     */
    void visitDelete(YipeeObjectTerminatorAdapter terminatorAdapter);
}
