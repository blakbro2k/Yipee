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
package asg.games.yipee.core.persistence;

import asg.games.yipee.core.objects.YipeePlayer;
import asg.games.yipee.core.objects.YipeeRoom;
import asg.games.yipee.core.objects.YipeeSeat;
import asg.games.yipee.core.objects.YipeeTable;

import java.util.List;

/**
 * Interface for persisting and retrieving {@link YipeeRoom}, {@link YipeeTable}, {@link YipeeSeat}, and {@link YipeePlayer}
 * objects in a structured game environment.
 * <p>
 * This abstraction allows flexible storage backends such as file-based, in-memory, or database-backed
 * implementations, while maintaining consistency in object-level access and management.
 * </p>
 *
 * <p>All methods throw {@link Exception} to allow implementations to handle persistence-related errors.</p>
 *
 * @author Blakbro2k
 */
public interface YipeeStorage {

    // --- Room Operations ---

    /**
     * Persists a single {@link YipeeRoom} into storage.
     *
     * @param room the room to store
     * @throws Exception if storage fails
     */
    void putRoom(YipeeRoom room) throws Exception;

    /**
     * Retrieves a {@link YipeeRoom} by its name or ID.
     *
     * @param nameOrId the room's name or ID
     * @return the retrieved room
     * @throws Exception if retrieval fails
     */
    YipeeRoom getRoom(String nameOrId) throws Exception;

    /**
     * Returns all stored rooms.
     *
     * @return list of all {@link YipeeRoom}s
     * @throws Exception if retrieval fails
     */
    List<YipeeRoom> getAllRooms() throws Exception;

    /**
     * Persists a collection of {@link YipeeRoom}s to storage.
     *
     * @param rooms the rooms to store
     * @throws Exception if storage fails
     */
    void putAllRooms(Iterable<YipeeRoom> rooms) throws Exception;

    // --- Table Operations ---

    /**
     * Persists a single {@link YipeeTable} into storage.
     *
     * @param table the table to store
     * @throws Exception if storage fails
     */
    void putTable(YipeeTable table) throws Exception;

    /**
     * Retrieves a {@link YipeeTable} by its name or ID.
     *
     * @param nameOrId the table's name or ID
     * @return the retrieved table
     * @throws Exception if retrieval fails
     */
    YipeeTable getTable(String nameOrId) throws Exception;

    /**
     * Returns all stored tables.
     *
     * @return list of all {@link YipeeTable}s
     * @throws Exception if retrieval fails
     */
    List<YipeeTable> getAllTables() throws Exception;

    /**
     * Persists a collection of {@link YipeeTable}s to storage.
     *
     * @param tables the tables to store
     * @throws Exception if storage fails
     */
    void putAllTables(Iterable<YipeeTable> tables) throws Exception;

    // --- Seat Operations ---

    /**
     * Persists a single {@link YipeeSeat} into storage.
     *
     * @param seat the seat to store
     * @throws Exception if storage fails
     */
    void putSeat(YipeeSeat seat) throws Exception;

    /**
     * Retrieves a {@link YipeeSeat} by its name or ID.
     *
     * @param nameOrId the seat's name or ID
     * @return the retrieved seat
     * @throws Exception if retrieval fails
     */
    YipeeSeat getSeat(String nameOrId) throws Exception;

    /**
     * Returns all stored seats.
     *
     * @return list of all {@link YipeeSeat}s
     * @throws Exception if retrieval fails
     */
    List<YipeeSeat> getAllSeats() throws Exception;

    /**
     * Persists a collection of {@link YipeeSeat}s to storage.
     *
     * @param seats the seats to store
     * @throws Exception if storage fails
     */
    void putAllSeats(Iterable<YipeeSeat> seats) throws Exception;

    // --- Player Operations ---

    /**
     * Persists a single {@link YipeePlayer} into storage.
     *
     * @param player the player to store
     * @throws Exception if storage fails
     */
    void putPlayer(YipeePlayer player) throws Exception;

    /**
     * Retrieves a {@link YipeePlayer} by its name or ID.
     *
     * @param nameOrId the player's name or ID
     * @return the retrieved player
     * @throws Exception if retrieval fails
     */
    YipeePlayer getPlayer(String nameOrId) throws Exception;

    /**
     * Returns all stored players.
     *
     * @return list of all {@link YipeePlayer}s
     * @throws Exception if retrieval fails
     */
    List<YipeePlayer> getAllPlayers() throws Exception;

    /**
     * Persists a collection of {@link YipeePlayer}s to storage.
     *
     * @param players the players to store
     * @throws Exception if storage fails
     */
    void putAllPlayers(Iterable<YipeePlayer> players) throws Exception;

    // --- Watcher Operations ---

    /**
     * Returns all stored watcher players (non-seated spectators).
     *
     * @return list of all watcher {@link YipeePlayer}s
     * @throws Exception if retrieval fails
     */
    List<YipeePlayer> getAllWatchers() throws Exception;

    /**
     * Persists a collection of watcher {@link YipeePlayer}s to storage.
     *
     * @param players the watcher players to store
     * @throws Exception if storage fails
     */
    void putAllWatchers(Iterable<YipeePlayer> players) throws Exception;
}
