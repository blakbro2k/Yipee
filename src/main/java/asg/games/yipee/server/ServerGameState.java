package asg.games.yipee.server;

import asg.games.yipee.objects.YipeeGameBoardState;

public class ServerGameState {
   YipeeGameBoardState[] gamePositions = new YipeeGameBoardState[8];

   public ServerGameState() {
   }

   public void setGameState(int position, YipeeGameBoardState state) {
      if (position < 0 || position > 8)
         throw new IndexOutOfBoundsException("Position is out of bounds 0 > x > 8.");
      gamePositions[position] = state;
   }

   public YipeeGameBoardState getGameState(int position) {
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
