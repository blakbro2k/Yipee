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

import asg.games.yipee.persistence.YipeeObjectJPAVisitor;
import asg.games.yipee.persistence.YipeeStorageAdapter;
import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_TABLES")
@JsonIgnoreProperties({"tableNumber", "tableStartReady", "upArguments", "tableName", "roomId"})
public class YipeeTable extends AbstractYipeeObject implements YipeeObjectJPAVisitor, Copyable<YipeeTable>, Disposable {
    @JsonIgnore
    public static final String ARG_TYPE = "type";
    @JsonIgnore
    public static final String ARG_RATED = "rated";
    @JsonIgnore
    public static final String ARG_SOUND = "sound";
    @JsonIgnore
    public static final String ENUM_VALUE_PRIVATE = "PRIVATE";
    @JsonIgnore
    public static final String ENUM_VALUE_PUBLIC = "PUBLIC";
    @JsonIgnore
    public static final String ENUM_VALUE_PROTECTED = "PROTECTED";
    @JsonIgnore
    public static final String ATT_NAME_PREPEND = "#";
    @JsonIgnore
    public static final String ATT_TABLE_SPACER = "_room_tbl";
    @JsonIgnore
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

    @OneToMany(mappedBy = "parentTable", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonManagedReference
    private Set<YipeeSeat> seats = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "watching")
    //@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonBackReference
    private Set<YipeePlayer> watchers = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_room_id", nullable = false)
    //@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonBackReference
    private YipeeRoom parentRoom;

    @JsonProperty("rated")
    private boolean isRated = false;

    @JsonProperty("soundOn")
    private boolean isSoundOn = true;

    public void setParentRoom(YipeeRoom parentRoom) {
        this.parentRoom = parentRoom;
    }

    public YipeeRoom getParentRoom() {
        return parentRoom;
    }

    //Empty Constructor required for Json.Serializable
    public YipeeTable() {
    }

    public YipeeTable(YipeeRoom parentRoom, int nameNumber) {
        this(parentRoom, nameNumber, null);
    }

    public YipeeTable(YipeeRoom parentRoom, int nameNumber, Map<String, Object> arguments) {
        this.parentRoom = parentRoom;
        initialize(nameNumber, arguments);
    }

    private void initialize(int nameNumber, Map<String, Object> arguments) {
        setTableName(nameNumber);
        setUpSeats();
        setUpArguments(arguments);
    }

    public void setTableName(int tableNumber) {
        setName(parentRoom.getName() + ATT_TABLE_SPACER + ATT_NAME_PREPEND + tableNumber);
    }

    private String getParentRoomId() {
        String roomId = "_NoRoomId_";
        if (parentRoom != null) {
            roomId = parentRoom.getId();
        }
        return roomId;
    }

    public int getTableNumber() {
        return Integer.parseInt(Util.split(getName(), ATT_NAME_PREPEND)[1]);
    }

    private void setUpArguments(Map<String, Object> arguments) {
        if (arguments != null) {
            for (String key : Util.getMapKeys(arguments)) {
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
            if (Util.equalsIgnoreCase(ARG_TYPE, arg)) {
                setAccessType(Util.otos(value));
            } else if (Util.equalsIgnoreCase(ARG_RATED, arg)) {
                setRated(Util.otob(value));
            } else if (Util.equalsIgnoreCase(ARG_SOUND, arg)) {
                setSound(Util.otob(value));
            }
        }
    }

    public void setAccessType(ACCESS_TYPE accessType) {
        this.accessType = accessType;
    }

    public void setAccessType(String accessType) {
        if (Util.equalsIgnoreCase(ACCESS_TYPE.PRIVATE.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PRIVATE);
        } else if (Util.equalsIgnoreCase(ACCESS_TYPE.PROTECTED.toString(), accessType)) {
            setAccessType(ACCESS_TYPE.PROTECTED);
        } else {
            setAccessType(ACCESS_TYPE.PUBLIC);
        }
    }

    public ACCESS_TYPE getAccessType() {
        return accessType;
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

    public boolean isSeatReady(YipeeSeat seat) {
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
            seats.add(new YipeeSeat(this, i));
        }
    }

    public void makeAllTablesUnready() {
        for (YipeeSeat seat : Util.safeIterable(seats)) {
            if (seat != null) {
                seat.setSeatReady(false);
            }
        }
    }

    public Set<YipeeSeat> getSeats() {
        return seats;
    }

    public void setSeats(Set<YipeeSeat> seats) {
        this.seats = seats;
    }

    public YipeeSeat getSeat(int seatNum) {
        return Util.getIndexOfSet(seats, seatNum);
    }

    public void addWatcher(YipeePlayer player) {
        if (player != null) {
            watchers.add(player);
        }
    }

    public void removeWatcher(YipeePlayer player) {
        if (player != null) {
            watchers.remove(player);
        }
    }

    public void setWatchers(Set<YipeePlayer> watchers) {
        this.watchers = watchers;
    }

    public Set<YipeePlayer> getWatchers() {
        return watchers;
    }

    @Override
    public void dispose() {
        Util.clearArrays(seats, watchers);
    }

    @Override
    public YipeeTable copy() {
        YipeeTable copy = new YipeeTable();
        copyParent(copy);
        copy.setAccessType(accessType);
        copy.setRated(isRated);
        copy.setSound(isSoundOn);
        copy.setParentRoom(parentRoom);
        return copy;
    }

    @Override
    public YipeeTable deepCopy() {
        YipeeTable copy = copy();
        copy.setSeats(seats);
        copy.setWatchers(watchers);
        return copy;
    }

    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try{
            if(adapter != null) {
                adapter.putAllPlayers(watchers);
                for (YipeeSeat seat : seats) {
                    if (seat != null) {
                        seat.setParentTable(this);
                    }
                }
                adapter.putAllSeats(seats);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName() + ": ", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeTable that = (YipeeTable) o;
        return isRated == that.isRated && isSoundOn == that.isSoundOn && accessType == that.accessType && Objects.equals(seats, that.seats) && Objects.equals(watchers, that.watchers) && Objects.equals(parentRoom, that.parentRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accessType, watchers, parentRoom, isRated, isSoundOn);
    }
}