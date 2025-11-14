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

import asg.games.yipee.common.dto.NetYipeeSeat;
import asg.games.yipee.common.enums.Copyable;
import com.badlogic.gdx.utils.Disposable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a seat within a table in a Yipee game room.
 * A seat can be occupied by a player and marked as ready to play.
 * <p>
 * Each seat tracks its parent table and seat number for identification.
 * Seats are uniquely named using the table ID and seat number (e.g., "table-3").
 * <p>
 * Key responsibilities of this class include:
 * - Managing player sit/stand actions.
 * - Tracking readiness status.
 * - Providing methods for copying and cleanup.
 *
 * Created by Blakbro2k on 1/28/2018.
 *
 * @see YipeeTableGDX
 * @see YipeePlayerGDX
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeSeatGDX extends AbstractYipeeObjectGDX
    implements Copyable<YipeeSeatGDX>, Disposable, NetYipeeSeat {

    private static final String ATTR_SEAT_NUM_SEPARATOR = "-";

    /**
     * Indicates whether the seated player is ready.
     */
    private boolean isSeatReady = false;

    /** The player currently occupying the seat, or null if unoccupied. */
    private YipeePlayerGDX seatedPlayer;

    /** The numeric seat index (0–7). */
    private int seatNumber;

    /** The ID of the table this seat belongs to. */
    private String parentTableId;

    /**
     * Default constructor required for JSON serialization.
     */
    public YipeeSeatGDX() {
    }

    /**
     * Constructs a seat with a specific table ID and seat number.
     *
     * @param tableId    the parent table's ID
     * @param seatNumber the seat index (must be between 0 and 7)
     */
    public YipeeSeatGDX(String tableId, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7)
            throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTableId(tableId);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    /**
     * Constructs a seat using a reference to the parent table object.
     *
     * @param table      the parent table
     * @param seatNumber the seat index (must be between 0 and 7)
     */
    public YipeeSeatGDX(YipeeTableGDX table, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7)
            throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTable(table);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    /**
     * Sets the seat's unique name based on table ID and seat number.
     */
    public void setSeatName() {
        setName(getSeatName() + seatNumber);
    }

    /**
     * Builds the base name using the parent table ID and separator.
     *
     * @return the seat name prefix
     */
    private String getSeatName() {
        return getParentTableId() + ATTR_SEAT_NUM_SEPARATOR;
    }

    /**
     * Attempts to sit a player down in the seat.
     *
     * @param player the player to assign
     * @return true if the seat was empty and the player was seated; false if already occupied
     * @throws IllegalArgumentException if the player is null
     */
    public boolean sitDown(YipeePlayerGDX player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (isOccupied()) {
            return false;
        }
        setSeatedPlayer(player);
        return true;
    }

    /**
     * Removes the player from the seat and resets readiness.
     *
     * @return the player who stood up, or null if no one was seated
     */
    public YipeePlayerGDX standUp() {
        YipeePlayerGDX player = seatedPlayer;
        setSeatedPlayer(null);
        setSeatReady(false);
        return player;
    }

    /**
     * Sets the parent table ID and updates the seat's name accordingly.
     *
     * @param parentTable the table this seat belongs to
     */
    public void setParentTable(YipeeTableGDX parentTable) {
        if (parentTable != null) {
            parentTableId = parentTable.getId();
            setName(getSeatName() + getSeatNumber());
        }
    }

    /**
     * Checks whether a player is currently seated.
     *
     * @return true if the seat is occupied, false otherwise
     */
    public boolean isOccupied() {
        return seatedPlayer != null;
    }

    /**
     * Determines if the seat is both occupied and marked as ready.
     *
     * @return true if seat has a player and is ready
     */
    public boolean isSeatReady() {
        return isOccupied() && isSeatReady;
    }

    /**
     * Clears the seat, removing the player if occupied.
     */
    @Override
    public void dispose() {
        if (isOccupied()) {
            standUp();
        }
    }

    /**
     * Creates a shallow copy of this seat (excluding player).
     *
     * @return a new YipeeSeatGDX with the same configuration
     */
    @Override
    public YipeeSeatGDX copy() {
        YipeeSeatGDX copy = new YipeeSeatGDX();
        copyParent(copy);
        copy.setParentTableId(parentTableId);
        copy.setSeatReady(isSeatReady);
        copy.setSeatNumber(seatNumber);
        return copy;
    }

    /**
     * Creates a deep copy of the seat, including a deep copy of the player if present.
     *
     * @return a deep copy of this seat
     */
    @Override
    public YipeeSeatGDX deepCopy() {
        YipeeSeatGDX deep = copy();
        deep.setSeatedPlayer(seatedPlayer != null ? seatedPlayer.deepCopy() : null);
        return deep;
    }
}
