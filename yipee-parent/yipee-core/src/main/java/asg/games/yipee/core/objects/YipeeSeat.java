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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents a seat at a {@link YipeeTable} in the Yipee game.
 * Each seat may be assigned a {@link YipeePlayer} and has a numeric index (0–7).
 *
 * <p>Supports sitting, standing, and tracking readiness for the player occupying it.</p>
 *
 * <p>This class is a persistent entity mapped to the {@code YT_SEATS} database table.</p>
 *
 * @author Blakbro2k
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_SEATS")
@JsonIgnoreProperties({"occupied", "tableId"})
public class YipeeSeat extends AbstractYipeeObject implements Copyable<YipeeSeat>, Disposable {
    @Transient
    private static final Logger logger = LoggerFactory.getLogger(YipeeSeat.class);

    @JsonIgnore
    private static final String ATTR_SEAT_NUM_SEPARATOR = "-";

    private boolean isSeatReady = false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "seated_player_id", unique = true)
    private YipeePlayer seatedPlayer;

    private int seatNumber;

    private String parentTableId;

    /**
     * Default constructor for JSON deserialization and ORM.
     */
    public YipeeSeat() {
    }

    /**
     * Constructs a new seat with the given table ID and seat number.
     *
     * @param tableId    the ID of the parent table
     * @param seatNumber the seat's number (must be between 0 and 7)
     * @throws IllegalArgumentException if the seat number is out of bounds
     */
    public YipeeSeat(String tableId, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTableId(tableId);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    /**
     * Constructs a new seat using the given table object and seat number.
     *
     * @param table the parent {@link YipeeTable}
     * @param seatNumber the seat's number (must be between 0 and 7)
     * @throws IllegalArgumentException if the seat number is out of bounds
     */
    public YipeeSeat(YipeeTable table, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTable(table);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    /**
     * Sets the seat's internal name based on the parent table ID and seat number.
     * Typically called automatically in constructors or when setting the parent.
     */
    public void setSeatName() {
        setName(getSeatName() + seatNumber);
    }

    /**
     * Generates the base name of the seat using the parent table ID and a separator.
     *
     * @return the prefix portion of the seat name
     */
    private String getSeatName() {
        logger.debug("seatname={}", getParentTableId() + ATTR_SEAT_NUM_SEPARATOR);
        return getParentTableId() + ATTR_SEAT_NUM_SEPARATOR;
    }

    /**
     * Attempts to seat a player in this seat.
     *
     * @param player the {@link YipeePlayer} attempting to sit down
     * @return {@code true} if successful, {@code false} if the seat is already occupied
     * @throws IllegalArgumentException if the player is null
     */
    public boolean sitDown(YipeePlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (isOccupied()) {
            logger.debug("Seat is already occupied.");
            return false;
        }
        setSeatedPlayer(player);
        return true;
    }

    /**
     * Removes the currently seated player from this seat.
     *
     * @return the player who was seated, or {@code null} if none
     */
    public YipeePlayer standUp() {
        YipeePlayer player = seatedPlayer;
        setSeatedPlayer(null);
        setSeatReady(false);
        return player;
    }

    /**
     * Sets the parent table for this seat and updates the internal seat name accordingly.
     *
     * @param parentTable the parent {@link YipeeTable}
     */
    public void setParentTable(YipeeTable parentTable) {
        if (parentTable != null) {
            parentTableId = parentTable.getId();
            setName(getSeatName() + getSeatNumber());
        }
    }

    /**
     * Returns whether the seat currently has a player.
     *
     * @return {@code true} if occupied, otherwise {@code false}
     */
    public boolean isOccupied() {
        return seatedPlayer != null;
    }

    /**
     * Returns whether the seat is both occupied and marked as ready.
     *
     * @return {@code true} if the seated player is ready, otherwise {@code false}
     */
    public boolean isSeatReady() {
        return isOccupied() && isSeatReady;
    }

    /**
     * Removes the player from this seat if occupied and resets readiness.
     */
    @Override
    public void dispose() {
        if (isOccupied()) {
            standUp();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeSeat yipeeSeat = (YipeeSeat) o;
        return isSeatReady == yipeeSeat.isSeatReady && seatNumber == yipeeSeat.seatNumber && Objects.equals(seatedPlayer, yipeeSeat.seatedPlayer) && Objects.equals(parentTableId, yipeeSeat.parentTableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isSeatReady, seatedPlayer, seatNumber, parentTableId);
    }

    /**
     * Creates a shallow copy of this seat (does not copy player).
     *
     * @return a new {@code YipeeSeat} instance with copied attributes
     */
    @Override
    public YipeeSeat copy() {
        YipeeSeat copy = new YipeeSeat();
        copyParent(copy);
        copy.setSeatReady(isSeatReady);
        copy.setSeatNumber(seatNumber);
        copy.setParentTableId(parentTableId);
        return copy;
    }

    /**
     * Creates a deep copy of this seat, including a deep copy of the seated player.
     *
     * @return a new {@code YipeeSeat} instance with duplicated state
     */
    @Override
    public YipeeSeat deepCopy() {
        YipeeSeat deep = copy();
        deep.setSeatedPlayer(seatedPlayer.deepCopy());
        return deep;
    }
}
