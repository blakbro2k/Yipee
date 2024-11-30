package asg.games.yipee.objects;

import java.util.Objects;

public class YipeeBrokenBlock extends AbstractYipeeObject {
   private int block, row, col;

   //Empty Constructor required for Json.Serializable
   public YipeeBrokenBlock() {
   }

   public YipeeBrokenBlock(int block, int row, int col) {
      this.block = block;
      this.row = row;
      this.col = col;
   }

   public int getBlock() {
      return block;
   }

   public void setBlock(int block) {
      this.block = block;
   }

   public int getRow() {
      return row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getCol() {
      return col;
   }

   public void setCol(int col) {
      this.col = col;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      if (!super.equals(o)) return false;
      YipeeBrokenBlock that = (YipeeBrokenBlock) o;
      return block == that.block && row == that.row && col == that.col;
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), block, row, col);
   }

   @Override
   public String toString() {
      return super.toString() + "{Block: [" + block + "]" + YipeeBlock.printReaderFriendlyBlock(block) + "@, row: " + row + ", col: " + col + "}";
   }
}
