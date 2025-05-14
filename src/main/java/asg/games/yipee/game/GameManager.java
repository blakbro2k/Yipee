/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.game;

import asg.games.yipee.objects.YipeeGameBoardState;
import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.tools.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The GameManager class serves as the backbone for managing a Yipee game session.
 * It handles the game loop, player actions, and the state of each game board for up to 8 players.
 * <p>
 * Key Responsibilities:
 * - Initializes game boards and players for each seat.
 * - Manages a fixed-timestep game loop and player actions.
 * - Synchronizes game states and broadcasts updates to clients.
 * - Provides hooks for game-specific logic like state broadcasting and win/loss conditions.
 * <p>
 * Thread Safety:
 * - Uses thread-safe data structures such as ConcurrentHashMap and ConcurrentLinkedQueue.
 * - Employs executors for managing game loop and player action processing.
 */
public class GameManager {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);
    private static final String CONST_TITLE = "Yipee! Game Manager";
    private final ScheduledExecutorService gameLoopExecutor; // Manages the game loop
    private final ExecutorService playerActionExecutor; // Handles player action processing
    private final Queue<PlayerAction> playersActionQueue = new ConcurrentLinkedQueue<>(); // Stores pending player actions
    private final Map<Integer, GamePlayerBoard> gameBoardMap = new ConcurrentHashMap<>(); // Maps seat IDs to game boards

    @Getter
    @Setter
    static class GamePlayerBoard {
        private YipeePlayer player = new YipeePlayer();
        private YipeeGameBoard board = new YipeeGameBoard(-1);
        private final Queue<YipeeGameBoardState> gameBoardStates = new ConcurrentLinkedQueue<>(); // Tracks states by seat ID

        public boolean addState(YipeeGameBoardState state) {
            return gameBoardStates.add(state);
        }

        public boolean removeState(YipeeGameBoardState state) {
            return gameBoardStates.remove(state);
        }

        public void reset() {
            board.reset(-1);
            gameBoardStates.clear();
        }

        public void setBoardSeed(long seed) {
            board.reset(seed);
        }

        public void startBoard() {
            board.begin();
        }

        public YipeeGameBoardState getLatestGameState() {
            return gameBoardStates.peek();
        }
    }

    /**
     * Constructor initializes game boards, executors, and logging for game session setup.
     */
    public GameManager() {
        logger.info("{} Build {}", CONST_TITLE, Version.printVersion());
        logger.info("Initializing Gamestates...");
        logger.info("Initializing Game loop...");
        gameLoopExecutor = Executors.newScheduledThreadPool(1); // Single thread for game loop
        logger.info("Initializing Actions...");
        playerActionExecutor = Executors.newFixedThreadPool(10); // Thread pool for player actions

        logger.info("Initializing Seats...");
        // Initialize 8 game boards (1 for each seat)
        for (int seatId = 0; seatId < 8; seatId++) {
            logger.trace("Initializing seat[{}]", seatId);
            gameBoardMap.put(seatId, new GamePlayerBoard());
        }
    }

    /**
     * Starts the game loop and initializes the game boards with a common seed.
     */
    public void startGameLoop() {
        resetGameBoards();

        // Set same seeded game for 8 game boards (1 for each seat)
        long seed = TimeUtils.millis();
        logger.info("Starting game with seed={}", seed);
        for (int seatId = 0; seatId < 8; seatId++) {
            GamePlayerBoard board = gameBoardMap.get(seatId);
            if (!isPlayerEmpty(seatId)) {
                board.setBoardSeed(seed);
                board.startBoard();
                addState(seatId, board.getLatestGameState());
            }
        }
    }

    /**
     * Checks if the board has a player set.  This means a player has sat down.
     *
     * @param seatId the seat ID
     * @return true if {@link YipeePlayer} player is not null
     */
    private boolean isPlayerEmpty(int seatId) {
        validateSeat(seatId);
        return getGameBoardPlayer(seatId) == null;
    }

    /**
     * Resets the board.
     *
     * @param seatId the seat ID
     */
    public void resetGameBoard(int seatId) {
        validateSeat(seatId);
    }

    /**
     * Validates that the seat ID is within acceptable bounds (0-7).
     * Adjust this if using 1-based indexing for seats externally.
     *
     * @param seatId the seat ID to validate
     * @throws IllegalArgumentException if the seat ID is out of bounds
     */
    private void validateSeat(int seatId) {
        if (seatId < 0 || seatId > 7) {
            logger.error("Seat ID [{}] is out of bounds. Valid range is 0-7.", seatId);
            throw new IllegalArgumentException("Seat ID must be between 0 and 7.");
        }
    }

    /**
     * Retrieves the {@link YipeeGameBoard} game board associated with a specific seat ID.
     *
     * @param seatId the ID of the seat (1-8)
     * @return the {@link YipeeGameBoard} instance or null if none exists
     */
    public YipeeGameBoard getGameBoard(int seatId) {
        validateSeat(seatId);
        GamePlayerBoard gameBoardObj = gameBoardMap.get(seatId);
        YipeeGameBoard board = null;
        if (gameBoardObj != null) {
            board = gameBoardObj.getBoard();
        }
        return board;
    }

    /**
     * Retrieves a {@link YipeePlayer} player associated with a specific seat ID.
     *
     * @param seatId the ID of the seat (1-8)
     * @return the {@link YipeePlayer} player or null if none exists
     */
    public YipeePlayer getGameBoardPlayer(int seatId) {
        validateSeat(seatId);
        GamePlayerBoard gameBoardObj = gameBoardMap.get(seatId);
        YipeePlayer player = null;
        if (gameBoardObj != null) {
            player = gameBoardObj.getPlayer();
        }
        return player;
    }

    /**
     * Retrieves the {@link YipeeGameBoardState} Game States associated with a specific seat ID.
     *
     * @param seatId the ID of the seat (1-8)
     * @return the YipeeGameBoard instance or null if none exists
     */
    public Queue<YipeeGameBoardState> getGameBoardStates(int seatId) {
        validateSeat(seatId);
        GamePlayerBoard gameBoardObj = gameBoardMap.get(seatId);
        Queue<YipeeGameBoardState> states = null;
        if (gameBoardObj != null) {
            states = gameBoardObj.getGameBoardStates();
        }
        return states;
    }

    /**
     * Sets a {@link YipeePlayer} player in the given seatId to associate with the {@link YipeeGameBoard} GameBoard.
     *
     * @param seatId
     * @param player
     */
    public void setGameBoardObjectPlayer(int seatId, YipeePlayer player) {
        validateSeat(seatId);
        GamePlayerBoard gameBoardObj = gameBoardMap.get(seatId);
        if (gameBoardObj != null) {
            gameBoardObj.setPlayer(player);
        }
    }

    /**
     * @param seatId
     * @param gameState
     */
    public void addState(int seatId, YipeeGameBoardState gameState) {
        if (seatId < 0) {
            logger.warn("Invalid value for seat[{}], skipping adding to stack.");
            return;
        }
        if (gameState != null) {
            GamePlayerBoard gamePlayerBoard = gameBoardMap.get(seatId);
            if (!gamePlayerBoard.addState(gameState)) {
                logger.warn("There was an exception adding state for seat[{}]", seatId);
            }
        } else {
            logger.warn("GameState for seat[{}], skipping adding to stack.");
        }
    }

    /**
     * Resets all game boards and clears associated states.
     */
    public void resetGameBoards() {
        for (int seatId = 0; seatId < 8; seatId++) {
            gameBoardMap.get(seatId).reset();
        }
    }

    /**
     * Processes player actions in the queue and updates game boards.
     *
     * @param delta the time step for the game loop
     */
    public void gameLoopTick(float delta) {
        // Process Player Actions
        PlayerAction action;
        while ((action = playersActionQueue.poll()) != null) {
            processPlayerAction(action, delta);
        }
        // 3. Check Win/Loss Conditions
        logger.debug("Checking Game End conditions");
        //checkGameEndConditions();

        // 4. Prepare Outgoing State Updates
        logger.debug("Broadcasting GameState");
        //broadcastGameState();
    }

    /**
     * Processes a single player action and updates the target game board.
     *
     * @param action the player action to process
     * @param delta  the time step for the game loop
     */
    public void processPlayerAction(PlayerAction action, float delta) {
        int targetSeatId = action.getTargetBoardId();
        YipeeGameBoard board = getGameBoard(targetSeatId);
        logger.info("Initial boardSeat: {} is taking action: {} on target boardSeat: {}.", action.getInitiatingBoardId(), action.getActionType(), action.getTargetBoardId());

        if (board == null) {
            logger.warn("No game board found for seat [{}]. Skipping action [{}].", targetSeatId, action.getActionType());
            return;
        }
        logger.debug("Processing action [{}] for seat [{}]", action.getActionType(), targetSeatId);

        playerActionExecutor.submit(() -> {
            synchronized (board) {
                board.update(delta);
                board.applyPlayerAction(action);
                addState(targetSeatId, board.exportGameState());
            }
        });
    }

    /**
     * Stops the game server by shutting down executors and cleaning up resources.
     */
    public void stop() {
        logger.info("Attempting to shutdown GameServer...");

        gameLoopExecutor.shutdown();
        playerActionExecutor.shutdown();
        try {
            boolean gameLoopTerminator = gameLoopExecutor.awaitTermination(5, TimeUnit.SECONDS);
            boolean gameActionExe = playerActionExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Error shutting down executors", e);
        }
    }

    /**
     *
     * @param action
     */
    public void addPlayerAction(PlayerAction action) {
        playersActionQueue.offer(action);
    }

    /**
     *
     * @param seatId
     * @return YipeeGameBoardState
     */
    public YipeeGameBoardState getLatestGameBoardState(int seatId) {
        Queue<YipeeGameBoardState> states = getGameBoardStates(seatId);
        return states.peek(); // Access a specific game board
    }
}
