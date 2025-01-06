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

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
    private YipeeKeyMap keys = new YipeeKeyMap();

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
        copy.setKeys(this.keys);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeePlayer that = (YipeePlayer) o;
        return rating == that.rating && icon == that.icon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rating, icon);
    }

    @Override
    public void dispose() {
        //clearRooms();
        //clearWatchers();
    }
}