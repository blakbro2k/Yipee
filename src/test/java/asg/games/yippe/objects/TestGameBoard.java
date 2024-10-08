package asg.games.yippe.objects;

import asg.games.yipee.objects.YipeeGameBoard;
import asg.games.yipee.objects.YipeeGameBoardState;
import asg.games.yipee.server.ServerGameState;
import asg.games.yipee.tools.TimeUtils;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;


public class TestGameBoard {

    @Test()
    public void testGameBoard() {
        System.out.println("Start Test Game Board.");
        ServerGameState serverGameState = new ServerGameState();

        int gameSeed = -1;
        int maxTicks = 129;
        YipeeGameBoard playerB = new YipeeGameBoard(gameSeed);
        playerB.setDebug(true);
        YipeeGameBoard partnerB = new YipeeGameBoard(gameSeed);
        partnerB.setDebug(false);
        boolean isPartnerRight = true;
        playerB.begin();
        playerB.startMoveDown();
        //partnerB.begin();
        int playerBIndex = 0;
        int partnerBIndex = 1;
        serverGameState.setGameState(playerBIndex, playerB.getGameState());
        serverGameState.setGameState(partnerBIndex, partnerB.getGameState());
        List<YipeeGameBoardState> states = new LinkedList<>();

        long gameStartTime = TimeUtils.millis();
        long serverTickTime;
        long prevServerTickTime = TimeUtils.millis();

        for (int tick = 0; tick < maxTicks; tick++) {
            if (tick == 1) {
                playerB.cycleDown();
            }
            if (tick == 1) {
                //playerB.testVerticalYahoo();
            }
            if (tick == 71) {
                playerB.cycleDown();
            }
            if (tick == 52) {
                //playerB.movePieceLeft();
            }
            if (tick == 125) {
                //playerB.movePieceRight();
            }
            serverTickTime = TimeUtils.millis();
            float delta = serverTickTime - prevServerTickTime;
            prevServerTickTime = serverTickTime;

            System.out.println("#---------- Start iteration tick: " + tick + "  --------------------#");
            playerB.setPartnerBoard(partnerB, isPartnerRight);
            partnerB.setPartnerBoard(playerB, !isPartnerRight);
            playerB.update(delta);
            partnerB.update(delta);

            YipeeGameBoardState gameState = playerB.getGameState();
            gameState.setServerGameStartTime(gameStartTime);
            gameState.setCurrentStateTimeStamp(TimeUtils.millis());
            states.add(gameState);
            //System.out.println(playerB);
            System.out.println("#----------  Game State -------------------#");
            System.out.println(gameState);
            System.out.println("#---------- end iteration tick: " + tick + "  --------------------#");
        }
        playerB.end();
        partnerB.end();
        System.out.println("Total States: " + states.size());
        System.out.println("End Test Game Board");
        System.out.println("\n\n\n" + states.get(2));
    }
}