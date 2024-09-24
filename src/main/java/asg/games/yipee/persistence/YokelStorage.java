package asg.games.yipee.persistence;

import com.badlogic.gdx.utils.ObjectMap;

import java.util.List;

import asg.games.yokel.managers.GameManager;
import asg.games.yokel.objects.YokelPlayer;
import asg.games.yokel.objects.YokelRoom;
import asg.games.yokel.objects.YokelSeat;
import asg.games.yokel.objects.YokelTable;

/** Interface for Yokel Object Resource Storage.
 * @author blakbro2k */
public interface YokelStorage {

    /** Puts a Room into storage. */
    void putRoom(YokelRoom room) throws Exception;

    /** Gets a Room from the storage */
    YokelRoom getRoom(String nameOrId) throws Exception;

    /** Get all Rooms from store. */
    List<YokelRoom> getAllRooms() throws Exception;

    /** Save all Rooms from store. */
    void putAllRooms(Iterable<YokelRoom> rooms) throws Exception;

    /** Puts a lounge into storage. */
    void putTable(YokelTable table) throws Exception;

    /** Gets a Room from the storage. */
    YokelTable getTable(String nameOrId) throws Exception;

    /** Get all Tables from store. */
    List<YokelTable> getAllTables() throws Exception;

    /** Save all Tables from store. */
    void putAllTables(Iterable<YokelTable> tables) throws Exception;

    /** Puts a lounge into storage. */
    void putSeat(YokelSeat seat) throws Exception;

    /** Releases all resources of this object. */
    YokelSeat getSeat(String nameOrId) throws Exception;

    /** Get all Rooms from store. */
    List<YokelSeat> getAllSeats() throws Exception;

    /** Save all Rooms from store. */
    void putAllSeats(Iterable<YokelSeat> seats) throws Exception;

    /** Puts a lounge into storage. */
    void putPlayer(YokelPlayer player) throws Exception;

    /** Gets a player from the storage */
    YokelPlayer getPlayer(String nameOrId) throws Exception;

    /** Get all Players from store. */
    List<YokelPlayer> getAllPlayers() throws Exception;

    /** Save all Players from store. */
    void putAllPlayers(Iterable<YokelPlayer> players) throws Exception;

    /** Releases all resources of this object. */
    void putGame(String id, GameManager game) throws Exception;

    /** Releases all resources of this object. */
    GameManager getGame(String gameId) throws Exception;

    /** Get all active GameManager objects from the store. */
    ObjectMap.Values<GameManager> getAllGames() throws Exception;

    /** Save all given GameManger objects into store. */
    void putAllGames(Iterable<GameManager> games) throws Exception;

}