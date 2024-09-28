package asg.games.yipee.objects;

import asg.games.yipee.persistence.YipeeStorageAdapter;
import asg.games.yipee.tools.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class YipeeTable extends AbstractYipeeObject implements YipeeObjectJPAVisitor, Copyable<YipeeTable>, Disposable {
    public static final String ARG_TYPE = "type";
    public static final String ARG_RATED = "rated";
    public static final String ENUM_VALUE_PRIVATE = "PRIVATE";
    public static final String ENUM_VALUE_PUBLIC = "PUBLIC";
    public static final String ENUM_VALUE_PROTECTED = "PROTECTED";
    public static final String ATT_NAME_PREPEND = "#";
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
    private List<YipeeSeat> seats = new LinkedList<>();
    private List<YipeePlayer> watchers = new ArrayList<>();
    private boolean isRated = false;
    private boolean isSoundOn = true;
    private String roomId;

    //Empty Constructor required for Json.Serializable
    public YipeeTable() {
        super();
    }

    public YipeeTable(String roomId, int nameNumber) {
        this(roomId, nameNumber, null);
    }

    public YipeeTable(String roomId, int nameNumber, Map<String, Object> arguments) {
        this();
        this.roomId = roomId;
        initialize(nameNumber, arguments);
    }

    private void initialize(int nameNumber, Map<String, Object> arguments) {
        setTableName(nameNumber);
        setUpSeats();
        setUpArguments(arguments);
    }

    public void setTableName(int tableNumber) {
        setName(roomId + ATT_NAME_PREPEND + tableNumber);
    }

    public int getTableNumber() {
        System.out.println(getName());
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
        if (isGroupReady(0)) {
            return isGroupReady(1) || isGroupReady(2) || isGroupReady(3);
        }
        if (isGroupReady(1)) {
            return isGroupReady(0) || isGroupReady(2) || isGroupReady(3);
        }
        if (isGroupReady(2)) {
            return isGroupReady(0) || isGroupReady(1) || isGroupReady(3);
        }
        if (isGroupReady(3)) {
            return isGroupReady(0) || isGroupReady(1) || isGroupReady(2);
        }
        return false;
    }

    private void setUpSeats() {
        for (int i = 0; i < MAX_SEATS; i++) {
            seats.add(new YipeeSeat(getName(), i));
        }
    }

    public void makeAllTablesUnready() {
        for (YipeeSeat seat : Util.safeIterable(seats)) {
            if (seat != null) {
                seat.setSeatReady(false);
            }
        }
    }

    public List<YipeeSeat> getSeats() {
        return seats;
    }

    public void setSeats(List<YipeeSeat> seats) {
        this.seats = seats;
    }

    public YipeeSeat getSeat(int seatNum) {
        return seats.get(seatNum);
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    private void setWatchers(List<YipeePlayer> watchers) {
        this.watchers = watchers;
    }

    public List<YipeePlayer> getWatchers() {
        return watchers;
    }

    @Override
    public void dispose() {
        Util.clearArrays(seats, watchers);
    }

    @Override
    public YipeeTable copy() {
        YipeeTable copy = new YipeeTable();
        copy.setName(this.name);
        copy.setRoomId(this.roomId);
        return copy;
    }

    @Override
    public YipeeTable deepCopy() {
        YipeeTable copy = copy();
        copyParent(copy);
        copy.setAccessType(accessType);
        copy.setRated(isRated);
        copy.setSound(isSoundOn);
        copy.setRoomId(roomId);
        copy.setSeats(seats);
        copy.setWatchers(watchers);
        return copy;
    }

    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try{
            if(adapter != null) {
                adapter.putAllPlayers(watchers);
                adapter.putAllSeats(seats);
            }
        } catch (Exception e) {
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName() + ": ", e);
        }
    }
/*
    @Override
    public void write(Json json) {
        super.write(json);
        if(json != null) {
            json.writeValue("accessType", accessType);
            json.writeValue("isRated", isRated);
            json.writeValue("isSoundOn", isSoundOn);
            json.writeValue("roomId", roomId);
            json.writeValue("seats", seats);
            json.writeValue("watchers", watchers);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void read(Json json, JsonValue jsonValue) {
        super.read(json, jsonValue);
        if (json != null) {
            accessType = json.readValue("accessType", ACCESS_TYPE.class, jsonValue);
            isRated = json.readValue("isRated", Boolean.class, jsonValue);
            isSoundOn = json.readValue("isSoundOn", Boolean.class, jsonValue);
            roomId = json.readValue("roomId", String.class, jsonValue);
            seats = json.readValue("seats", Array.class, jsonValue);
            watchers = json.readValue("watchers", Array.class, jsonValue);
        }
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeeTable that = (YipeeTable) o;
        return isRated() == that.isRated() && isSoundOn() == that.isSoundOn() && getAccessType() == that.getAccessType() && getSeats().equals(that.getSeats()) && getWatchers().equals(that.getWatchers()) && Objects.equals(getRoomId(), that.getRoomId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAccessType(), getSeats(), getWatchers(), isRated(), isSoundOn(), getRoomId());
    }
}