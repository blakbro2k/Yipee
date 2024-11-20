package asg.games.yipee.objects;

import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

@JsonIgnoreProperties({"seatNumber", "occupied", "seatReady"})
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "YT_SEATS")
public class YipeeSeat extends AbstractYipeeObject implements Disposable {
    @JsonIgnore
    private static final String ATTR_SEAT_NUM_SEPARATOR = "-";

    @ManyToOne
    @JoinColumn(name = "seated_player_id")
    private YipeePlayer seatedPlayer;
    private String tableId;
    private boolean isSeatReady = false;

    public void setSeatedPlayer(YipeePlayer seatedPlayer) {
        this.seatedPlayer = seatedPlayer;
    }

    //Empty Constructor required for Json.Serializable
    public YipeeSeat() {
    }

    public YipeeSeat(String tableId, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setTableId(tableId);
        setName(tableId + ATTR_SEAT_NUM_SEPARATOR + seatNumber);
    }

    public boolean sitDown(YipeePlayer player) {
        if (!isOccupied()) {
            seatedPlayer = player;
            return true;
        }
        return false;
    }

    public YipeePlayer standUp(){
        YipeePlayer var = seatedPlayer;
        seatedPlayer = null;
        setSeatReady(false);
        return var;
    }

    public boolean isOccupied(){
        return seatedPlayer != null;
    }

    public void setSeatReady(boolean isSeatReady){
        this.isSeatReady = isSeatReady;
    }

    public boolean isSeatReady(){
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
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeSeat yokelSeat = (YipeeSeat) o;
        return isSeatReady() == yokelSeat.isSeatReady() && Objects.equals(getSeatedPlayer(), yokelSeat.getSeatedPlayer()) && Objects.equals(getTableId(), yokelSeat.getTableId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSeatedPlayer(), getTableId(), isSeatReady());
    }
}