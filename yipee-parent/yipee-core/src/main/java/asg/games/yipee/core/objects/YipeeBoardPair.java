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
package asg.games.yipee.core.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents a pair of Yipee game boards — typically a player and their partner.
 *
 * <p>This object is commonly used to render or process a two-board view of the game,
 * often used in client rendering, simulation, or replay scenarios.
 *
 * <p>The left board typically represents the primary player, and the right board represents
 * their partner (or another player in cooperative/competitive context).
 *
 * <p>This class supports JSON serialization and respects specific ignored fields during that process.
 *
 * @author Blakbro2k
 */
@Getter
@Setter
@JsonIgnoreProperties({"brokenCells", "tableStartReady", "upArguments", "tableName"})
public class YipeeBoardPair extends AbstractYipeeObject {
    private static final Logger logger = LoggerFactory.getLogger(YipeeBoardPair.class);

    /**
     * The left-side game board (usually the primary player).
     */
    private YipeeGameBoardState leftBoard;

    /**
     * The right-side game board (typically the partner or secondary board).
     */
    private YipeeGameBoardState rightBoard;

    /**
     * Default constructor required for JSON serialization.
     */
    public YipeeBoardPair() {
    }

    /**
     * Constructs a new pair of game boards.
     *
     * @param left  the left-side board (usually the player's board)
     * @param right the right-side board (partner or opponent)
     */
    public YipeeBoardPair(YipeeGameBoardState left, YipeeGameBoardState right) {
        this.leftBoard = left;
        this.rightBoard = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBoardPair that = (YipeeBoardPair) o;
        return Objects.equals(leftBoard, that.leftBoard) &&
            Objects.equals(rightBoard, that.rightBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), leftBoard, rightBoard);
    }
}
