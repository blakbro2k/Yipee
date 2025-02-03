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
package asg.games.yipee.objects;

import asg.games.yipee.persistence.TerminatorJPAVisitor;
import asg.games.yipee.persistence.YipeeObjectJPAVisitor;
import asg.games.yipee.persistence.YipeeObjectTerminatorAdapter;
import asg.games.yipee.persistence.YipeeStorageAdapter;
import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a room in the game where players can join, interact, and play at tables.
 * Each room contains a set of players and a collection of tables.
 * <p>
 * This class is a persistent entity mapped to the database table {@code YT_ROOMS}.
 * The room can belong to a specific lounge, defined by its {@code loungeName}.
 * <p>
 * Responsibilities of this class include:
 * - Managing the players who join or leave the room.
 * - Adding, removing, and managing tables within the room.
 * - Providing utility methods for copying, deep copying, and persistence.
 * <p>
 * This class follows a hierarchical ownership model:
 * - {@code YipeeRoom} owns {@code YipeeTable}, {@code YipeeTable} owns {@code YipeeSeat}
 * - and {@code YipeeRoom}, {@code YipeeTable} and {@code YipeeSeat} may contain {@code YipeePlayer}.
 *
 * <p>
 * Created by Blakbro2k on 1/28/2018.
 * Updated for enhanced documentation and clarity.
 *
 * @see YipeeTable
 * @see YipeeSeat
 * @see YipeePlayer
 */
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_ROOMS")
public class YipeeRoom extends AbstractYipeeObject implements YipeeObjectJPAVisitor, TerminatorJPAVisitor, Copyable<YipeeRoom>, Disposable {
    @Transient
    private static final Logger logger = LoggerFactory.getLogger(YipeeRoom.class);

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

    @JsonProperty("lounge")
    private String loungeName = DEFAULT_LOUNGE_NAME;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "YT_ROOM_PLAYERS", // Join table name
            joinColumns = @JoinColumn(name = "room_id"), // Foreign key to YipeeRoom
            inverseJoinColumns = @JoinColumn(name = "player_id") // Foreign key to YipeePlayer
    )
    private Set<YipeePlayer> players = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "tableNumber")
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
        return Util.getMapValues(tableIndexMap);
    }

    public Set<YipeePlayer> getPlayers() {
        return players;
    }

    public Collection<Integer> getTableIndexes() {
        return Util.getMapKeys(tableIndexMap);
    }

    private void addPlayer(YipeePlayer player) {
        players.add(player);
    }

    private void removePlayer(YipeePlayer player) {
        players.remove(player);
    }

    public void joinRoom(YipeePlayer player) {
        if (player != null) {
            logger.info("Player: {}, is joining room: {}", player.getName(), this.getName());
        }
        addPlayer(player);
    }

    public void leaveRoom(YipeePlayer player) {
        if (player != null) {
            logger.info("Player: {}, is leaving room: {}", player.getName(), this.getName());
        }
        removePlayer(player);
    }

    public YipeeTable addTable() {
        return addTable(null);
    }

    public YipeeTable addTable(Map<String, Object> arguments) {
        int tableNumber = Util.getNextTableNumber(this);
        logger.debug("Next table number in iteration: " + tableNumber);
        return addTable(tableNumber, arguments);
    }

    public YipeeTable addTable(int tableNumber, Map<String, Object> arguments) {
        YipeeTable table = arguments != null
                ? new YipeeTable(tableNumber, arguments)
                : new YipeeTable(tableNumber);
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
            logger.error("Invalid table index: {}", index);
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
        logger.info("Building a deep copy of table map");
        Map<Integer, YipeeTable> deepCopiedTableMap = new HashMap<>();
        for (Map.Entry<Integer, YipeeTable> entry : this.tableIndexMap.entrySet()) {
            deepCopiedTableMap.put(entry.getKey(), entry.getValue().deepCopy());
        }
        copy.setTableIndexMap(deepCopiedTableMap);
        copy.setPlayers(players.stream().map(YipeePlayer::deepCopy).collect(Collectors.toSet())); // Assuming YipeePlayer is immutable or cloned
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YipeeRoom)) return false;
        if (!super.equals(o)) return false;
        YipeeRoom room = (YipeeRoom) o;
        return Objects.equals(loungeName, room.loungeName) && Objects.equals(players, room.players) && Objects.equals(tableIndexMap, room.tableIndexMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loungeName, players, tableIndexMap);
    }

    /**
     * Visiting method that handles saving child objects to the database.
     *
     * @param adapter
     */
    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try {
            if (adapter != null) {
                adapter.visitSaveYipeeRoom(this);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName(), e);
        }
    }

    /**
     * Visiting method that handles deleting child objects from the database.
     *
     * @param terminatorAdapter
     */
    @Override
    public void visitDelete(YipeeObjectTerminatorAdapter terminatorAdapter) {
        try {
            if (terminatorAdapter != null) {
                terminatorAdapter.visitTerminateYipeeRoom(this);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting termination for " + this.getClass().getSimpleName(), e);
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