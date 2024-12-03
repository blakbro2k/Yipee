package asg.games.yipee.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_PLAYERS")
public class YipeePlayer extends AbstractYipeeObject implements Copyable<YipeePlayer>, Disposable {
    @JsonIgnore
    public final static int DEFAULT_RATING_NUMBER = 1500;

    private int rating;
    private int icon;

    @JsonProperty("rooms")
    @ManyToMany
    @JoinTable(name = "YT_PLAYER_ROOM_IDX",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Set<YipeeRoom> rooms = new LinkedHashSet<>();

    @JsonProperty("watching")
    @ManyToMany
    @JoinTable(name = "YT_PLAYER_TABLE_IDX",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id"))
    private Set<YipeeTable> watching = new LinkedHashSet<>();

    //Empty Constructor required for Json.Serializable
    public YipeePlayer() {
    }

    public YipeePlayer(String name) {
        this(name, DEFAULT_RATING_NUMBER, 1);
    }

    public YipeePlayer(String name, int rating) {
        this(name, rating, 1);
    }

    public YipeePlayer(String name, int rating, int icon){
        this();
        setName(name);
        setRating(rating);
        setIcon(icon);
    }

    public void increaseRating(int inc) {
        rating += inc;
    }

    public void decreaseRating(int dec) {
        rating -= dec;
    }

    public boolean addRoom(YipeeRoom room) {
        if (room == null) return false;
        return rooms.add(room);
    }

    public boolean removeRoom(YipeeRoom room) {
        if (room == null) return false;
        return rooms.remove(room);
    }

    public boolean addWatcher(YipeeTable tableToWatch) {
        if (tableToWatch == null) return false;
        return watching.add(tableToWatch);
    }

    public boolean removeWatcher(YipeeTable tableToWatch) {
        if (tableToWatch == null) return false;
        return watching.remove(tableToWatch);
    }

    public long watchingCount() {
        return watching.stream().count();
    }

    public long roomsCount() {
        return rooms.stream().count();
    }

    public void clearWatchers() {
        watching.clear();
    }

    public void clearRooms() {
        rooms.clear();
    }

    @Override
    public YipeePlayer copy() {
        YipeePlayer copy = new YipeePlayer();
        copyParent(copy);
        copy.setRating(this.rating);
        copy.setIcon(this.icon);
        return copy;
    }

    @Override
    public YipeePlayer deepCopy() {
        YipeePlayer copy = copy();
        copy.setRooms(this.getRooms());
        copy.setWatching(this.getWatching());
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeePlayer that = (YipeePlayer) o;
        return rating == that.rating && icon == that.icon && Objects.equals(rooms, that.rooms) && Objects.equals(watching, that.watching);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rating, icon, rooms, watching);
    }

    @Override
    public void dispose() {
        clearRooms();
        clearWatchers();
    }
}