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
 * Created by Blakbro2k on 1/28/2018.
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

    //Empty Constructor required for Json.Serializable
    public YipeeSeat() {
    }

    public YipeeSeat(String tableId, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTableId(tableId);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    public YipeeSeat(YipeeTable table, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTable(table);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    public void setSeatName() {
        setName(getSeatName() + seatNumber);
    }

    private String getSeatName() {
        logger.debug("seatname={}", getParentTableId() + ATTR_SEAT_NUM_SEPARATOR);
        return getParentTableId() + ATTR_SEAT_NUM_SEPARATOR;
    }

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

    public YipeePlayer standUp() {
        YipeePlayer player = seatedPlayer;
        setSeatedPlayer(null);
        setSeatReady(false);
        return player;
    }

    public void setParentTable(YipeeTable parentTable) {
        if (parentTable != null) {
            parentTableId = parentTable.getId();
            setName(getSeatName() + getSeatNumber());
        }
    }

    public boolean isOccupied() {
        return seatedPlayer != null;
    }

    public boolean isSeatReady() {
        return isOccupied() && isSeatReady;
    }

    @Override
    public void dispose() {
        if(isOccupied()){
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

    @Override
    public YipeeSeat copy() {
        YipeeSeat copy = new YipeeSeat();
        copyParent(copy);
        copy.setSeatReady(isSeatReady);
        copy.setSeatNumber(seatNumber);
        copy.setParentTableId(parentTableId);
        return copy;
    }

    @Override
    public YipeeSeat deepCopy() {
        YipeeSeat deep = copy();
        deep.setSeatedPlayer(seatedPlayer.deepCopy());
        return deep;
    }
}
