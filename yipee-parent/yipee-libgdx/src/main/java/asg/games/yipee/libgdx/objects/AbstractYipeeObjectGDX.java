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
package asg.games.yipee.libgdx.objects;

import com.badlogic.gdx.utils.TimeUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract base class for all Yipee game objects. This class implements the {@link YipeeObjectGDX} interface
 * and provides common properties and methods shared by all game objects in the Yipee system.
 * </br>
 * It includes an {@code id}, {@code name}, and timestamps for creation and modification. Subclasses will inherit
 * these properties and may extend them with specific game logic.
 * </br>
 * </br>
 * This class is mapped to a database table as a superclass for other entities in the Yipee game system, such as
 * {@link YipeeBlockGDX}, {@link YipeePlayerGDX}, and others.
 * </br>
 *
 * @author Blakbro2k
 * @version 1.0
 * </br>
 * @see YipeeObjectGDX
 * @see YipeeBlockGDX
 * @see YipeeBlockMoveGDX
 * @see YipeeBoardPairGDX
 * @see YipeeBrokenBlockGDX
 * @see YipeeClockGDX
 * @see YipeeGameBoardStateGDX
 * @see YipeePieceGDX
 * @see YipeePlayerGDX
 * @see YipeeRoomGDX
 * @see YipeeSeatGDX
 * @see YipeeTableGDX
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractYipeeObjectGDX implements YipeeObjectGDX {
    protected String id;

    /**
     * Name of the Yipee object, must be unique
     */
    protected String name;

    /**
     * Timestamp representing the creation time of the Yipee object (in milliseconds)
     */
    protected long created;

    /**
     * Timestamp representing the last modification time of the Yipee object (in milliseconds)
     */
    protected long modified;

    /**
     * Default constructor, which sets the creation and modification timestamps to the current time (in milliseconds).
     */
    AbstractYipeeObjectGDX() {
        setCreated(TimeUtils.millis());
        setModified(TimeUtils.millis());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + this.getId() + "," + this.getName() + "]";
    }

    /**
     * Copies common properties from the parent Yipee object to another Yipee object.
     * This method ensures that the {@code id}, {@code name}, {@code created}, and {@code modified} fields are copied,
     * but the {@code id} is set to {@code null} to indicate it is a new instance.
     *
     * @param o the Yipee object to copy properties to
     */
    protected void copyParent(YipeeObjectGDX o) {
        if (o != null) {
            o.setId(null);
            o.setName(this.getName());
            o.setCreated(TimeUtils.millis());
            o.setModified(TimeUtils.millis());
        }
    }
}
