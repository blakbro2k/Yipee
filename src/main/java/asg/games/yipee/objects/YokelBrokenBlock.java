package asg.games.yipee.objects;

public class YokelBrokenBlock extends AbstractYokelObject {
   private int block, row, col;

   //Empty Constructor required for Json.Serializable
   public YokelBrokenBlock() {
   }

   public YokelBrokenBlock(int block, int row, int col) {
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
   public String toString() {
      return super.toString() + "{Block: [" + block + "]" + YokelBlock.printReaderFriendlyBlock(block) + "@, row: " + row + ", col: " + col + "}";
   }
}
