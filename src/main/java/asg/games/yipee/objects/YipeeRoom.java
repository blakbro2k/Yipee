package asg.games.yipee.objects;

import asg.games.yipee.persistence.YipeeStorageAdapter;
import asg.games.yipee.tools.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YipeeRoom extends AbstractYipeeObject implements YipeeObjectJPAVisitor, Copyable<YipeeRoom>, Disposable {
    public static final String SOCIAL_LOUNGE = "Social";
    public static final String BEGINNER_LOUNGE = "Beginner";
    public static final String INTERMEDIATE_LOUNGE = "Intermediate";
    public static final String ADVANCED_LOUNGE = "Advanced";

    private List<YipeePlayer> players = new ArrayList<>();
    private Map<Integer, YipeeTable> tables = new HashMap<>();

    private String loungeName = "_NoTableName";

    //Empty Constructor required for Json.Serializable
    public YipeeRoom() {
        super();
    }

    public YipeeRoom(String name) {
        this();
        setName(name);
    }

    public YipeeRoom(String name, String loungeName) {
        this();
        setName(name);
        setLoungeName(loungeName);
    }

    public Collection<YipeeTable> getAllTables() {
        return Util.getMapValues(tables);
    }

    public Collection<Integer> getAllTableIndexes() {
        return Util.getMapKeys(tables);
    }

    public void setAllTables(Map<Integer, YipeeTable> tables) {
        this.tables = tables;
    }

    public List<YipeePlayer> getAllPlayers() {
        return players;
    }

    public void setAllPlayers(List<YipeePlayer> players) {
        this.players = players;
    }

    public void joinRoom(YipeePlayer player) {
        if (player != null && !players.contains(player)) {
            players.add(player);
        }
    }

    public void leaveRoom(YipeePlayer player) {
        players.remove(player);
    }

    public YipeeTable addTable() {
        return addTable(null);
    }

    public YipeeTable addTable(Map<String, Object> arguments) {
        YipeeTable table;
        int tableNumber = Util.getNextTableNumber(this);

        if (arguments != null) {
            table = new YipeeTable(getId(), tableNumber, arguments);
        } else {
            table = new YipeeTable(getId(), tableNumber);
        }
        tables.put(tableNumber, table);
        return table;
    }

    public YipeeTable getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    public void removeTable(int tableNumber) {
        tables.remove(tableNumber);
    }

    public String getLoungeName() {
        return loungeName;
    }

    public void setLoungeName(String loungeName) {
        this.loungeName = loungeName;
    }

    @Override
    public void dispose() {
        Util.clearArrays(players);
        if (tables != null) {
            tables.clear();
        }
    }

    @Override
    public YipeeRoom copy() {
        YipeeRoom copy = new YipeeRoom();
        copy.setName(this.name);
        copy.setLoungeName(this.loungeName);
        return copy;
    }

    @Override
    public YipeeRoom deepCopy() {
        YipeeRoom copy = copy();
        copyParent(copy);
        copy.setAllPlayers(players);
        copy.setAllTables(tables);
        return copy;
    }

    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try {
            if (adapter != null) {
                adapter.putAllTables(Util.getMapValues(tables));
                adapter.putAllPlayers(players);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName(), e);
        }
    }
/*
    @Override
    public void write(Json json) {
        super.write(json);
        if (json != null) {
            json.writeValue("loungeName", loungeName);
            json.writeValue("players", players);
            json.writeValue("tables", tables);
        }
    }

    @Override
    public void read(Json json, JsonValue jsonValue) {
        super.read(json, jsonValue);
        if (json != null) {
            loungeName = json.readValue("loungeName", String.class, jsonValue);
            players = json.readValue("players", Array.class, jsonValue);
            //need to add manually to preserve order
            ObjectMap<Object, Object> tablesJson = json.readValue("tables", ObjectMap.class, jsonValue);
            if (tablesJson != null) {
                for (Object keyStringObj : Util.getMapKeys(tablesJson)) {
                    if (keyStringObj instanceof String) {
                        int key = Integer.parseInt(keyStringObj.toString());
                        Object o = tablesJson.get(keyStringObj);
                        if (o instanceof YokelTable) {
                            tables.put(key, (YokelTable) o);
                        }
                    }
                }
            }
        }
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeRoom yokelRoom = (YipeeRoom) o;
        return players.equals(yokelRoom.players) && tables.equals(yokelRoom.tables) && getLoungeName().equals(yokelRoom.getLoungeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), players, tables, getLoungeName());
    }
}