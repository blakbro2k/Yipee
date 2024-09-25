package asg.games.yipee.server;

import asg.games.yipee.objects.YokelGameBoardState;

public class ServerGameState {
   YokelGameBoardState[] gamePositions = new YokelGameBoardState[8];

   public ServerGameState() {
   }

   public void setGameState(int position, YokelGameBoardState state) {
      if (position < 0 || position > 8)
         throw new IndexOutOfBoundsException("Position is out of bounds 0 > x > 8.");
      gamePositions[position] = state;
   }

   public YokelGameBoardState getGameState(int position) {
      if (position < 0 || position > 8)
         throw new IndexOutOfBoundsException("Position is out of bounds 0 > x > 8.");
      return gamePositions[position];
   }
/*
   @Override
   public void write(Json json) {
   }

   @Override
   public void read(Json json, JsonValue jsonData) {

   }*/
}
