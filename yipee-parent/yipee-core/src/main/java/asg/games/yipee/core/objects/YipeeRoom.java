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
package asg.games.yipee.core.objects;

import asg.games.yipee.common.enums.Copyable;
import asg.games.yipee.common.enums.Disposable;
import asg.games.yipee.core.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
 * - {@code YipeeRoom} owns {@code YipeeTableDTO}, {@code YipeeTableDTO} owns {@code YipeeSeatDTO}
 * - and {@code YipeeRoom}, {@code YipeeTableDTO} and {@code YipeeSeatDTO} may contain {@code YipeePlayer}.
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
public class YipeeRoom extends AbstractYipeeObject implements Copyable<YipeeRoom>, Disposable {
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

    @Getter
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

    /**
     * Creates an empty room with default settings.
     * Required for JSON serialization/deserialization.
     */
    public YipeeRoom() {
    }

    /**
     * Constructs a room with the given name.
     *
     * @param name the name of the room
     */
    public YipeeRoom(String name) {
        setName(name);
    }

    /**
     * Constructs a room with the given name and lounge.
     *
     * @param name       the name of the room
     * @param loungeName the lounge category for the room
     */
    public YipeeRoom(String name, String loungeName) {
        setName(name);
        setLoungeName(loungeName);
    }

    /**
     * Returns all tables currently present in this room.
     *
     * @return a collection of all {@link YipeeTable} instances
     */
    public Collection<YipeeTable> getTables() {
        return Util.getMapValues(tableIndexMap);
    }

    /**
     * Returns the numeric indexes of all tables in this room.
     *
     * @return a collection of table indexes
     */
    public Collection<Integer> getTableIndexes() {
        return Util.getMapKeys(tableIndexMap);
    }

    /**
     * Handles player entry into the room.
     *
     * @param player the player joining the room
     */
    public void joinRoom(YipeePlayer player) {
        if (player != null) {
            logger.info("Player: {}, is joining room: {}", player.getName(), this.getName());
        }
        players.add(player);
    }

    /**
     * Handles player exit from the room.
     *
     * @param player the player leaving the room
     */
    public void leaveRoom(YipeePlayer player) {
        if (player != null) {
            logger.info("Player: {}, is leaving room: {}", player.getName(), this.getName());
        }
        players.remove(player);
    }

    /**
     * Adds a new table to the room with an auto-generated table number.
     *
     * @return the newly added table
     */
    public YipeeTable addTable() {
        return addTable(null);
    }

    /**
     * Adds a new table with optional configuration parameters.
     *
     * @param arguments optional table properties to pass to the constructor
     * @return the newly created {@link YipeeTable}
     */
    public YipeeTable addTable(Map<String, Object> arguments) {
        int tableNumber = Util.getNextTableNumber(this);
        logger.debug("Next table number in iteration: " + tableNumber);
        return addTable(tableNumber, arguments);
    }

    /**
     * Adds a table to the room with a specific table number.
     *
     * @param tableNumber the desired table number
     * @param arguments optional table configuration
     * @return the newly created {@link YipeeTable}
     */
    public YipeeTable addTable(int tableNumber, Map<String, Object> arguments) {
        YipeeTable table = arguments != null
            ? new YipeeTable(tableNumber, arguments)
            : new YipeeTable(tableNumber);
        tableIndexMap.put(tableNumber, table);
        return table;
    }

    /**
     * Gets a reference to the table that matches the given object.
     *
     * @param table the table object to search for
     * @return the table instance in this room, or null if not found
     */
    public YipeeTable getTable(YipeeTable table) {
        return getTableAt(getTableIndexValue(table));
    }

    /**
     * Removes the specified table from the room.
     *
     * @param table the table to remove
     */
    public void removeTable(YipeeTable table) {
        removeTableAt(getTableIndexValue(table));
    }

    /**
     * Removes the table at the specified index from the room.
     *
     * @param index the table index
     */
    public void removeTableAt(int index) {
        if (index < 0 || index > tableIndexMap.size() + 1) {
            return;
        }
        YipeeTable table = getTableAt(index);
        tableIndexMap.remove(index);
    }

    /**
     * Retrieves the index associated with the given table.
     *
     * @param table the table to find
     * @return the index of the table, or -1 if not found
     */
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

    /**
     * Retrieves the table at the specified index.
     *
     * @param index the index of the table
     * @return the {@link YipeeTable} at the index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public YipeeTable getTableAt(int index) {
        if (index < 0 || index > tableIndexMap.size() + 1) {
            logger.error("Invalid table index: {}", index);
            throw new IndexOutOfBoundsException("Invalid table index: " + index);
        }
        return tableIndexMap.get(index);
    }

    /**
     * Disposes of the room's internal state, clearing player and table data.
     */
    @Override
    public void dispose() {
        Util.clearArrays(players);
        tableIndexMap.clear();
    }


    /**
     * Creates a shallow copy of this room.
     * Players and tables are not deep-copied.
     *
     * @return a basic duplicate of this room
     */
    @Override
    public YipeeRoom copy() {
        YipeeRoom copy = new YipeeRoom();
        copyParent(copy);
        copy.setLoungeName(this.loungeName);
        return copy;
    }

    /**
     * Creates a deep copy of the room including all players and tables.
     *
     * @return a deep-copied {@link YipeeRoom} with independent table/player objects
     */
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
}
