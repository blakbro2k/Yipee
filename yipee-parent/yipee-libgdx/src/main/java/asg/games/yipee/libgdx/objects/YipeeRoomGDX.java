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
 * This class is a persistent entity mapped to the database table {@code YT_ROOMS}.
 * The room can belong to a specific lounge, defined by its {@code loungeName}.
 * <p>
 * Responsibilities of this class include:
 * - Managing the players who join or leave the room.
 * - Adding, removing, and managing tables within the room.
 * - Providing utility methods for copying, deep copying, and persistence.
 * <p>
 * This class follows a hierarchical ownership model:
 * - {@code YipeeRoomGDX} owns {@code YipeeTableGDX}, {@code YipeeTableGDX} owns {@code YipeeSeatGDX}
 * - and {@code YipeeRoomGDX}, {@code YipeeTableGDX} and {@code YipeeSeatGDX} may contain {@code YipeePlayerGDX}.
 *
 * <p>
 * Created by Blakbro2k on 1/28/2018.
 * Updated for enhanced documentation and clarity.
 *
 * @see YipeeTableGDX
 * @see YipeeSeatGDX
 * @see YipeePlayerGDX
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeRoomGDX extends AbstractYipeeObjectGDX implements Copyable<YipeeRoomGDX>, Disposable {

    public static final String SOCIAL_LOUNGE = "Social";
    public static final String BEGINNER_LOUNGE = "Beginner";
    public static final String INTERMEDIATE_LOUNGE = "Intermediate";
    public static final String ADVANCED_LOUNGE = "Advanced";
    public static final String DEFAULT_LOUNGE_NAME = "_NoLoungeSelected";

    private String loungeName = DEFAULT_LOUNGE_NAME;

    private ObjectSet<YipeePlayerGDX> players = GdxSets.newSet();
    private ObjectMap<Integer, YipeeTableGDX> tableIndexMap = GdxMaps.newObjectMap();

    //Empty Constructor required for Json.Serializable
    public YipeeRoomGDX() {
    }

    public YipeeRoomGDX(String name) {
        setName(name);
    }

    public YipeeRoomGDX(String name, String loungeName) {
        setName(name);
        setLoungeName(loungeName);
    }

    public Iterable<YipeeTableGDX> getTables() {
        return tableIndexMap.values();
    }

    public Iterable<Integer> getTableIndexes() {
        return tableIndexMap.keys();
    }

    private void addPlayer(YipeePlayerGDX player) {
        players.add(player);
    }

    private void removePlayer(YipeePlayerGDX player) {
        players.remove(player);
    }

    public void joinRoom(YipeePlayerGDX player) {
        if (player != null) {
            addPlayer(player);
        }
    }

    public void leaveRoom(YipeePlayerGDX player) {
        if (player != null) {
            removePlayer(player);
        }
    }

    public YipeeTableGDX addTable() {
        return addTable(null);
    }

    public YipeeTableGDX addTable(ObjectMap<String, Object> arguments) {
        int tableNumber = LibGDXUtil.getNextTableNumber(this);
        return addTable(tableNumber, arguments);
    }

    public YipeeTableGDX addTable(int tableNumber, ObjectMap<String, Object> arguments) {
        YipeeTableGDX table = arguments != null
            ? new YipeeTableGDX(tableNumber, arguments)
            : new YipeeTableGDX(tableNumber);
        tableIndexMap.put(tableNumber, table);
        return table;
    }

    public YipeeTableGDX getTable(YipeeTableGDX table) {
        return getTableAt(getTableIndexValue(table));
    }

    public void removeTable(YipeeTableGDX table) {
        removeTableAt(getTableIndexValue(table));
    }

    public void removeTableAt(int index) {
        if (index < 0 || index > LibGDXUtil.sizeOf(LibGDXUtil.getMapValues(tableIndexMap)) + 1) {
            return;
        }
        YipeeTableGDX table = getTableAt(index);
        tableIndexMap.remove(index);
    }

    public int getTableIndexValue(YipeeTableGDX table) {
        int index = -1;
        if (tableIndexMap.containsValue(table, false)) {
            for (YipeeTableGDX _table : tableIndexMap.values()) {
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

    public YipeeTableGDX getTableAt(int index) {
        if (index < 0 || index > LibGDXUtil.sizeOf(tableIndexMap) + 1) {
            throw new IndexOutOfBoundsException("Invalid table index: " + index);
        }
        return tableIndexMap.get(index);
    }

    @Override
    public void dispose() {
        //Util.clearArrays(players);
        players.clear();
        tableIndexMap.clear();
    }

    @Override
    public YipeeRoomGDX copy() {
        YipeeRoomGDX copy = new YipeeRoomGDX();
        copyParent(copy);
        copy.setLoungeName(this.loungeName);
        return copy;
    }

    @Override
    public YipeeRoomGDX deepCopy() {
        YipeeRoomGDX copy = copy();

        // Deep copy of tables
        ObjectMap<Integer, YipeeTableGDX> deepCopiedTableMap = GdxMaps.newObjectMap();
        for (ObjectMap.Entry<Integer, YipeeTableGDX> entry : tableIndexMap.entries()) {
            deepCopiedTableMap.put(entry.key, entry.value != null ? entry.value.deepCopy() : null);
        }
        copy.setTableIndexMap(deepCopiedTableMap);

        // Deep copy of players
        ObjectSet<YipeePlayerGDX> deepCopiedPlayers = GdxSets.newSet();
        for (YipeePlayerGDX player : players) {
            // Assuming YipeePlayerGDX is immutable or has its own copy() / deepCopy()
            // If not, you might want to just add player directly:
            deepCopiedPlayers.add(player);
            // or if you have player.deepCopy():
            // deepCopiedPlayers.add(player.deepCopy());
        }
        copy.setPlayers(deepCopiedPlayers);

        return copy;
    }

}

/**
 * @Entity
 * @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 * @Table(name = "YT_ROOMS")
 * public class YipeeRoom extends AbstractYipeeObject implements YipeeObjectJPAVisitor, Copyable<YipeeRoom>, Disposable {
 * @Transient private Map<YipeeTableDTO, Integer> reverseTableIndexMap = new HashMap<>();
 * @OneToMany(mappedBy = "parentRoom", cascade = CascadeType.ALL, orphanRemoval = true)
 * @MapKey(name = "tableNumber")
 * @JsonManagedReference("parentRoom") private Map<Integer, YipeeTableDTO> tableIndexMap = new HashMap<>();
 * <p>
 * // Lifecycle Methods
 * @PostLoad private void populateReverseTableIndexMap() {
 * reverseTableIndexMap.clear();
 * for (Map.Entry<Integer, YipeeTableDTO> entry : tableIndexMap.entrySet()) {
 * reverseTableIndexMap.put(entry.getValue(), entry.getKey());
 * }
 * }
 * @PrePersist
 * @PreUpdate private void syncReverseTableIndexMap() {
 * reverseTableIndexMap.clear();
 * for (Map.Entry<Integer, YipeeTableDTO> entry : tableIndexMap.entrySet()) {
 * reverseTableIndexMap.put(entry.getValue(), entry.getKey());
 * }
 * }
 * <p>
 * // Add Table
 * public YipeeTableDTO addTable(int tableNumber, Map<String, Object> arguments) {
 * YipeeTableDTO table = arguments != null
 * ? new YipeeTableDTO(this, tableNumber, arguments)
 * : new YipeeTableDTO(this, tableNumber);
 * table.setParentRoom(this);
 * tableIndexMap.put(tableNumber, table);
 * reverseTableIndexMap.put(table, tableNumber); // Update reverse map
 * return table;
 * }
 * <p>
 * // Remove Table
 * public void removeTable(YipeeTableDTO table) {
 * Integer index = reverseTableIndexMap.remove(table); // Update reverse map
 * if (index != null) {
 * tableIndexMap.remove(index);
 * table.setParentRoom(null);
 * }
 * }
 * <p>
 * // Get Table Index
 * public int getTableIndexValue(YipeeTableDTO table) {
 * return reverseTableIndexMap.getOrDefault(table, -1);
 * }
 * <p>
 * // Setter for Table Index Map
 * public void setTableIndexMap(Map<Integer, YipeeTableDTO> tableIndexMap) {
 * this.tableIndexMap = tableIndexMap;
 * reverseTableIndexMap.clear();
 * for (Map.Entry<Integer, YipeeTableDTO> entry : tableIndexMap.entrySet()) {
 * reverseTableIndexMap.put(entry.getValue(), entry.getKey());
 * }
 * }
 * <p>
 * // Other existing methods remain unchanged...
 * }
 */
