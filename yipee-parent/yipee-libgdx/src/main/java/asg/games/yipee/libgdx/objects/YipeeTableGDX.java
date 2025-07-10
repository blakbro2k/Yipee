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
package asg.games.yipee.libgdx.objects;

import asg.games.yipee.libgdx.tools.LibGDXUtil;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class YipeeTableGDX extends AbstractYipeeObjectGDX implements Copyable<YipeeTableGDX>, Disposable {
    public static final String ARG_TYPE = "type";
    public static final String ARG_RATED = "rated";
    public static final String ARG_SOUND = "sound";
    public static final String ENUM_VALUE_PRIVATE = "PRIVATE";
    public static final String ENUM_VALUE_PUBLIC = "PUBLIC";
    public static final String ENUM_VALUE_PROTECTED = "PROTECTED";
    public static final String ATT_NAME_PREPEND = "#";
    public static final String ATT_TABLE_SPACER = "_room_tbl";
    public static final int MAX_SEATS = 8;

    public enum ACCESS_TYPE {
        PRIVATE(ENUM_VALUE_PRIVATE), PUBLIC(ENUM_VALUE_PUBLIC), PROTECTED(ENUM_VALUE_PROTECTED);
        private final String accessType;

        ACCESS_TYPE(String accessType) {
            this.accessType = accessType;
        }

        public String getValue() {
            return accessType;
        }
    }

    private ACCESS_TYPE accessType = ACCESS_TYPE.PUBLIC;

    private ObjectSet<YipeeSeatGDX> seats = GdxSets.newSet();

    private ObjectSet<YipeePlayerGDX> watchers = GdxSets.newSet();

    private Integer tableNumber;

    private boolean isRated = false;

    private boolean isSoundOn = true;

    //Empty Constructor required for Json.Serializable
    public YipeeTableGDX() {
    }

    public YipeeTableGDX(int nameNumber) {
        this(nameNumber, null);
    }

    public YipeeTableGDX(int nameNumber, ObjectMap<String, Object> arguments) {
        initialize(nameNumber, arguments);
    }

    private void initialize(int nameNumber, ObjectMap<String, Object> arguments) {
        setTableName(nameNumber);
        setUpSeats();
        setUpArguments(arguments);
    }

    public void setTableName(int tableNumber) {
        setName(getId() + ATT_TABLE_SPACER + ATT_NAME_PREPEND + tableNumber);
    }

    public int getTableNumber() {
        return Integer.parseInt(LibGDXUtil.split(getName(), ATT_NAME_PREPEND)[1]);
    }

    private void setUpArguments(ObjectMap<String, Object> arguments) {
        if (arguments != null) {
            for (String key : arguments.keys()) {
                if (key != null) {
                    Object o = arguments.get(key);
                    processArg(key, o);
                }
            }
        } else { //Setting up Table with Default arguments
            setAccessType(ACCESS_TYPE.PUBLIC);
            setRated(false);
        }
    }

    private void processArg(String arg, Object value) {
        if (arg != null && value != null) {
            if (LibGDXUtil.equalsIgnoreCase(ARG_TYPE, arg)) {
                setAccessType(LibGDXUtil.otos(value));
            } else if (LibGDXUtil.equalsIgnoreCase(ARG_RATED, arg)) {
                setRated(LibGDXUtil.otob(value));
            } else if (LibGDXUtil.equalsIgnoreCase(ARG_SOUND, arg)) {
                setSound(LibGDXUtil.otob(value));
            }
        }
    }

    public void setAccessType(ACCESS_TYPE accessType) {
        this.accessType = accessType;
    }

    public void setAccessType(String accessType) {
        if (LibGDXUtil.equalsIgnoreCase(ACCESS_TYPE.PRIVATE.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PRIVATE);
        } else if (LibGDXUtil.equalsIgnoreCase(ACCESS_TYPE.PROTECTED.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PROTECTED);
        } else {
            setAccessType(ACCESS_TYPE.PUBLIC);
        }
    }

    public void setRated(boolean rated) {
        this.isRated = rated;
    }

    public void setSound(boolean sound) {
        this.isSoundOn = sound;
    }

    public boolean isRated() {
        return isRated;
    }

    public boolean isSoundOn() {
        return isSoundOn;
    }

    public boolean isGroupReady(int g) {
        if (g < 0 || g > 3) {
            return false;
        }
        int seatNumber = g * 2;
        return isSeatReady(seatNumber) || isSeatReady(seatNumber + 1);
    }

    public boolean isSeatReady(YipeeSeatGDX seat) {
        if (seat != null) {
            return seat.isSeatReady();
        }
        return false;
    }

    public boolean isSeatReady(int seatNum) {
        return isSeatReady(getSeat(seatNum));
    }

    public boolean isTableStartReady() {
        int readyGroups = 0;
        for (int i = 0; i < 4; i++) {
            if (isGroupReady(i)) readyGroups++;
            if (readyGroups > 1) return true;
        }
        return false;
    }

    private void setUpSeats() {
        for (int i = 0; i < MAX_SEATS; i++) {
            seats.add(new YipeeSeatGDX(this, i));
        }
    }

    public void makeAllTablesUnready() {
        for (YipeeSeatGDX seat : LibGDXUtil.safeIterable(seats)) {
            if (seat != null) {
                seat.setSeatReady(false);
            }
        }
    }

    public YipeeSeatGDX getSeat(int seatNum) {
        return LibGDXUtil.getIndexOfSet(seats, seatNum);
    }

    public void addWatcher(YipeePlayerGDX player) {
        if (player != null) {
            watchers.add(player);
        }
    }

    public void removeWatcher(YipeePlayerGDX player) {
        if (player != null) {
            watchers.remove(player);
        }
    }


    @Override
    public void dispose() {
        //LibGDXUtil.clearArrays(seats, watchers);
        seats.clear();
        watchers.clear();
    }

    @Override
    public YipeeTableGDX copy() {
        YipeeTableGDX copy = new YipeeTableGDX();
        copyParent(copy);
        copy.setAccessType(accessType);
        copy.setRated(isRated);
        copy.setSound(isSoundOn);
        return copy;
    }

    @Override
    public YipeeTableGDX deepCopy() {
        YipeeTableGDX copy = copy();
        ObjectSet<YipeeSeatGDX> copiedSeats = GdxSets.newSet();
        for (YipeeSeatGDX seat : seats) {
            copiedSeats.add(seat.deepCopy());
        }
        copy.setSeats(copiedSeats);
        copy.setWatchers(GdxSets.newSet(watchers));
        return copy;
    }
}
