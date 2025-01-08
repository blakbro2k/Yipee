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
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

@Getter
@Setter
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
    @JsonIgnore
    public static final String DEFAULT_LOUNGE_NAME = "_NoLoungeSelected";


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "YT_ROOM_PLAYERS", // Join table name
            joinColumns = @JoinColumn(name = "room_id"), // Foreign key to YipeeRoom
            inverseJoinColumns = @JoinColumn(name = "player_id") // Foreign key to YipeePlayer
    )
    @JsonManagedReference("room-players")
    private Set<YipeePlayer> players = new LinkedHashSet<>();

    @JsonProperty("lounge")
    private String loungeName = DEFAULT_LOUNGE_NAME;

    @OneToMany(mappedBy = "parentRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "tableNumber")
    @JsonManagedReference("parentRoom")
    private Map<Integer, YipeeTable> tableIndexMap = new HashMap<>();

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
        return Collections.unmodifiableCollection(Util.getMapValues(tableIndexMap));
    }

    public Set<YipeePlayer> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public Collection<Integer> getTableIndexes() {
        return Collections.unmodifiableCollection(Util.getMapKeys(tableIndexMap));
    }

    private void addPlayer(YipeePlayer player) {
        players.add(player);
        player.getRooms().add(this); // Maintain bidirectional consistency
    }

    private void removePlayer(YipeePlayer player) {
        players.remove(player);
        player.getRooms().add(this); // Maintain bidirectional consistency
    }

    public void joinRoom(YipeePlayer player) {
        addPlayer(player);
    }

    public void leaveRoom(YipeePlayer player) {
        removePlayer(player);
    }

    public YipeeTable addTable() {
        return addTable(null);
    }

    public YipeeTable addTable(Map<String, Object> arguments) {
        int tableNumber = Util.getNextTableNumber(this);
        return addTable(tableNumber, arguments);
    }

    public YipeeTable addTable(int tableNumber, Map<String, Object> arguments) {
        YipeeTable table = arguments != null
                ? new YipeeTable(this, tableNumber, arguments)
                : new YipeeTable(this, tableNumber);
        table.setParentRoom(this);
        tableIndexMap.put(tableNumber, table);
        return table;
    }

    public YipeeTable getTable(YipeeTable table) {
        return getTableAt(getTableIndexValue(table));
    }

    public void removeTable(YipeeTable table) {
        removeTableAt(getTableIndexValue(table));
    }

    public void removeTableAt(int index) {
        if (index < 0 || index > tableIndexMap.size() + 1) {
            return;
        }
        YipeeTable table = getTableAt(index);
        table.setParentRoom(null);
        tableIndexMap.remove(index);
    }

    public int getTableIndexValue(YipeeTable table) {
        int index = -1;
        if (tableIndexMap.containsValue(table)) {
            for (YipeeTable _table : tableIndexMap.values()) {
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
        if (index < 0 || index > tableIndexMap.size() + 1) {
            throw new IndexOutOfBoundsException("Invalid table index: " + index);
        }
        return tableIndexMap.get(index);
    }

    @Override
    public void dispose() {
        Util.clearArrays(players);
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
        Map<Integer, YipeeTable> deepCopiedTableMap = new HashMap<>();
        for (Map.Entry<Integer, YipeeTable> entry : this.tableIndexMap.entrySet()) {
            deepCopiedTableMap.put(entry.getKey(), entry.getValue().deepCopy());
        }
        copy.setTableIndexMap(deepCopiedTableMap);
        copy.setPlayers(new LinkedHashSet<>(this.players)); // Assuming YipeePlayer is immutable or cloned
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YipeeRoom room)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getPlayers(), room.getPlayers()) && Objects.equals(getTables(), room.getTables()) && getLoungeName().equals(room.getLoungeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLoungeName());
    }

    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try {
            if (adapter != null) {
                adapter.visitYipeeRoom(this);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName(), e);
        }
    }
}

/**
 * @Entity
 * @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 * @Table(name = "YT_ROOMS")
 * public class YipeeRoom extends AbstractYipeeObject implements YipeeObjectJPAVisitor, Copyable<YipeeRoom>, Disposable {
 * @Transient private Map<YipeeTable, Integer> reverseTableIndexMap = new HashMap<>();
 * @OneToMany(mappedBy = "parentRoom", cascade = CascadeType.ALL, orphanRemoval = true)
 * @MapKey(name = "tableNumber")
 * @JsonManagedReference("parentRoom") private Map<Integer, YipeeTable> tableIndexMap = new HashMap<>();
 * <p>
 * // Lifecycle Methods
 * @PostLoad private void populateReverseTableIndexMap() {
 * reverseTableIndexMap.clear();
 * for (Map.Entry<Integer, YipeeTable> entry : tableIndexMap.entrySet()) {
 * reverseTableIndexMap.put(entry.getValue(), entry.getKey());
 * }
 * }
 * @PrePersist
 * @PreUpdate private void syncReverseTableIndexMap() {
 * reverseTableIndexMap.clear();
 * for (Map.Entry<Integer, YipeeTable> entry : tableIndexMap.entrySet()) {
 * reverseTableIndexMap.put(entry.getValue(), entry.getKey());
 * }
 * }
 * <p>
 * // Add Table
 * public YipeeTable addTable(int tableNumber, Map<String, Object> arguments) {
 * YipeeTable table = arguments != null
 * ? new YipeeTable(this, tableNumber, arguments)
 * : new YipeeTable(this, tableNumber);
 * table.setParentRoom(this);
 * tableIndexMap.put(tableNumber, table);
 * reverseTableIndexMap.put(table, tableNumber); // Update reverse map
 * return table;
 * }
 * <p>
 * // Remove Table
 * public void removeTable(YipeeTable table) {
 * Integer index = reverseTableIndexMap.remove(table); // Update reverse map
 * if (index != null) {
 * tableIndexMap.remove(index);
 * table.setParentRoom(null);
 * }
 * }
 * <p>
 * // Get Table Index
 * public int getTableIndexValue(YipeeTable table) {
 * return reverseTableIndexMap.getOrDefault(table, -1);
 * }
 * <p>
 * // Setter for Table Index Map
 * public void setTableIndexMap(Map<Integer, YipeeTable> tableIndexMap) {
 * this.tableIndexMap = tableIndexMap;
 * reverseTableIndexMap.clear();
 * for (Map.Entry<Integer, YipeeTable> entry : tableIndexMap.entrySet()) {
 * reverseTableIndexMap.put(entry.getValue(), entry.getKey());
 * }
 * }
 * <p>
 * // Other existing methods remain unchanged...
 * }
 */