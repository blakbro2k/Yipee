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
package asg.games.yipee.libgdx.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a visual pairing of two {@link YipeeGameBoardStateGDX} instances—typically
 * a player and their partner or opponent—in a client-side LibGDX context.
 *
 * <p>This class is used primarily for grouping board states for rendering and
 * synchronizing client UI logic. It extends {@link AbstractYipeeObjectGDX} and is
 * serializable for JSON communication between systems.</p>
 *
 * <p>Both boards are nullable and may be updated dynamically as part of the game flow.</p>
 *
 * <p>Used in screens such as the main game screen or preview boards in spectator mode.</p>
 *
 * @author See AUTHORS
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeBoardPairGDX extends AbstractYipeeObjectGDX {
    /**
     * The left-side game board (often the local player or their preview).
     */
    YipeeGameBoardStateGDX leftBoard;

    /**
     * The right-side game board (often the partner or opponent of the leftBoard).
     */
    YipeeGameBoardStateGDX rightBoard;

    /**
     * Default no-arg constructor required for JSON serialization and framework instantiation.
     * Initializes the board pair with null boards.
     */
    public YipeeBoardPairGDX() {
    }

    /**
     * Constructs a {@code YipeeBoardPairGDX} with the given left and right game boards.
     *
     * @param left  the left game board, typically the player's board
     * @param right the right game board, typically the partner or enemy board
     */
    public YipeeBoardPairGDX(YipeeGameBoardStateGDX left, YipeeGameBoardStateGDX right) {
        setLeftBoard(left);
        setRightBoard(right);
    }
}
