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

import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"seatNumber", "occupied", "tableId"})
public class YipeeSeat extends AbstractYipeeObject implements Disposable {
    @Transient
    private static final Logger logger = LoggerFactory.getLogger(YipeeSeat.class);

    @JsonIgnore
    private static final String ATTR_SEAT_NUM_SEPARATOR = "-";

    private boolean isSeatReady = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seated_player_id", unique = true)
    private YipeePlayer seatedPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_table_id")
    @JsonBackReference("parentTable")
    private YipeeTable parentTable;

    //Empty Constructor required for Json.Serializable
    public YipeeSeat() {
    }

    public YipeeSeat(YipeeTable table, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTable(table);
        setName(getSeatName() + seatNumber);
    }

    private String getSeatName() {
        return getTableId() + ATTR_SEAT_NUM_SEPARATOR;
    }

    public boolean sitDown(YipeePlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (isOccupied()) {
            logger.info("Seat is already occupied.");
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

    public YipeeTable getParentTable() {
        return parentTable;
    }

    public void setParentTable(YipeeTable parentTable) {
        this.parentTable = parentTable;
        if (parentTable != null) {
            setName(getSeatName() + getSeatNumber());
        }
    }


    public boolean isOccupied() {
        return seatedPlayer != null;
    }

    public boolean isSeatReady() {
        return isOccupied() && isSeatReady;
    }

    public int getSeatNumber(){
        if (getName() == null) return -1;
        return Integer.parseInt(Util.split(getName(), ATTR_SEAT_NUM_SEPARATOR)[1]);
    }

    @Override
    public void dispose() {
        if(isOccupied()){
            standUp();
        }
    }

    public String getTableId() {
        if (parentTable != null) {
            return getParentTable().getId();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YipeeSeat)) return false;
        if (!super.equals(o)) return false;
        YipeeSeat yipeeSeat = (YipeeSeat) o;
        return isSeatReady == yipeeSeat.isSeatReady && Objects.equals(seatedPlayer, yipeeSeat.seatedPlayer) && Objects.equals(parentTable, yipeeSeat.parentTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isSeatReady, seatedPlayer, parentTable);
    }
}