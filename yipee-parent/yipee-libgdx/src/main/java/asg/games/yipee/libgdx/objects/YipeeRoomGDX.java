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
package asg.games.yipee.libgdx.objects;

import asg.games.yipee.libgdx.tools.LibGDXUtil;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a room in the game where players can join, interact, and play at tables.
 * Each room contains a set of players and a collection of tables.
 * <p>
 * This class is designed for LibGDX and implements {@link Copyable} and {@link Disposable} for
 * resource management and cloning.
 * </p>
 *
 * Responsibilities:
 * <ul>
 *     <li>Manages room-level metadata like lounge name</li>
 *     <li>Manages players who join or leave</li>
 *     <li>Manages creation and deletion of tables</li>
 *     <li>Supports deep copying for state replication</li>
 * </ul>
 *
 * @see YipeeTableGDX
 * @see YipeeSeatGDX
 * @see YipeePlayerGDX
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeRoomGDX extends AbstractYipeeObjectGDX implements Copyable<YipeeRoomGDX>, Disposable {

    /**
     * Predefined name for the Social lounge
     */
    public static final String SOCIAL_LOUNGE = "Social";

    /** Predefined name for the Beginner lounge */
    public static final String BEGINNER_LOUNGE = "Beginner";

    /** Predefined name for the Intermediate lounge */
    public static final String INTERMEDIATE_LOUNGE = "Intermediate";

    /** Predefined name for the Advanced lounge */
    public static final String ADVANCED_LOUNGE = "Advanced";

    /** Fallback name for when no lounge is selected */
    public static final String DEFAULT_LOUNGE_NAME = "_NoLoungeSelected";

    /** Name of the lounge this room belongs to */
    private String loungeName = DEFAULT_LOUNGE_NAME;

    /** Set of players currently present in this room */
    private ObjectSet<YipeePlayerGDX> players = GdxSets.newSet();

    /** Map of table number to corresponding table in this room */
    private ObjectMap<Integer, YipeeTableGDX> tableIndexMap = GdxMaps.newObjectMap();

    /**
     * Default constructor required for serialization.
     */
    public YipeeRoomGDX() {
    }

    /**
     * Constructs a new room with the given name.
     *
     * @param name the room's name
     */
    public YipeeRoomGDX(String name) {
        setName(name);
    }

    /**
     * Constructs a new room with the given name and lounge name.
     *
     * @param name the room's name
     * @param loungeName the lounge name it belongs to
     */
    public YipeeRoomGDX(String name, String loungeName) {
        setName(name);
        setLoungeName(loungeName);
    }

    /**
     * @return iterable of all tables in this room
     */
    public Iterable<YipeeTableGDX> getTables() {
        return tableIndexMap.values();
    }

    /**
     * @return iterable of all table index keys
     */
    public Iterable<Integer> getTableIndexes() {
        return tableIndexMap.keys();
    }

    /**
     * Adds the specified player to the internal player set.
     * <p>
     * This method does not perform null checks or duplicate validation.
     * It is intended for internal use by public methods such as {@code joinRoom()}.
     *
     * @param player the player to add
     */
    private void addPlayer(YipeePlayerGDX player) {
        players.add(player);
    }

    /**
     * Removes the specified player from the internal player set.
     * <p>
     * This method does not check whether the player exists in the set.
     * It is intended for internal use by public methods such as {@code leaveRoom()}.
     *
     * @param player the player to remove
     */
    private void removePlayer(YipeePlayerGDX player) {
        players.remove(player);
    }


    /**
     * Adds a player to the room.
     *
     * @param player the player joining
     */
    public void joinRoom(YipeePlayerGDX player) {
        if (player != null) {
            addPlayer(player);
        }
    }

    /**
     * Removes a player from the room.
     *
     * @param player the player leaving
     */
    public void leaveRoom(YipeePlayerGDX player) {
        if (player != null) {
            removePlayer(player);
        }
    }

    /**
     * Adds a new table with an auto-generated index.
     *
     * @return the newly added table
     */
    public YipeeTableGDX addTable() {
        return addTable(null);
    }

    /**
     * Adds a new table with optional arguments.
     *
     * @param arguments table initialization parameters
     * @return the newly added table
     */
    public YipeeTableGDX addTable(ObjectMap<String, Object> arguments) {
        int tableNumber = LibGDXUtil.getNextTableNumber(this);
        return addTable(tableNumber, arguments);
    }

    /**
     * Adds a new table with the specified index and optional parameters.
     *
     * @param tableNumber the desired index of the table
     * @param arguments initialization arguments
     * @return the newly added table
     */
    public YipeeTableGDX addTable(int tableNumber, ObjectMap<String, Object> arguments) {
        YipeeTableGDX table = arguments != null
            ? new YipeeTableGDX(tableNumber, arguments)
            : new YipeeTableGDX(tableNumber);
        tableIndexMap.put(tableNumber, table);
        return table;
    }

    /**
     * Looks up a table in the map by instance.
     *
     * @param table the table to find
     * @return the matched table from map, or null if not found
     */
    public YipeeTableGDX getTable(YipeeTableGDX table) {
        return getTableAt(getTableIndexValue(table));
    }

    /**
     * Removes the specified table from the map.
     *
     * @param table the table to remove
     */
    public void removeTable(YipeeTableGDX table) {
        removeTableAt(getTableIndexValue(table));
    }

    /**
     * Removes the table at the given index.
     *
     * @param index table index to remove
     */
    public void removeTableAt(int index) {
        if (index < 0 || index > LibGDXUtil.sizeOf(LibGDXUtil.getMapValues(tableIndexMap)) + 1) {
            return;
        }
        YipeeTableGDX table = getTableAt(index);
        tableIndexMap.remove(index);
    }

    /**
     * Gets the index of the given table in the map.
     *
     * @param table table to search for
     * @return index or -1 if not found
     */
    public int getTableIndexValue(YipeeTableGDX table) {
        int index = -1;
        if (tableIndexMap.containsValue(table, false)) {
            for (YipeeTableGDX _table : tableIndexMap.values()) {
                index++;
                if (_table != null && _table.equals(table)) {
                    break;
                }
            }
        }
        return index;
    }

    /**
     * Retrieves the table at a specific index.
     *
     * @param index index of table
     * @return the table at that index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public YipeeTableGDX getTableAt(int index) {
        if (index < 0 || index > LibGDXUtil.sizeOf(tableIndexMap) + 1) {
            throw new IndexOutOfBoundsException("Invalid table index: " + index);
        }
        return tableIndexMap.get(index);
    }

    /**
     * Clears the room's player and table data.
     */
    @Override
    public void dispose() {
        players.clear();
        tableIndexMap.clear();
    }

    /**
     * Returns a shallow copy of this room (excluding players and tables).
     *
     * @return new {@link YipeeRoomGDX} with copied metadata
     */
    @Override
    public YipeeRoomGDX copy() {
        YipeeRoomGDX copy = new YipeeRoomGDX();
        copyParent(copy);
        copy.setLoungeName(this.loungeName);
        return copy;
    }

    /**
     * Returns a deep copy of this room, including all tables and players.
     * Players are shallow-copied unless their own deepCopy() method is used.
     *
     * @return a fully cloned {@link YipeeRoomGDX}
     */
    @Override
    public YipeeRoomGDX deepCopy() {
        YipeeRoomGDX copy = copy();

        // Deep copy of tables
        ObjectMap<Integer, YipeeTableGDX> deepCopiedTableMap = GdxMaps.newObjectMap();
        for (ObjectMap.Entry<Integer, YipeeTableGDX> entry : tableIndexMap.entries()) {
            deepCopiedTableMap.put(entry.key, entry.value != null ? entry.value.deepCopy() : null);
        }
        copy.setTableIndexMap(deepCopiedTableMap);

        // Deep copy of players (shallow copy if player is assumed immutable)
        ObjectSet<YipeePlayerGDX> deepCopiedPlayers = GdxSets.newSet();
        for (YipeePlayerGDX player : players) {
            deepCopiedPlayers.add(player);
            // Use deepCopiedPlayers.add(player.deepCopy()) if deep clone is available
        }
        copy.setPlayers(deepCopiedPlayers);

        return copy;
    }
}
