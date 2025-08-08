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

import asg.games.yipee.core.objects.Disposable;
import asg.games.yipee.core.objects.YipeeRoom;
import asg.games.yipee.core.objects.YipeeTable;

/**
 * Defines storage logic for complex {@link asg.games.yipee.core.objects.YipeeObject} structures,
 * allowing objects to delegate their persistence to a visitor-style adapter.
 * <p>
 * Implementations of this interface should encapsulate the logic for saving domain objects
 * such as {@link YipeeRoom} and {@link YipeeTable}, including their nested child relationships
 * and any custom persistence handling.
 * </p>
 *
 * <p>This interface follows the Visitor design pattern to separate persistence logic from domain models.</p>
 *
 * <p>Extends {@link Disposable} to allow clean resource management, such as closing sessions or connections.</p>
 *
 * @author Blakbro2k
 */
public interface YipeeStorageAdapter extends Disposable {

    /**
     * Saves the specified {@link YipeeRoom} and its internal structure to persistent storage.
     *
     * @param room the {@link YipeeRoom} to be saved
     */
    void visitSaveYipeeRoom(YipeeRoom room);

    /**
     * Saves the specified {@link YipeeTable} and its associated data to persistent storage.
     *
     * @param table the {@link YipeeTable} to be saved
     */
    void visitSaveYipeeTable(YipeeTable table);
}
