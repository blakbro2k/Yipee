package asg.games.yipee.objects;

import asg.games.yipee.persistence.YipeeObjectJPAVisitor;
import asg.games.yipee.persistence.YipeeStorageAdapter;
import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_ROOMS")
public class YipeeRoom extends AbstractYipeeObject implements YipeeObjectJPAVisitor, Copyable<YipeeRoom>, Disposable {
    @JsonIgnore
    public static final String SOCIAL_LOUNGE = "Social";
    @JsonIgnore
    public static final String BEGINNER_LOUNGE = "Beginner";
    @JsonIgnore
    public static final String INTERMEDIATE_LOUNGE = "Intermediate";
    @JsonIgnore
    public static final String ADVANCED_LOUNGE = "Advanced";

    @ManyToMany(mappedBy = "rooms")
    private Set<YipeePlayer> players = new LinkedHashSet<>();

    @OneToMany(mappedBy = "parentRoom")
    private Set<YipeeTable> tables = new LinkedHashSet<>();

    @JsonProperty("lounge")
    private String loungeName = "_NoLoungeSelected";

    //Empty Constructor required for Json.Serializable
    public YipeeRoom() {
    }

    public YipeeRoom(String name) {
        setName(name);
    }

    public YipeeRoom(String name, String loungeName) {
        setName(name);
        setLoungeName(loungeName);
    }

    public Collection<YipeeTable> getTables() {
        return tables;
    }

    public void setTables(Set<YipeeTable> tables) {
        this.tables = tables;
    }

    public Set<YipeePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<YipeePlayer> players) {
        this.players = players;
    }

    public void joinRoom(YipeePlayer player) {
        if (player != null) {
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
        int tableNumber = Util.getNextTableNumber(this);
        return addTable(tableNumber, arguments);
    }

    public YipeeTable addTable(int tableNumber, Map<String, Object> arguments) {
        YipeeTable table;

        if (arguments != null) {
            table = new YipeeTable(this, tableNumber, arguments);
        } else {
            table = new YipeeTable(this, tableNumber);
        }
        tables.add(table);
        return table;
    }

    public void removeTable(YipeeTable table) {
        tables.remove(table);
    }

    public void removeTableAt(int index) {
        if (index > tables.size() + 1) {
            return;
        }
        removeTable(getTableAt(index));
    }

    public YipeeTable getTable(YipeeTable table) {
        return getTableAt(getTableIndexValue(table));
    }

    public int getTableIndexValue(YipeeTable table) {
        int index = -1;
        if (tables.contains(table)) {
            for (YipeeTable _table : tables) {
                index++;
                if (_table != null) {
                    if (_table.equals(table)) {
                        break;
                    }
                }
            }
        }
        return index;
    }

    public List<Integer> getAllTableIndexes() {
        int _size = tables.size();
        Integer[] indexes = new Integer[_size];

        // Setting the value in the array
        Arrays.setAll(indexes, p -> p > _size - 1 ? 0 : p);

        return Util.arrayToList(indexes);
    }

    public YipeeTable getTableAt(int index) {
        if (index > tables.size() + 1) {
            return null;
        }
        return tables.toArray(new YipeeTable[0])[index];
    }

    public String getLoungeName() {
        return loungeName;
    }

    public void setLoungeName(String loungeName) {
        this.loungeName = loungeName;
    }

    @Override
    public void dispose() {
        Util.clearArrays(players, tables);
    }

    @Override
    public YipeeRoom copy() {
        YipeeRoom copy = new YipeeRoom();
        copyParent(copy);
        copy.setLoungeName(this.loungeName);
        return copy;
    }

    @Override
    public YipeeRoom deepCopy() {
        YipeeRoom copy = copy();
        copy.setTables(tables);
        copy.setPlayers(players);
        return copy;
    }

    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try {
            if (adapter != null) {
                //adapter.putAllTables(tables);
                //adapter.putAllPlayers(players);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName(), e);
        }
    }

}