/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.objects;

import asg.games.yipee.persistence.YipeeObjectJPAVisitor;
import asg.games.yipee.persistence.YipeeStorageAdapter;
import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
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
    //@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    //@JsonManagedReference
    private Set<YipeePlayer> watchers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "parentRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonManagedReference
    private Set<YipeeTable> tables = new LinkedHashSet<>();

    @JsonProperty("lounge")
    private String loungeName = "_NoLoungeSelected";

    private final Map<Integer, YipeeTable> tableIndexMap = new HashMap<>();

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
        return Collections.unmodifiableCollection(tables);
    }

    public void setTables(Set<YipeeTable> tables) {
        this.tables = tables;
    }

    public Set<YipeePlayer> getWatchers() {
        return Collections.unmodifiableSet(watchers);
    }

    public void setWatchers(Set<YipeePlayer> watchers) {
        this.watchers = watchers;
    }

    public void joinRoom(YipeePlayer player) {
        if (player != null) {
            watchers.add(player);
        }
    }

    public void leaveRoom(YipeePlayer player) {
        watchers.remove(player);
    }

    public YipeeTable addTable() {
        return addTable(null);
    }

    public YipeeTable addTable(Map<String, Object> arguments) {
        int tableNumber = Util.getNextTableNumber(this);
        return addTable(tableNumber, arguments);
    }

    public Collection<YipeeTable> getAllTables() {
        return Util.getMapValues(tableIndexMap);
    }

    /*public List<Integer> getAllTableIndexes() {
        int _size = tables.size();
        Integer[] indexes = new Integer[_size];

        // Setting the value in the array
        Arrays.setAll(indexes, p -> p > _size - 1 ? 0 : p);

        return Util.arrayToList(indexes);
    }*/

    public Collection<Integer> getAllTableIndexes() {
        return Util.getMapKeys(tableIndexMap);
    }

    public YipeeTable addTable(int tableNumber, Map<String, Object> arguments) {
        YipeeTable table = arguments != null
                ? new YipeeTable(this, tableNumber, arguments)
                : new YipeeTable(this, tableNumber);

        tables.add(table);
        tableIndexMap.put(tableNumber, table);
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

    public YipeeTable getTableAt(int index) {
        if (index < 0 || index >= tableIndexMap.size()) {
            throw new IndexOutOfBoundsException("Invalid table index: " + index);
        }
        return tableIndexMap.get(index);
    }

    public String getLoungeName() {
        return loungeName;
    }

    public void setLoungeName(String loungeName) {
        this.loungeName = loungeName;
    }

    @Override
    public void dispose() {
        Util.clearArrays(watchers, tables);
        tableIndexMap.clear();
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
        copy.setWatchers(watchers);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YipeeRoom)) return false;
        if (!super.equals(o)) return false;
        YipeeRoom room = (YipeeRoom) o;
        return Objects.equals(getWatchers(), room.getWatchers()) && Objects.equals(getTables(), room.getTables()) && getLoungeName().equals(room.getLoungeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLoungeName());
    }

    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try {
            if (adapter != null) {
                adapter.putAllTables(tables);
                adapter.putAllWatchers(watchers);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName(), e);
        }
    }
}