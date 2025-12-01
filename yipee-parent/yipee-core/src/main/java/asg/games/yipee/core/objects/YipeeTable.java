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

import asg.games.yipee.common.dto.NetYipeeTable;
import asg.games.yipee.common.enums.ACCESS_TYPE;
import asg.games.yipee.common.enums.Copyable;
import asg.games.yipee.common.enums.Disposable;
import asg.games.yipee.core.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a table in a Yipee game room, containing up to 8 seats for players and optional watchers.
 * Tables can be rated, have sound enabled, and be marked as public, private, or protected.
 *
 * <p>This class supports deep copying, ORM persistence, and argument-based setup for configuration.
 *
 * <p>Each table is initialized with 8 seats by default, and supports various checks such as seat readiness and table start readiness.
 *
 * @see YipeeSeat
 * @see YipeePlayer
 * @see YipeeRoom
 */
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_TABLES")
@JsonIgnoreProperties({"tableStartReady", "upArguments", "tableName", "roomId"})
public class YipeeTable extends AbstractYipeeObject implements Copyable<YipeeTable>, Disposable, NetYipeeTable<YipeeSeat, YipeePlayer> {
    private static final Logger logger = LoggerFactory.getLogger(YipeeTable.class);

    @JsonIgnore
    public static final String ARG_TYPE = "type";
    @JsonIgnore
    public static final String ARG_RATED = "rated";
    @JsonIgnore
    public static final String ARG_SOUND = "sound";
    @JsonIgnore
    public static final String ATT_NAME_PREPEND = "#";
    @JsonIgnore
    public static final String ATT_TABLE_SPACER = "_room_tbl";
    @JsonIgnore
    public static final int MAX_SEATS = 8;

    @Getter
    private ACCESS_TYPE accessType = ACCESS_TYPE.PUBLIC;

