package asg.games.yipee.objects;

import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

@JsonIgnoreProperties({"seatNumber", "occupied", "tableId"})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_SEATS")
public class YipeeSeat extends AbstractYipeeObject implements Disposable {
    @JsonIgnore
    private static final String ATTR_SEAT_NUM_SEPARATOR = "-";

    private boolean isSeatReady = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seated_player_id", unique = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private YipeePlayer seatedPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_table_id", unique = true, nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private YipeeTable parentTable;

    //Empty Constructor required for Json.Serializable
    public YipeeSeat() {
    }

    public YipeeSeat(YipeeTable table, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTable(table);
        setName(getTableId() + ATTR_SEAT_NUM_SEPARATOR + seatNumber);
    }

    public boolean sitDown(YipeePlayer player) {
        if (!isOccupied() && player != null) {
            setSeatedPlayer(player);
            return true;
        }
        return false;
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
    }

    public void setSeatedPlayer(YipeePlayer seatedPlayer) {
        this.seatedPlayer = seatedPlayer;
    }

    public boolean isOccupied() {
        return seatedPlayer != null;
    }

    public void setSeatReady(boolean isSeatReady) {
        this.isSeatReady = isSeatReady;
    }

    public boolean isSeatReady() {
        return isOccupied() && isSeatReady;
    }

    public YipeePlayer getSeatedPlayer(){
        return seatedPlayer;
    }

    public int getSeatNumber(){
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

}