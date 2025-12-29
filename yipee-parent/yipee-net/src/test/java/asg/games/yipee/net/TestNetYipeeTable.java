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
