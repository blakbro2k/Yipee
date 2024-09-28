package asg.games.yipee.objects;

public class YipeeBrokenBlock extends AbstractYipeeObject {
   private int block, row, col;

   //Empty Constructor required for Json.Serializable
   public YipeeBrokenBlock() {
      super();
   }

   public YipeeBrokenBlock(int block, int row, int col) {
      this();
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
      return super.toString() + "{Block: [" + block + "]" + YipeeBlock.printReaderFriendlyBlock(block) + "@, row: " + row + ", col: " + col + "}";
   }
}
