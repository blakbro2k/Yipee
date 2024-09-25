package asg.games.yipee.persistence.examples;

import asg.games.yipee.game.GameManager;
import asg.games.yipee.objects.YokelObject;
import asg.games.yipee.objects.YokelPlayer;
import asg.games.yipee.objects.YokelRoom;
import asg.games.yipee.objects.YokelSeat;
import asg.games.yipee.objects.YokelTable;
import asg.games.yipee.persistence.YokelStorageAdapter;
import asg.games.yipee.tools.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class YokelMemoryStorage extends MemoryStorage implements YokelStorageAdapter {
    //<"lounge name", room object>
    private Map<String, String> clients;
    //<"player id", player object>
    private Map<String, YokelPlayer> registeredPlayers;
    //<"table name", gameManager>
    private Map<String, GameManager> games;
    //<"player id", room object>
    private Map<String, List<String>> room_idx;
    //<"player id", room object>
    private Map<String, List<String>> table_idx;
    //<"player id", room object>
    private Map<String, List<String>> seat_idx;


    public YokelMemoryStorage(){
        super();
        room_idx = new HashMap<>();
        table_idx = new HashMap<>();
        seat_idx = new HashMap<>();
        registeredPlayers = new HashMap<>();
        games = new HashMap<>();
        clients = new HashMap<>();
    }

    @Override
    public void registerPlayer(String clientID, YokelPlayer player) throws Exception {
        if(player == null) throw new Exception("Player was null.");
        if(clientID == null) throw new Exception("clientID was null.");
        String playerId = player.getId();
        registeredPlayers.put(playerId, player);
        clients.put(clientID, playerId);
    }

    @Override
    public void unRegisterPlayer(String clientID) throws Exception {
        String playerId = getPlayerIdFromClient(clientID);

        //Remove user from all Seats
        //removeFromAllSeats(playerId);

        //Remove user from all tables
        //Remove user from all Rooms
        if(playerId != null){
            registeredPlayers.remove(playerId);
        }
        clients.remove(clientID);
    }


    @Override
    public Collection<YokelPlayer> getAllRegisteredPlayers() {
        return Util.getMapValues(registeredPlayers);
    }

    private String getPlayerIdFromClient(String clientId){
        return clients.get(clientId);
    }

    @Override
    public YokelPlayer getRegisteredPlayerGivenId(String id) {
        return registeredPlayers.get(id);
    }

    @Override
    public YokelPlayer getRegisteredPlayer(YokelPlayer player) {
        if(player != null){
            return getRegisteredPlayerGivenId(player.getId());
        }
        return null;
    }

    @Override
    public boolean isClientRegistered(String clientId) {
        return clients.containsKey(clientId);
    }

    @Override
    public boolean isPlayerRegistered(String playerId) {
        return registeredPlayers.containsKey(playerId);
    }

    @Override
    public void putRoom(YokelRoom room) throws Exception {
        if(room == null) throw new Exception("Room was null");
        save(room);
    }

    @Override
    public YokelRoom getRoom(String nameOrId) {
        return get(YokelRoom.class, nameOrId);
    }

    @Override
    public void putTable(YokelTable table) throws Exception {
        save(table);
    }

    @Override
    public YokelTable getTable(String nameOrId) {
        return get(YokelTable.class, nameOrId);
    }

    @Override
    public List<YokelTable> getAllTables() {
        return null;
    }

    @Override
    public void putAllTables(Iterable<YokelTable> tables) {

    }

    @Override
    public void putSeat(YokelSeat seat) throws Exception {
        setSeatIdx(seat);
    }

    @Override
    public YokelSeat getSeat(String nameOrId) {
        return get(YokelSeat.class, nameOrId);
    }

    @Override
    public List<YokelSeat> getAllSeats() {
        return null;
    }

    @Override
    public void putAllSeats(Iterable<YokelSeat> seats) {

    }

    @Override
    public void putPlayer(YokelPlayer player) throws Exception {
        save(player);
    }

    @Override
    public YokelPlayer getPlayer(String nameOrId) {
        return get(YokelPlayer.class, nameOrId);
    }

    @Override
    public List<YokelPlayer> getAllPlayers() {
        return null;
    }

    @Override
    public void putAllPlayers(Iterable<YokelPlayer> players) {

    }


    @Override
    public void putGame(String id, GameManager game) {
        throw new RuntimeException("addGame not implemented.");
    }

    @Override
    public GameManager getGame(String gameId) {
        return games.get(gameId);
    }

    @Override
    public Collection<Object> getAllGames() {
        return Util.getMapValues(games);
    }

    @Override
    public void putAllGames(Iterable<GameManager> games) {

    }

    @Override
    public List<YokelRoom> getAllRooms() {
        return null;
    }

    @Override
    public void putAllRooms(Iterable<YokelRoom> rooms) {

    }


    @Override
    public void dispose() {
        if(room_idx != null){
            room_idx.clear();
            room_idx = null;
        }

        if(table_idx != null){
            table_idx.clear();
            table_idx = null;
        }

        if(seat_idx != null){
            seat_idx.clear();
            seat_idx = null;
        }

        if(registeredPlayers != null){
            registeredPlayers.clear();
            registeredPlayers = null;
        }

        if(games != null){
            games.clear();
            games = null;
        }

        if(clients != null){
            clients.clear();
            clients = null;
        }
    }

    private void save(Object o){
        saveObject(o);
        commitTransactions();
    }

    private <T extends YokelObject> T get(Class<T> klass, String idOrName){
        T object = getObjectById(klass, idOrName);
        if(object == null) object = getObjectByName(klass, idOrName);
        return object;
    }

    private void setSeatIdx(YokelSeat seat) {
        if(seat != null){
            YokelPlayer player = seat.getSeatedPlayer();
            if(player != null){
                setIndex(seat_idx, player.getId(), seat.getId());
            } else {
               // removeSeatedIndex(seat);
            }
            save(seat);
        }
    }

    private void removeSeatedIndex(YokelSeat seat) {
        if(seat != null){

        }
    }

    private void setIndex(Map<String, List<String>> idx, String playerIndex, String objectIndex) {
        List<String> values = idx.get(playerIndex);
        if(values == null){
            values = new ArrayList<>();
        }
        values.add(objectIndex);
        idx.put(playerIndex, values);
    }

    private void removeFromAllSeats(String playerId){
        if(playerId != null){
            List<String> seats = seat_idx.get(playerId);
            for(String seatId : Util.safeIterable(seats)){
                if(seatId != null){
                    YokelSeat seat = get(YokelSeat.class, seatId);
                    if(seat != null){
                        seat.standUp();
                    }
                    saveObject(seat);
                }
            }
            commitTransactions();
        }
    }
}