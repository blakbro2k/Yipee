package asg.games.yipee.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Objects;

public class YipeePiece extends AbstractYipeeObject {
    @JsonIgnore
    public static final int MEDUSA_GAME_PIECE = 1;
    @JsonIgnore
    public static final int MIDAS_GAME_PIECE = 2;

    private final int[] cells = new int[3];
    private int index;
    public int row;
    public int column;

    //Empty Constructor required for Json.Serializable
    public YipeePiece() {}

    public YipeePiece(int index, int block1, int block2, int block3) {
        setIndex(index);
        setBlock1(block1);
        setBlock2(block2);
        setBlock3(block3);
    }

    public int[] getCells(){
        return cells;
    }

    public int getBlock1(){
        return cells[2];
    }

    public void setBlock1(int block){
        cells[2] = block;
    }

    public int getBlock2(){
        return cells[1];
    }

    public void setBlock2(int block){
        cells[1] = block;
    }

    public int getBlock3(){
        return cells[0];
    }

    public void setBlock3(int block){
        cells[0] = block;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void setPosition(int r, int c) {
        if(r < 0) throw new RuntimeException("Row value cannot be less than zero!");
        if(c < 0) throw new RuntimeException("Column value cannot be less than zero!");
        this.row = r;
        this.column = c;
    }

    public void cycleDown() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock3);
        setBlock2(tempBlock1);
        setBlock3(tempBlock2);
    }

    public void cycleUp() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock2);
        setBlock2(tempBlock3);
        setBlock3(tempBlock1);
    }

    public int getIntValueFromFloat(Object o) {
        if (o instanceof Float) {
            return Math.round((Float) o);
        }
        return -1;
    }

    public int getValueAt(int i) {
        return cells[i];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeePiece that = (YipeePiece) o;
        return getIndex() == that.getIndex() && row == that.row && column == that.column && Arrays.equals(getCells(), that.getCells());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), getIndex(), row, column);
        result = 31 * result + Arrays.hashCode(getCells());
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + Arrays.toString(cells);
    }
}