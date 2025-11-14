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
import asg.games.yipee.core.objects.YipeeRoom;
import asg.games.yipee.core.objects.YipeeTable;

/**
 * Defines termination logic for complex {@link YipeeObject} types.
 * <p>
 * This adapter is used by {@link TerminatorJPAVisitor} implementations to decouple deletion logic
 * from the core object structure. Each method provides a hook to perform any cleanup or cascading
 * deletion required for a specific object type.
 * </p>
 *
 * @author Blakbro2k
 */
public interface YipeeObjectTerminatorAdapter {

    /**
     * Terminates and cleans up resources associated with the given {@link YipeeRoom}.
     * This may involve deleting related tables, players, and internal state.
     *
     * @param room the {@link YipeeRoom} to terminate
     */
    void visitTerminateYipeeRoom(YipeeRoom room);

    /**
     * Terminates and cleans up resources associated with the given {@link YipeeTable}.
     * This may involve deleting related seats, watchers, and any associated state.
     *
     * @param table the {@link YipeeTable} to terminate
     */
    void visitTerminateYipeeTable(YipeeTable table);
}
