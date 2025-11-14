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

import asg.games.yipee.common.dto.NetYipeeTable;
import asg.games.yipee.common.enums.ACCESS_TYPE;
import asg.games.yipee.common.enums.Copyable;
import asg.games.yipee.libgdx.tools.LibGDXUtil;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a table within a Yipee room where up to 8 players can be seated and grouped for gameplay.
 * Each table consists of a fixed number of {@link YipeeSeatGDX} instances and supports:
 * <ul>
 *   <li>Configurable access type (PUBLIC, PRIVATE, PROTECTED).</li>
 *   <li>Rated/unrated gameplay mode.</li>
 *   <li>Sound toggle.</li>
 *   <li>Player watchers (spectators).</li>
 *   <li>Ready-check logic to determine if enough seats are filled to begin the game.</li>
 * </ul>
 * <p>
 * Tables are uniquely named using the {@code tableNumber} and a naming pattern that includes their parent room ID.
 *
 * <p>
 * Created by Blakbro2k on 1/28/2018.
 *
 * @see YipeeSeatGDX
 * @see YipeePlayerGDX
 * @see YipeeRoomGDX
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeTableGDX extends AbstractYipeeObjectGDX implements Copyable<YipeeTableGDX>, Disposable, NetYipeeTable<YipeeSeatGDX, YipeePlayerGDX> {
    public static final String ARG_TYPE = "type";
    public static final String ARG_RATED = "rated";
    public static final String ARG_SOUND = "sound";

    public static final String ATT_NAME_PREPEND = "#";
    public static final String ATT_TABLE_SPACER = "_room_tbl";
    public static final int MAX_SEATS = 8;


    /**
     * Access control type for this table (e.g., PUBLIC, PRIVATE, PROTECTED).
     */
    private ACCESS_TYPE accessType = ACCESS_TYPE.PUBLIC;

    /** Set of all 8 player seats available at the table. */
    @Setter(AccessLevel.NONE) // don't let Lombok generate setSeats
    private ObjectSet<YipeeSeatGDX> seats = GdxSets.newSet();

    /** Players who are watching (spectating) the table. */
    @Setter(AccessLevel.NONE) // don't let Lombok generate setWatchers
    private ObjectSet<YipeePlayerGDX> watchers = GdxSets.newSet();

    /** Numeric identifier for the table, used in naming. */
    private int tableNumber;

    /** Whether this game is rated (affects ranking/score). */
    private boolean isRated = false;

    /** Whether game sounds are enabled for this table. */
    private boolean isSoundOn = true;

    /**
     * Creates an empty table for serialization purposes.
     */
    public YipeeTableGDX() {
    }

    /**
     * Creates a new table with the specified table number.
     * @param nameNumber The numeric identifier used in naming.
     */
    public YipeeTableGDX(int nameNumber) {
        this(nameNumber, null);
    }

    /**
     * Creates a new table with a table number and configuration arguments.
     * @param nameNumber Table number used in naming.
     * @param arguments Optional configuration map (type, rated, sound).
     */
    public YipeeTableGDX(int nameNumber, ObjectMap<String, Object> arguments) {
        initialize(nameNumber, arguments);
    }

    /**
     * Initializes the table name, seats, and optional arguments.
     * @param nameNumber The numeric ID for naming.
     * @param arguments Optional configuration map.
     */
    private void initialize(int nameNumber, ObjectMap<String, Object> arguments) {
        setTableName(nameNumber);
        setUpSeats();
        setUpArguments(arguments);
    }

    /**
     * Constructs and sets the table name using the internal ID and the specified table number.
     * Format: {id}_room_tbl#{tableNumber}
     *
     * @param tableNumber the numeric table index used to distinguish this table.
     */
    public void setTableName(int tableNumber) {
        setName(getId() + ATT_TABLE_SPACER + ATT_NAME_PREPEND + tableNumber);
    }

    /**
     * Extracts and returns the table number from the current table name.
     * Assumes table name follows the pattern set in {@link #setTableName(int)}.
     *
     * @return the numeric table identifier parsed from the name.
     */
    public int getTableNumber() {
        return Integer.parseInt(LibGDXUtil.split(getName(), ATT_NAME_PREPEND)[1]);
    }

    private void setUpArguments(ObjectMap<String, Object> arguments) {
        if (arguments != null) {
            for (String key : arguments.keys()) {
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
     * Processes a single configuration argument and applies it to the table.
     * @param arg The argument key.
     * @param value The associated value.
     */
    private void processArg(String arg, Object value) {
        if (arg != null && value != null) {
            if (LibGDXUtil.equalsIgnoreCase(ARG_TYPE, arg)) {
                setAccessType(LibGDXUtil.otos(value));
            } else if (LibGDXUtil.equalsIgnoreCase(ARG_RATED, arg)) {
                setRated(LibGDXUtil.otob(value));
            } else if (LibGDXUtil.equalsIgnoreCase(ARG_SOUND, arg)) {
                setSoundOn(LibGDXUtil.otob(value));
            }
        }
    }

    public void setAccessType(ACCESS_TYPE accessType) {
        this.accessType = accessType;
    }

    @Override
    public void setWatchers(Iterable<YipeePlayerGDX> watchers) {
        this.watchers = GdxSets.newSet(watchers);
    }

    @Override
    public void setSeats(Iterable<YipeeSeatGDX> seats) {
        this.seats = GdxSets.newSet(seats);
    }

    /**
     * Sets the access type using a case-insensitive string.
     * Accepts "PRIVATE", "PUBLIC", or "PROTECTED".
     * Defaults to PUBLIC if invalid or null.
     *
     * @param accessType The string representation of access type.
     */
    public void setAccessType(String accessType) {
        if (LibGDXUtil.equalsIgnoreCase(ACCESS_TYPE.PRIVATE.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PRIVATE);
        } else if (LibGDXUtil.equalsIgnoreCase(ACCESS_TYPE.PROTECTED.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PROTECTED);
        } else {
            setAccessType(ACCESS_TYPE.PUBLIC);
        }
    }

    /**
     * Indicates whether the table is configured for rated gameplay.
     * Rated games may affect player ranking, stats, or match history.
     *
     * @return true if the game is rated; false otherwise.
     */
    public boolean isRated() {
        return isRated;
    }

    /**
     * Indicates whether sound effects are enabled for the game on this table.
     *
     * @return true if sound is enabled; false if muted.
     */
    public boolean isSoundOn() {
        return isSoundOn;
    }

    /**
     * Determines whether a given group of 2 seats is ready.
     * There are 4 groups (0–3), each corresponding to 2 adjacent seats.
     *
     * @param g the group index (0 to 3).
     * @return true if either seat in the group is marked ready.
     */
    public boolean isGroupReady(int g) {
        if (g < 0 || g > 3) {
            return false;
        }
        int seatNumber = g * 2;
        return isSeatReady(seatNumber) || isSeatReady(seatNumber + 1);
    }

    /**
     * Checks whether a specific seat is occupied and marked ready.
     *
     * @param seat the {@link YipeeSeatGDX} instance to check.
     * @return true if the seat is ready; false otherwise.
     */
    public boolean isSeatReady(YipeeSeatGDX seat) {
        if (seat != null) {
            return seat.isSeatReady();
        }
        return false;
    }

    /**
     * Checks if the seat at the given index is ready.
     *
     * @param seatNum the seat number (0–7).
     * @return true if the seat is ready; false otherwise.
     */
    public boolean isSeatReady(int seatNum) {
        return isSeatReady(getSeat(seatNum));
    }

    /**
     * Determines if the table is ready to start the game.
     * A game is considered start-ready if at least two groups are marked ready.
     *
     * @return true if at least two groups of players are ready to play.
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
     * Initializes all 8 seats for the table.
     */
    private void setUpSeats() {
        for (int i = 0; i < MAX_SEATS; i++) {
            seats.add(new YipeeSeatGDX(this, i));
        }
    }

    /**
     * Resets all seats in the table to unready state.
     * Typically called before a game restart or match setup.
     */
    public void makeAllTablesUnready() {
        for (YipeeSeatGDX seat : LibGDXUtil.safeIterable(seats)) {
            if (seat != null) {
                seat.setSeatReady(false);
            }
        }
    }

    /**
     * Retrieves a seat by index from the internal seat set.
     *
     * @param seatNum the seat index (0–7).
     * @return the {@link YipeeSeatGDX} instance, or null if invalid.
     */
    public YipeeSeatGDX getSeat(int seatNum) {
        return LibGDXUtil.getIndexOfSet(seats, seatNum);
    }

    /**
     * Adds a player to the set of watchers (spectators) for this table.
     *
     * @param player the {@link YipeePlayerGDX} to add as a watcher.
     */
    public void addWatcher(YipeePlayerGDX player) {
        if (player != null) {
            watchers.add(player);
        }
    }

    /**
     * Removes a player from the set of watchers (spectators) for this table.
     *
     * @param player the {@link YipeePlayerGDX} to remove from watching.
     */
    public void removeWatcher(YipeePlayerGDX player) {
        if (player != null) {
            watchers.remove(player);
        }
    }

    /**
     * Disposes of the table by clearing all seats and watcher references.
     * This is used for cleanup in lifecycle-managed environments.
     */
    @Override
    public void dispose() {
        //LibGDXUtil.clearArrays(seats, watchers);
        seats.clear();
        watchers.clear();
    }

    /**
     * Creates a shallow copy of the table with shared seat and watcher sets.
     * Used when deep duplication is not needed.
     * @return A new {@link YipeeTableGDX} instance with basic fields copied.
     */
    @Override
    public YipeeTableGDX copy() {
        YipeeTableGDX copy = new YipeeTableGDX();
        copyParent(copy);
        copy.setAccessType(accessType);
        copy.setRated(isRated);
        copy.setSoundOn(isSoundOn);
        return copy;
    }

    /**
     * Creates a deep copy of the table with duplicated seat and watcher sets.
     * Useful for simulation or rollback where shared references are not allowed.
     * @return A fully cloned {@link YipeeTableGDX} instance.
     */
    @Override
    public YipeeTableGDX deepCopy() {
        YipeeTableGDX copy = copy();
        ObjectSet<YipeeSeatGDX> copiedSeats = GdxSets.newSet();
        for (YipeeSeatGDX seat : seats) {
            copiedSeats.add(seat.deepCopy());
        }
        copy.setSeats(copiedSeats);
        copy.setWatchers(GdxSets.newSet(watchers));
        return copy;
    }
}
