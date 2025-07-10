package asg.games.yipee.net.game;

import asg.games.yipee.net.packets.YipeeSerializable;

/**
 * Defines the contract for managing a Yipee game session.
 * <p>
 * This interface allows for both server-side authoritative managers
 * and client-side prediction managers to share a common structure.
 */
public interface GameManager {

    /**
     * Initializes the match with a given seed and local seat ID.
     *
     * @param seed        the random seed to synchronize game boards
     * @param localSeatId the seat ID of the local player (can be -1 on server)
     */
    void initialize(long seed, int localSeatId);

    /**
     * Advances the game loop or prediction by a given time step.
     *
     * @param delta time step in seconds
     */
    void update(float delta);

    /**
     * Applies a local player's own input (for prediction).
     *
     * @param action the action input to apply
     */
    void applyLocalPlayerAction(YipeeSerializable action);

    /**
     * Integrates an authoritative server state for any board.
     *
     * @param seatId the seat to update
     * @param state  the authoritative game board state
     */
    void receiveServerState(int seatId, GameBoardState state);

    /**
     * Gets the latest local copy of a single game board state for rendering.
     *
     * @param seatId the seat ID
     * @return the latest game board state
     */
    GameBoardState getBoardState(int seatId);

    /**
     * Gets all stored states for a given seat, for resync or debugging.
     *
     * @param seatId the seat ID
     * @return iterable of stored states
     */
    Iterable<? extends GameBoardState> getBoardStates(int seatId);

    /**
     * Resets all state using the given seed.
     *
     * @param seed the random seed
     */
    void reset(long seed);

    /**
     * Checks if a player's seat board is dead.
     *
     * @param gameSeat the seat ID
     * @return true if the player's board is dead
     */
    boolean isPlayerDead(int gameSeat);

    /**
     * Starts the game loop (server authoritative or local simulation).
     */
    void startGameLoop();

    /**
     * Ends the game loop and stops updates.
     */
    void endGameLoop();

    /**
     * Checks if the entire game session is over (all players dead or win conditions met).
     *
     * @return true if the game is over
     */
    boolean checkGameEndConditions();

    /**
     * Indicates whether the game loop is currently running.
     *
     * @return true if the game loop is active
     */
    boolean isRunning();
}
