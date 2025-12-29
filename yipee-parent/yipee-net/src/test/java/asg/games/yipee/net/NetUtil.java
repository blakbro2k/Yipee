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

public class NetUtil {
    /**
     * Casts a {@link TestNetYipeePlayer} to its core {@link TestNetYipeePlayer} implementation if possible.
     *
     * @param testNetYipeePlayer the network representation of a player
     * @return the core {@link TestNetYipeePlayer} if compatible, or {@code null} if not
     */
    public static TestNetYipeePlayer getCoreNetPlayer(NetYipeePlayer testNetYipeePlayer) {
        if (testNetYipeePlayer instanceof TestNetYipeePlayer) {
            TestNetYipeePlayer player = (TestNetYipeePlayer) testNetYipeePlayer;
            TestNetYipeePlayer retPlayer = new TestNetYipeePlayer(player.getName(), player.getRating(), player.getIcon());
            retPlayer.setId(player.getId());
            retPlayer.setName(player.getName());
            retPlayer.setCreated(player.getCreated());
            retPlayer.setModified(player.getModified());
            retPlayer.setIcon(player.getIcon());
            retPlayer.setRating(player.getRating());
            return retPlayer;
        }
        return null;
    }

    /**
     * Gets the name of the player from a {@link TestNetYipeePlayer} if it is a {@link TestNetYipeePlayer}.
     *
     * @param netTestNetYipeePlayer the network player object
     * @return the player's name, or empty string if not a {@link TestNetYipeePlayer}
     */
    public static String getCoreNetPlayerName(NetYipeePlayer netTestNetYipeePlayer) {
        TestNetYipeePlayer player = getCoreNetPlayer(netTestNetYipeePlayer);
        return player != null ? player.getName() : "";
    }

    /**
     * Casts a {@link NetYipeeTable} to its core {@link TestNetYipeeTable} implementation if possible.
     *
     * @param netYipeeTable the network player object
     * @return the core {@link TestNetYipeeTable} if compatible, or {@code null} if not
     */
    public static TestNetYipeeTable getCoreNetTable(NetYipeeTable netYipeeTable) {
        if (netYipeeTable instanceof TestNetYipeeTable) {
            TestNetYipeeTable table = (TestNetYipeeTable) netYipeeTable;
            TestNetYipeeTable retTable = new TestNetYipeeTable(table.getTableNumber());
            retTable.setId(table.getId());
            retTable.setName(table.getName());
            retTable.setCreated(table.getCreated());
            retTable.setModified(table.getModified());
            retTable.setTableNumber(table.getTableNumber());
            retTable.setRated(table.isRated());
            retTable.setSoundOn(table.isSoundOn());
            return retTable;
        }
        return null;
    }
}
