package asg.games.yipee.net;

import asg.games.yipee.common.dto.NetYipeePlayer;
import asg.games.yipee.common.dto.NetYipeeTable;
import asg.games.yipee.common.enums.ACCESS_TYPE;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestNetYipeeTable extends TestNetYipeeObject implements NetYipeeTable<TestNetYipeeSeat, NetYipeePlayer> {
    ACCESS_TYPE accessType;
    boolean rated;
    boolean soundOn;
    int tableNumber;
    @Setter(AccessLevel.NONE) // don't let Lombok generate setSeats
    Set<NetYipeePlayer> watchers = new LinkedHashSet<>();
    @Setter(AccessLevel.NONE) // don't let Lombok generate setSeats
    Set<TestNetYipeeSeat> seats = new LinkedHashSet<>();

    public TestNetYipeeTable() {
    }

    public TestNetYipeeTable(int tableNumber) {
        setTableNumber(tableNumber);
    }

    @Override
    public void setWatchers(Iterable<NetYipeePlayer> watchers) {
        Set<NetYipeePlayer> hashSets = new LinkedHashSet<>();

        for (NetYipeePlayer o : watchers) {
            hashSets.add(o);
        }
        this.watchers = hashSets;
    }

    @Override
    public void setSeats(Iterable<TestNetYipeeSeat> seats) {
        Set<TestNetYipeeSeat> hashSets = new LinkedHashSet<>();

        for (TestNetYipeeSeat o : seats) {
            hashSets.add(o);
        }
        this.seats = hashSets;
    }
}
