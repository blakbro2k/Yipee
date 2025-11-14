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
package asg.games.yipee.core.objects;

import asg.games.yipee.common.dto.NetYipeeKeyMap;
import asg.games.yipee.common.dto.NetYipeePlayer;
import asg.games.yipee.common.enums.Copyable;
import asg.games.yipee.common.enums.Disposable;
import asg.games.yipee.core.persistence.Updatable;
import asg.games.yipee.core.tools.NetUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents a player in the Yipee game. Each player has a rating and an icon.
 * The player's rating determines their skill level, and the icon represents the player's avatar.
 * <p>
 * This class also contains the configuration for the player's keys
 * <p>
 * Created by Blakbro2k on 1/28/2018.
 * Updated for enhanced documentation and clarity.
 *
 * @see YipeeKeyMap
 */
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_PLAYERS")
public class YipeePlayer extends AbstractYipeeObject implements Copyable<YipeePlayer>, Updatable<YipeePlayer>, Disposable, NetYipeePlayer {
    private static final Logger logger = LoggerFactory.getLogger(YipeePlayer.class);

    @JsonIgnore
    public final static int DEFAULT_RATING_NUMBER = 1500;
    @JsonIgnore
    public final static int DEFAULT_ICON_NUMBER = 1;

    @Column(name = "rating", nullable = false, columnDefinition = "INT DEFAULT 1500")
    private int rating;

    @Column(name = "icon", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int icon;

    @Lob
    @Column(name = "serialized_key_config", columnDefinition = "TEXT")
    @JsonIgnore
    private String serializedKeyConfig;

    @Transient
    private NetYipeeKeyMap keyConfig = new YipeeKeyMap(this.getId());

    /**
     * Default constructor required for JSON serialization/deserialization.
     */
    public YipeePlayer() {
    }

    /**
     * Creates a new {@code YipeePlayer} with the specified name, default rating, and default icon.
     *
     * @param name the player's name
     */
    public YipeePlayer(String name) {
        this(name, DEFAULT_RATING_NUMBER, DEFAULT_ICON_NUMBER);
    }

    /**
     * Creates a new {@code YipeePlayer} with the specified name and rating, using the default icon.
     *
     * @param name the player's name
     * @param rating the player's rating
     */
    public YipeePlayer(String name, int rating) {
        this(name, rating, DEFAULT_ICON_NUMBER);
    }

    /**
     * Creates a new {@code YipeePlayer} with the specified name, rating, and icon.
     *
     * @param name the player's name
     * @param rating the initial skill rating
     * @param icon the player's avatar icon ID
     */
    public YipeePlayer(String name, int rating, int icon) {
        this();
        setName(name);
        setRating(rating);
        setIcon(icon);
    }

    /**
     * Gets the {@code YipeeKeyMap} which holds the player key configuration map
     * @return
     */
    public NetYipeeKeyMap getKeyConfig() {
        if (keyConfig == null && serializedKeyConfig != null) {
            synchronized (this) {
                if (keyConfig == null) {
                    try {
                        keyConfig = NetUtil.readValue(serializedKeyConfig, YipeeKeyMap.class);
                        // If playerId was not set at creation, patch it here
                        if (keyConfig.getPlayerId() == null && getId() != null) {
                            keyConfig.setPlayerId(getId());
                        }
                    } catch (JsonProcessingException e) {
                        logger.error("Failed to deserialize keyConfig", e);
                        throw new RuntimeException("Failed to deserialize keyConfig", e);
                    }
                }
            }
        }
        return keyConfig;
    }

    /**
     * Sets the {@code YipeeKeyMap} which holds the player key configuration map
     * @param keyConfig
     */
    public void setKeyConfig(NetYipeeKeyMap keyConfig) {
        if (keyConfig != null && keyConfig.getPlayerId() == null && getId() != null) {
            keyConfig.setPlayerId(getId());
        }
        this.keyConfig = keyConfig;
        try {
            this.serializedKeyConfig = NetUtil.writeValueAsString(keyConfig);
            logger.debug("Serialized keyConfig: {}", serializedKeyConfig);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize keyConfig", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Increases the player's rating by the specified amount.
     *
     * @param inc the amount to add to the current rating (must be non-negative)
     * @throws IllegalArgumentException if {@code inc} is negative
     */
    public void increaseRating(int inc) {
        if (inc < 0) throw new IllegalArgumentException("Increment must be non-negative.");
        logger.debug("Increasing current rating:[{}] by {}", rating, inc);
        rating += inc;
        logger.debug("rating={}", rating);
    }

    /**
     * Decreases the player rating by a given amount
     *
     * @param dec
     */
    public void decreaseRating(int dec) {
        if (dec < 0) throw new IllegalArgumentException("Decrement must be non-negative.");
        logger.debug("Decreasing current rating:[{}] by {}", rating, dec);
        rating = Math.max(0, rating - dec);
        logger.debug("rating={}", rating);
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
        copy.setKeyConfig(this.getKeyConfig() != null ? this.getKeyConfig().deepCopy() : null);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YipeePlayer)) return false;
        if (!super.equals(o)) return false;
        YipeePlayer player = (YipeePlayer) o;
        return rating == player.rating && icon == player.icon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rating, icon, name);
    }

    /**
     * Disposes of internal resources such as the key configuration.
     * Called when this player instance is no longer needed.
     */
    @Override
    public void dispose() {
        keyConfig.dispose();
    }

    @Override
    public void updateFrom(YipeePlayer source) {
        if (source != null) {
            setName(source.getName());
            setRating(source.getRating());
            setIcon(source.getIcon());
        }
    }
}
