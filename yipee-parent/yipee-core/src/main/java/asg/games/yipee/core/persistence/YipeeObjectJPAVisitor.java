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

/**
 * A visitor interface for saving complex, nested {@link asg.games.yipee.core.objects.YipeeObject} structures.
 * <p>
 * Implementing classes should define how to handle the persistence of child or related entities
 * through the provided {@link YipeeStorageAdapter}. This enables fine-grained control over save operations
 * and allows objects to coordinate the persistence of their internal structure.
 * </p>
 *
 * <p>Follows the Visitor design pattern to decouple save logic from the entity structure.</p>
 *
 * @author Blakbro2k
 */
public interface YipeeObjectJPAVisitor {

    /**
     * Accepts a {@link YipeeStorageAdapter} to perform domain-specific save operations.
     *
     * @param adapter the storage adapter responsible for saving child or related objects
     */
    void visitSave(YipeeStorageAdapter adapter);
}
