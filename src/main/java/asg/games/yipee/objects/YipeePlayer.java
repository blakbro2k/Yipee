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
 * Represents a player in Yipee.  The player has a rating and an icon represented by an int
 * <p>
 * Created by Blakbro2k on 1/28/2018.
 */
@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "YT_PLAYERS")
public class YipeePlayer extends AbstractYipeeObject implements YipeeObjectJPAVisitor, Copyable<YipeePlayer>, Disposable {
    @Transient
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
    private YipeeKeyMap keyConfig = new YipeeKeyMap();

    /**
     * Default constructor required for JSON serialization/deserialization.
     */
    public YipeePlayer() {
    }

    /**
     * Creates a new YipeePlayer with default rating and defaut icon
     * @param name
     */
    public YipeePlayer(String name) {
        this(name, DEFAULT_RATING_NUMBER, DEFAULT_ICON_NUMBER);
    }

    /**
     * Creates a new @{link YipeePlayer} given a starting rating
     *
     * @param name
     * @param rating
     */
    public YipeePlayer(String name, int rating) {
        this(name, rating, DEFAULT_ICON_NUMBER);
    }

    /**
     * Creates a new @{link YipeePlayer} given a starting rating and icon
     *
     * @param name
     * @param rating
     * @param icon
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
    public YipeeKeyMap getKeyConfig() {
        if (keyConfig == null && serializedKeyConfig != null) {
            synchronized (this) {
                if (keyConfig == null) {
                    try {
                        keyConfig = Util.readValue(serializedKeyConfig, YipeeKeyMap.class);
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
    public void setKeyConfig(YipeeKeyMap keyConfig) {
        this.keyConfig = keyConfig;
        try {
            this.serializedKeyConfig = Util.writeValueAsString(keyConfig);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize keyConfig  in getKeysConfig()", e);
            throw new RuntimeException("Failed to serialize keyConfig in getKeysConfig()", e);
        }
    }

    /**
     * Increases the player rating by a given amount
     *
     * @param inc
     */
    public void increaseRating(int inc) {
        rating += inc;
    }

    /**
     * Decreases the player rating by a given amount
     *
     * @param dec
     */
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
        copy.setKeyConfig(this.getKeyConfig() != null ? this.getKeyConfig().deepCopy() : null);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YipeePlayer player)) return false;
        if (!super.equals(o)) return false;
        return rating == player.rating && icon == player.icon && serializedKeyConfig.equals(player.serializedKeyConfig) && keyConfig.equals(player.keyConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rating, icon, serializedKeyConfig, keyConfig);
    }

    @Override
    public void visitSave(YipeeStorageAdapter adapter) {
        try {
            if (adapter != null) {
                adapter.visitYipeePlayer(this);
            }
        } catch (Exception e) {
            logger.error("Issue visiting save for " + this.getClass().getSimpleName(), e);
            throw new RuntimeException("Issue visiting save for " + this.getClass().getSimpleName(), e);
        }
    }

    @Override
    public void dispose() {
        keyConfig.dispose();
    }
}