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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeBlockMove blockMove = (YipeeBlockMove) o;
        return col == blockMove.getCol() && row == blockMove.getRow() && targetRow == blockMove.getTargetRow();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), col, row, targetRow);
    }

    @Override
    public String toString() {
        return super.toString() + "{" + block + "@(col: " + col + ", row: " + row + ") to targetRow: " + targetRow + "}";
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
}
