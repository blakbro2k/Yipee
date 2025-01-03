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
package asg.games.yipee.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties({"brokenCells", "tableStartReady", "upArguments", "tableName"})
public class YipeeBoardPair extends AbstractYipeeObject {
    YipeeGameBoard leftBoard;
    YipeeGameBoard rightBoard;

    //Empty Contractor required for Json.Serializable
    public YipeeBoardPair() {}

    public YipeeBoardPair(YipeeGameBoard left, YipeeGameBoard right) {
        setLeftBoard(left);
        setRightBoard(right);
    }

    public void setLeftBoard(YipeeGameBoard leftBoard) {
        this.leftBoard = leftBoard;
    }

    public void setRightBoard(YipeeGameBoard rightBoard){
        this.rightBoard = rightBoard;
    }

    public YipeeGameBoard getLeftBoard() {
        return leftBoard;
    }

    public YipeeGameBoard getRightBoard() {
        return rightBoard;
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
