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

import asg.games.yipee.common.dto.NetYipeePlayer;
import asg.games.yipee.common.enums.Copyable;
import com.badlogic.gdx.utils.Disposable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a LibGDX-compatible version of a Yipee player.
 * <p>
 * Encapsulates player-specific data such as rating, icon, name, and key bindings.
 * Provides methods for JSON serialization, deserialization, copying, and rating management.
 * Implements {@link Disposable} to clean up input configuration when no longer needed.
 * </p>
 *
 * <p>This class is designed for use in the LibGDX client layer and interacts with
 * networked data via {@link NetYipeePlayer}.</p>
 *
 * @author Blakbro2k
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class YipeePlayerGDX extends AbstractYipeeObjectGDX implements Copyable<YipeePlayerGDX>, Disposable, NetYipeePlayer {
    /**
     * Default player rating value (Elo-style)
     */
    public static final int DEFAULT_RATING_NUMBER = 1500;

    /**
     * Default icon index
     */
    public static final int DEFAULT_ICON_NUMBER = 1;

    /** Player’s skill rating used for matchmaking or rank display */
    private int rating;

    /** Player avatar or badge index */
    private int icon;

    /**
     * Default constructor for serialization and framework use
     */
    public YipeePlayerGDX() {
    }

    /**
     * Constructs a player with a name and default rating/icon.
     * @param name the player's name
     */
    public YipeePlayerGDX(String name) {
        this(name, DEFAULT_RATING_NUMBER, DEFAULT_ICON_NUMBER);
    }

    /**
     * Constructs a player with a name and specific rating.
     * @param name the player's name
     * @param rating the initial rating
     */
    public YipeePlayerGDX(String name, int rating) {
        this(name, rating, DEFAULT_ICON_NUMBER);
    }

    /**
     * Constructs a player with name, rating, and icon.
     * @param name the player's name
     * @param rating the initial rating
     * @param icon the icon index
     */
    public YipeePlayerGDX(String name, int rating, int icon) {
        this();
        setName(name);
        setRating(rating);
        setIcon(icon);
    }

    /**
     * Increases the player's rating by a non-negative value.
     *
     * @param inc amount to add to rating
     */
    public void increaseRating(int inc) {
        if (inc < 0) throw new IllegalArgumentException("Increment must be non-negative.");
        rating += inc;
    }

    /**
     * Decreases the player's rating by a non-negative value, bounded at 0.
     *
     * @param dec amount to subtract from rating
     */
    public void decreaseRating(int dec) {
        if (dec < 0) throw new IllegalArgumentException("Decrement must be non-negative.");
        rating = Math.max(0, rating - dec);
    }

    /**
     * Creates a shallow copy of the player (excluding deep config).
     *
     * @return a new {@link YipeePlayerGDX} with basic fields copied
     */
    @Override
    public YipeePlayerGDX copy() {
        YipeePlayerGDX copy = new YipeePlayerGDX();
        copyParent(copy);
        copy.setRating(this.rating);
        copy.setIcon(this.icon);
        return copy;
    }

    /**
     * Creates a deep copy of the player including key configuration.
     *
     * @return a deep-cloned {@link YipeePlayerGDX} instance
     */
    @Override
    public YipeePlayerGDX deepCopy() {
        return copy();
    }

    /**
     * Releases resources used by this object.
     * Currently disposes of the key configuration, if present.
     */
    @Override
    public void dispose() {
    }
}
