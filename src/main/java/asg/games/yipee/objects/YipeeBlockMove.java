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

import java.util.Objects;

public class YipeeBlockMove extends AbstractYipeeObject {
    private int block;
    private int cellId;
    private int col;
    private int row;
    private int targetRow;

    //Empty Constructor required for Json.Serializable
    public YipeeBlockMove() {}

    public YipeeBlockMove(int cellID, int block, int col, int row, int targetRow) {
        setBlock(block);
        setCellID(cellID);
        setCol(col);
        setRow(row);
        this.targetRow = targetRow;
    }

    public void setCellID(int cellId) {
        this.cellId = cellId;
    }

    public int getCellID() {
        return cellId;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getBlock() {
        return block;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getTargetRow() {
        return targetRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBlockMove blockMove = (YipeeBlockMove) o;
        return col == blockMove.getCol() && row == blockMove.getRow() && targetRow == blockMove.getTargetRow() && cellId == blockMove.getCellID() && block == blockMove.getBlock();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), col, row, targetRow, cellId, block);
    }

    @Override
    public String toString() {
        return super.toString() + "{[cellId: " + cellId + "]" + block + "@(col: " + col + ", row: " + row + ") to targetRow: " + targetRow + "}";
    }
}
