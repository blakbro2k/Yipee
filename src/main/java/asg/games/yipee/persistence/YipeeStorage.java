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
package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.objects.YipeeRoom;
import asg.games.yipee.objects.YipeeSeat;
import asg.games.yipee.objects.YipeeTable;

import java.util.List;

/** Interface for Yokel Object Resource Storage.
 * @author blakbro2k */
public interface YipeeStorage {

    /** Puts a Room into storage. */
    void putRoom(YipeeRoom room) throws Exception;

    /** Gets a Room from the storage */
    YipeeRoom getRoom(String nameOrId) throws Exception;

    /** Get all Rooms from store. */
    List<YipeeRoom> getAllRooms() throws Exception;

    /** Save all Rooms from store. */
    void putAllRooms(Iterable<YipeeRoom> rooms) throws Exception;

    /** Puts a lounge into storage. */
    void putTable(YipeeTable table) throws Exception;

    /** Gets a Room from the storage. */
    YipeeTable getTable(String nameOrId) throws Exception;

    /** Get all Tables from store. */
    List<YipeeTable> getAllTables() throws Exception;

    /** Save all Tables from store. */
    void putAllTables(Iterable<YipeeTable> tables) throws Exception;

    /** Puts a lounge into storage. */
    void putSeat(YipeeSeat seat) throws Exception;

    /** Releases all resources of this object. */
    YipeeSeat getSeat(String nameOrId) throws Exception;

    /** Get all Rooms from store. */
    List<YipeeSeat> getAllSeats() throws Exception;

    /** Save all Rooms from store. */
    void putAllSeats(Iterable<YipeeSeat> seats) throws Exception;

    /** Puts a lounge into storage. */
    void putPlayer(YipeePlayer player) throws Exception;

    /**
     * Gets a player from the storage
     */
    YipeePlayer getPlayer(String nameOrId) throws Exception;

    /**
     * Get all Players from store.
     */
    List<YipeePlayer> getAllPlayers() throws Exception;

    /**
     * Save all Players from store.
     */
    void putAllPlayers(Iterable<YipeePlayer> players) throws Exception;

    /**
     * Get all Players from store.
     */
    List<YipeePlayer> getAllWatchers() throws Exception;

    /**
     * Save all Players from store.
     */
    void putAllWatchers(Iterable<YipeePlayer> players) throws Exception;

}