    @Setter(AccessLevel.NONE) // don't let Lombok generate setSeats
    @OneToMany(mappedBy = "parentTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<YipeeSeat> seats = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
        name = "YT_TABLE_PLAYERS",
        joinColumns = @JoinColumn(name = "table_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @Setter(AccessLevel.NONE) // don't let Lombok generate setWatchers
    private Set<YipeePlayer> watchers = new LinkedHashSet<>();


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private YipeeRoom room;

    /**
     * Room-local numeric identifier for this table (e.g., 1, 2, 4).
     * Exposed to clients as "tableNumber" and used as the map key in YipeeRoom.
     */
    @Getter
    @Column(name = "table_number", nullable = false)
    @JsonProperty("tableNumber")
    private int tableNumber;


    @JsonProperty("rated")
    private boolean isRated = false;

    @JsonProperty("soundOn")
    private boolean isSoundOn = true;

    /**
     * Default constructor required for JSON serialization and ORM frameworks.
     */
    public YipeeTable() {
    }

    /**
     * Constructs a new table with the given table number, using default arguments.
     *
     * @param nameNumber the table's numeric identifier
     */
    public YipeeTable(int nameNumber) {
        this(nameNumber, "");
    }

    /**
     * Constructs a new table using the provided name number and argument strings.
     *
     * @param nameNumber      the table's numeric identifier
     * @param argumentStrings optional configuration flags (e.g., "rated", "sound", "type")
     */
    public YipeeTable(int nameNumber, String... argumentStrings) {
        this(nameNumber, buildArgumentsFromStrings(argumentStrings));
    }

    /**
     * Builds a map of arguments from an array of strings.
     * (Currently a stub — implementation needed to parse flags such as "rated", "sound", etc.)
     *
     * @param argumentStrings the array of argument flags
     * @return a key-value map representing configuration arguments
     */
    private static Map<String, Object> buildArgumentsFromStrings(String[] argumentStrings) {
        Map<String, Object> arguments = new HashMap<>();
        for (String argumentString : Util.safeIterableArray(argumentStrings)) {
            if (argumentString == null) continue;
            String trimmed = argumentString.trim();
            if (trimmed.isEmpty()) continue;

            String[] argPair = Util.split(trimmed, ":");
            if (argPair.length != 2) continue;

            String key = argPair[0].trim();
            String value = argPair[1].trim();

            if (Util.equalsIgnoreCase(ARG_RATED, key)) {
                arguments.put(ARG_RATED, value);
            } else if (Util.equalsIgnoreCase(ARG_SOUND, key)) {
                arguments.put(ARG_SOUND, value);
            } else if (Util.equalsIgnoreCase(ARG_TYPE, key)) {
                arguments.put(ARG_TYPE, value);
            } else {
                // Optional: support arbitrary extra args for future use
                arguments.put(key, value);
            }
        }
        return arguments;
    }


    /**
     * Constructs a new table using a map of arguments.
     *
     * @param nameNumber the table's numeric identifier
     * @param arguments key-value map of configuration options
     */
    public YipeeTable(int nameNumber, Map<String, Object> arguments) {
        initialize(nameNumber, arguments);
    }

    /**
     * Initializes the table name, seats, and configuration from arguments.
     *
     * @param nameNumber numeric identifier
     * @param arguments optional argument map for configuration
     */
    private void initialize(int nameNumber, Map<String, Object> arguments) {
        setTableNumber(nameNumber);    // sets field + derived name
        setUpSeats();
        setUpArguments(arguments);
    }

    /**
     * Builds the table's name using its ID and numeric index.
     *
     * @param tableNumber the table's numeric index
     */
    private String buildTableName(int tableNumber) {
        return ATT_TABLE_SPACER + ATT_NAME_PREPEND + tableNumber;
    }

    /**
     * Sets the table's number.
     *
     * @param tableNumber the table's numeric index
     */
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
        // Keep name in sync whenever number changes
        setName(buildTableName(tableNumber));
    }

    /**
     * Sets the table's configuration based on argument map.
     * Accepts values like "rated", "sound", and "type".
     *
     * @param arguments argument map
     */
    private void setUpArguments(Map<String, Object> arguments) {
        if (arguments != null) {
            for (String key : Util.getMapKeys(arguments)) {
                if (key != null) {
                    Object o = arguments.get(key);
                    processArg(key, o);
                }
            }
        } else { //Setting up Table with Default arguments
            setAccessType(ACCESS_TYPE.PUBLIC);
            setRated(false);
        }
    }

    /**
     * Applies a single argument to the table configuration.
     *
     * @param arg argument key
     * @param value argument value
     */
    private void processArg(String arg, Object value) {
        if (arg != null && value != null) {
            if (Util.equalsIgnoreCase(ARG_TYPE, arg)) {
                setAccessType(Util.otos(value));
            } else if (Util.equalsIgnoreCase(ARG_RATED, arg)) {
                setRated(Util.otob(value));
            } else if (Util.equalsIgnoreCase(ARG_SOUND, arg)) {
                setSoundOn(Util.otob(value));
            }
        }
    }

    /**
     * Sets the table's access type.
     *
     * @param accessType an {@link ACCESS_TYPE} value
     */
    public void setAccessType(ACCESS_TYPE accessType) {
        this.accessType = accessType;
    }

    @Override
    public void setWatchers(Iterable<YipeePlayer> watchers) {
        this.watchers = Util.iterableToSet(watchers);
    }

    @Override
    public void setSeats(Iterable<YipeeSeat> seats) {
        this.seats = Util.iterableToSet(seats);
    }

    /**
     * Parses and sets the table's access type from a string.
     *
     * @param accessType string representation of access type
     */
    public void setAccessType(String accessType) {
        if (Util.equalsIgnoreCase(ACCESS_TYPE.PRIVATE.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PRIVATE);
        } else if (Util.equalsIgnoreCase(ACCESS_TYPE.PROTECTED.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PROTECTED);
        } else {
            setAccessType(ACCESS_TYPE.PUBLIC);
        }
    }

    /**
     * Checks if either seat in the specified group is ready.
     *
     * @param g the group index (0 to 3)
     * @return true if at least one seat in the group is ready
     */
    public boolean isGroupReady(int g) {
        if (g < 0 || g > 3) {
            return false;
        }
        int seatNumber = g * 2;
        return isSeatReady(seatNumber) || isSeatReady(seatNumber + 1);
    }

    /**
     * Checks whether the given seat is ready.
     *
     * @param seat the seat to check
     * @return true if the seat is marked ready
     */
    public boolean isSeatReady(YipeeSeat seat) {
        if (seat != null) {
            return seat.isSeatReady();
        }
        return false;
    }

    /**
     * Checks whether the seat at the given index is ready.
     *
     * @param seatNum the seat index
     * @return true if the seat is ready
     */
    public boolean isSeatReady(int seatNum) {
        return isSeatReady(getSeat(seatNum));
    }

    /**
     * Checks whether the table has at least two ready seat groups.
     *
     * @return true if table is ready to start
     */
    public boolean isTableStartReady() {
        int readyGroups = 0;
        for (int i = 0; i < 4; i++) {
            if (isGroupReady(i)) readyGroups++;
            if (readyGroups > 1) return true;
        }
        return false;
    }

    /**
     * Initializes this table with the default number of seats (8).
     * Each seat is associated with this table and assigned a unique index from 0 to 7.
     */
    private void setUpSeats() {
        for (int i = 0; i < MAX_SEATS; i++) {
            seats.add(new YipeeSeat(this, i));
        }
    }

    /**
     * Marks all seats as not ready.
     */
    public void makeAllSeatsUnready() {
        for (YipeeSeat seat : Util.safeIterable(seats)) {
            if (seat != null) {
                seat.setSeatReady(false);
            }
        }
    }

    /**
     * Gets the seat at the specified index.
     *
     * @param seatNum index of the seat (0–7)
     * @return the seat object at that index
     */
    public YipeeSeat getSeat(int seatNum) {
        return Util.getIndexOfSet(seats, seatNum);
    }

    /**
     * Adds a player to the list of table watchers.
     *
     * @param player the player to add
     */
    public void addWatcher(YipeePlayer player) {
        if (player != null) {
            watchers.add(player);
        }
    }

    /**
     * Removes a player from the list of table watchers.
     *
     * @param player the player to remove
     */
    public void removeWatcher(YipeePlayer player) {
        if (player != null) {
            watchers.remove(player);
        }
    }

    /**
     * Clears seat and watcher data for memory cleanup.
     */
    @Override
    public void dispose() {
        Util.clearArrays(seats, watchers);
    }

    /**
     * Creates a shallow copy of this table (does not copy seats or watchers).
     *
     * @return a new {@code YipeeTable} with copied attributes
     */
    @Override
    public YipeeTable copy() {
        YipeeTable copy = new YipeeTable();
        copyParent(copy);
        copy.setAccessType(accessType);
        copy.setRated(isRated);
        copy.setSoundOn(isSoundOn);
        return copy;
    }

    /**
     * Creates a deep copy of this table, including cloned seats and watchers.
     *
     * @return a fully cloned {@code YipeeTable}
     */
    @Override
    public YipeeTable deepCopy() {
        YipeeTable copy = copy();
        Set<YipeeSeat> newSeats = new LinkedHashSet<>();
        for (YipeeSeat seat : seats) {
            newSeats.add(seat.deepCopy());
        }
        copy.setSeats(newSeats);

        Set<YipeePlayer> newWatchers = new LinkedHashSet<>();
        for (YipeePlayer watcher : watchers) {
            newWatchers.add(watcher.deepCopy());
        }
        copy.setWatchers(newWatchers);
        return copy;
    }
}
