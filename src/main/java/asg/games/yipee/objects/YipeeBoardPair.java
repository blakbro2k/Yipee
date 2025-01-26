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
package asg.games.yipee.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@JsonIgnoreProperties({"brokenCells", "tableStartReady", "upArguments", "tableName"})
public class YipeeBoardPair extends AbstractYipeeObject {
    YipeeGameBoardState leftBoard;
    YipeeGameBoardState rightBoard;

    /**
     * Default constructor required for JSON serialization.
     * Initializes the block in its default state.
     */
    public YipeeBoardPair() {
    }

    public YipeeBoardPair(YipeeGameBoardState left, YipeeGameBoardState right) {
        setLeftBoard(left);
        setRightBoard(right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBoardPair that = (YipeeBoardPair) o;
        return Objects.equals(getLeftBoard(), that.getLeftBoard()) && Objects.equals(getRightBoard(), that.getRightBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLeftBoard(), getRightBoard());
    }
}
