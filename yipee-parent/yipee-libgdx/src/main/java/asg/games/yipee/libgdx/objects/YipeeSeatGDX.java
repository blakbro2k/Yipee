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

import com.badlogic.gdx.utils.Disposable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeSeatGDX extends AbstractYipeeObjectGDX implements asg.games.yipee.libgdx.objects.Copyable<YipeeSeatGDX>, Disposable {
    private static final String ATTR_SEAT_NUM_SEPARATOR = "-";

    private boolean isSeatReady = false;

    private YipeePlayerGDX seatedPlayer;

    private int seatNumber;

    private String parentTableId;

    //Empty Constructor required for Json.Serializable
    public YipeeSeatGDX() {
    }

    public YipeeSeatGDX(String tableId, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTableId(tableId);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    public YipeeSeatGDX(YipeeTableGDX table, int seatNumber) {
        if (seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        setParentTable(table);
        setSeatNumber(seatNumber);
        setSeatName();
    }

    public void setSeatName() {
        setName(getSeatName() + seatNumber);
    }

    private String getSeatName() {
        return getParentTableId() + ATTR_SEAT_NUM_SEPARATOR;
    }

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

    public YipeePlayerGDX standUp() {
        YipeePlayerGDX player = seatedPlayer;
        setSeatedPlayer(null);
        setSeatReady(false);
        return player;
    }

    public void setParentTable(YipeeTableGDX parentTable) {
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
        if (isOccupied()) {
            standUp();
        }
    }

    @Override
    public YipeeSeatGDX copy() {
        YipeeSeatGDX copy = new YipeeSeatGDX();
        copyParent(copy);
        copy.setParentTableId(parentTableId);
        copy.setSeatReady(isSeatReady);
        copy.setSeatNumber(seatNumber);
        return copy;
    }

    @Override
    public YipeeSeatGDX deepCopy() {
        YipeeSeatGDX deep = copy();
        deep.setSeatedPlayer(seatedPlayer.deepCopy());
        return null;
    }
}
