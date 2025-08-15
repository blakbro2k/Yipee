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

import asg.games.yipee.common.net.NetYipeePlayer;
import asg.games.yipee.libgdx.tools.NetUtil;
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

    /** Serialized form of the player's key mapping, used for network transmission */
    private String serializedKeyConfig;

    /** Deserialized form of the player's key mapping, used by the client for input */
    private YipeeKeyMapGDX keyConfig = new YipeeKeyMapGDX(this.getId());

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
     * Lazily initializes and returns the player's key configuration.
     * If the deserialized key config is missing, attempts to construct from the JSON string.
     *
     * @return player's key configuration
     */
    public YipeeKeyMapGDX getKeyConfig() {
        if (keyConfig == null && serializedKeyConfig != null) {
            synchronized (this) {
                if (keyConfig == null) {
                    try {
                        keyConfig = NetUtil.fromJsonClient(serializedKeyConfig, YipeeKeyMapGDX.class);
                        if (keyConfig.getPlayerId() == null && getId() != null) {
                            keyConfig.setPlayerId(getId());
                        }
                    } catch (RuntimeException e) {
                        throw new RuntimeException("Failed to deserialize keyConfig", e);
                    }
                }
            }
        }
        return keyConfig;
    }

    /**
     * Sets the player's key configuration and automatically serializes it.
     *
     * @param keyConfig the key map to assign
     */
    public void setKeyConfig(YipeeKeyMapGDX keyConfig) {
        if (keyConfig != null && keyConfig.getPlayerId() == null && getId() != null) {
            keyConfig.setPlayerId(getId());
        }
        this.keyConfig = keyConfig;
        try {
            this.serializedKeyConfig = (keyConfig != null) ? NetUtil.toJsonClient(keyConfig) : null;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to serialize keyConfig", e);
        }
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
        YipeePlayerGDX copy = copy();
        copy.setKeyConfig(this.getKeyConfig() != null ? this.getKeyConfig().deepCopy() : null);
        return copy;
    }

    /**
     * Releases resources used by this object.
     * Currently disposes of the key configuration, if present.
     */
    @Override
    public void dispose() {
        if (keyConfig != null) {
            keyConfig.dispose();
        }
    }
}
