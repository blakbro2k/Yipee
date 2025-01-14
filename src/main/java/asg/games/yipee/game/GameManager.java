package asg.games.yipee.game;

import asg.games.yipee.objects.YipeeGameBoardState;
import asg.games.yipee.tools.LogUtil;
import asg.games.yipee.tools.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class GameManager {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);
    private static final String CONST_TITLE = "Yipee! Game Manager";
    private final ScheduledExecutorService gameLoopExecutor;
    private final ExecutorService playerActionExecutor;
    private final Map<Integer, Queue<YipeeGameBoardState>> gameBoardStateMap; // Keyed by seat ID (1-8)
    private final Queue<PlayerAction> playersActionQueue = new ConcurrentLinkedQueue<>();
    private final Map<Integer, YipeeGameBoard> gameBoardMap = new ConcurrentHashMap<>();

    public GameManager() {
        LogUtil.info("{} Build {}", CONST_TITLE, Version.printVersion());
        LogUtil.info("Initializing Gamestates...");
        gameBoardStateMap = new ConcurrentHashMap<>(); // Thread-safe map for 8 game boards
        LogUtil.info("Initializing Game loop...");
        gameLoopExecutor = Executors.newScheduledThreadPool(1); // Single thread for game loop
        LogUtil.info("Initializing Actions...");
        playerActionExecutor = Executors.newFixedThreadPool(10); // Thread pool for player actions

        LogUtil.info("Initializing Seats...");
        // Initialize 8 game boards (1 for each seat)
        for (int seatId = 1; seatId <= 8; seatId++) {
            LogUtil.trace("Initializing seat[{}]", seatId);
            gameBoardMap.put(seatId, new YipeeGameBoard());
            gameBoardStateMap.put(seatId, new LinkedList<>());
        }
    }

    public void startGameLoop() {
        // Set same seeded game for 8 game boards (1 for each seat)
        long seed = TimeUtils.millis();
        LogUtil.info("Starting game with seed={}", seed);
        for (int seatId = 1; seatId <= 8; seatId++) {
            YipeeGameBoard board = gameBoardMap.get(seatId);
            if (board != null) {
                board.reset(seed);
                board.begin();
                addState(seatId, board.getGameState());
            }
        }
    }

    private void addState(int seatId, YipeeGameBoardState gameState) {
        if (seatId < 0) {
            LogUtil.warn("Invalid value for seat[{}], skipping adding to stack.");
            return;
        }
        if (gameState != null) {
            Queue<YipeeGameBoardState> states = gameBoardStateMap.get(seatId);
            if (!states.add(gameState)) {
                LogUtil.warn("There was an exception adding state for seat[{}]", seatId);
            }
        } else {
            LogUtil.warn("GameState for seat[{}], skipping adding to stack.");
        }
    }

    public void gameLoopTick(float delta) {
        // Process Player Actions
        PlayerAction action;
        while ((action = playersActionQueue.poll()) != null) {
            processPlayerAction(action, delta);
        }
        // 3. Check Win/Loss Conditions
        LogUtil.debug("Checking Game End conditions");
        checkGameEndConditions();

        // 4. Prepare Outgoing State Updates
        LogUtil.debug("Broadcasting GameState");
        broadcastGameState();
    }

    public void processPlayerAction(PlayerAction action, float delta) {
        int targetSeatId = action.getTargetBoardId();
        YipeeGameBoard board = gameBoardMap.get(targetSeatId);

        if (board == null) {
            LogUtil.warn("No game board found for seat [{}]. Skipping action [{}].", targetSeatId, action.getActionType());
            return;
        }

        LogUtil.debug("Processing action [{}] for seat [{}]", action.getActionType(), targetSeatId);

        playerActionExecutor.submit(() -> {
            synchronized (board) {
                board.update(delta);
                board.applyPlayerAction(action);
                addState(targetSeatId, board.getGameState());
            }
        });
    }

    public void stop() {
        LogUtil.info("Attempting to shutdown GameServer...");

        gameLoopExecutor.shutdown();
        playerActionExecutor.shutdown();
        try {
            boolean gameLoopTerminator = gameLoopExecutor.awaitTermination(5, TimeUnit.SECONDS);
            boolean gameActionExe = playerActionExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LogUtil.error("Error shutting down executors", e);
        }
    }

    protected void addPlayerAction(PlayerAction action) {
        playersActionQueue.offer(action);
    }

    public Queue<YipeeGameBoardState> getGameBoardStates(int seatId) {
        if (seatId < 0 || seatId > gameBoardStateMap.size()) {
            LogUtil.error("GameState seatId is out of bounds: {}", seatId);
            throw new IllegalArgumentException("GameState seatId is out of bounds: " + seatId);
        }
        return gameBoardStateMap.get(seatId); // Access a specific game board
    }

    public YipeeGameBoardState getLatestGameBoardState(int seatId) {
        Queue<YipeeGameBoardState> states = getGameBoardStates(seatId);
        return states.peek(); // Access a specific game board
    }

    protected abstract void broadcastGameState();

    //{
    //for (YipeeGameBoard gameBoard : gameBoards) {
    // Serialize the state and send it to the respective client
    //String serializedState = gameBoard.serializeState();
    //sendToClient(gameBoard.getPlayerId(), serializedState);
    // }
    // }
    protected abstract void checkGameEndConditions();
    //{
    /* if (/* win condition *///) {
    //    endGame("Winner is X");
    //} else if (/* loss condition */) {
          /*  endGame("Draw or Loss");
        }*/
    //}
}