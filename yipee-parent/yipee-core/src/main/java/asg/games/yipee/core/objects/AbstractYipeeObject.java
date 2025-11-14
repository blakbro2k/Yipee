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

import asg.games.yipee.common.enums.YipeeObject;
import asg.games.yipee.core.tools.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Abstract base class for all Yipee game objects. This class implements the {@link YipeeObject} interface
 * and provides common properties and methods shared by all game objects in the Yipee system.
 * <br>
 * It includes an {@code id}, {@code name}, and timestamps for creation and modification. Subclasses will inherit
 * these properties and may extend them with specific game logic.
 * <br>
 * <br>
 * This class is mapped to a database table as a superclass for other entities in the Yipee game system, such as
 * {@link YipeeBlock}, {@link YipeePlayer}, and others.
 * <br>
 *
 * @author Blakbro2k
 * @version 1.0
 * <br>
 * @see YipeeObject
 * @see YipeeBlock
 * @see YipeeBlockMove
 * @see YipeeBoardPair
 * @see YipeeBrokenBlock
 * @see YipeeClock
 * @see YipeeGameBoardState
 * @see YipeePiece
 * @see YipeePlayer
 * @see YipeeRoom
 * @see YipeeSeat
 * @see YipeeTable
 */
@Setter
@Getter
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    property = "@class"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = YipeeBlock.class, name = "YipeeBlock"),
    @JsonSubTypes.Type(value = YipeeBlockMove.class, name = "YipeeBlockMove"),
    @JsonSubTypes.Type(value = YipeeBoardPair.class, name = "YipeeBoardPair"),
    @JsonSubTypes.Type(value = YipeeBrokenBlock.class, name = "YipeeBrokenBlock"),
    @JsonSubTypes.Type(value = YipeeClock.class, name = "YipeeClock"),
    @JsonSubTypes.Type(value = YipeeGameBoardState.class, name = "YipeeGameBoardState"),
    @JsonSubTypes.Type(value = YipeePiece.class, name = "YipeePiece"),
    @JsonSubTypes.Type(value = YipeePlayer.class, name = "YipeePlayer"),
    @JsonSubTypes.Type(value = YipeeRoom.class, name = "YipeeRoom"),
    @JsonSubTypes.Type(value = YipeeSeat.class, name = "YipeeSeat"),
    @JsonSubTypes.Type(value = YipeeTable.class, name = "YipeeTable")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@MappedSuperclass
public abstract class AbstractYipeeObject implements YipeeObject {
    private static final Logger logger = LoggerFactory.getLogger(AbstractYipeeObject.class);

    /**
     * Unique identifier for the Yipee object
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 32)
    @JsonProperty()
    protected String id;

    /**
     * Name of the Yipee object, must be unique
     */
    @Column(name = "name", nullable = false, unique = true)
    protected String name;

    /**
     * Timestamp representing the creation time of the Yipee object (in milliseconds)
     */
    /**
     * Automatically set when the entity is first persisted.
     */
    @Column(name = "created", nullable = false, updatable = false)
    protected long created;

    /**
     * Timestamp representing the last modification time of the Yipee object (in milliseconds)
     */
    @Column(name = "modified", nullable = false)
    protected long modified;

    /**
     * Default constructor, JPA needs a non-public no-arg constructor.
     */
    protected AbstractYipeeObject() {
    }

    @PrePersist
    protected void onPrePersist() {
        final long now = TimeUtils.millis();

        // 1) Ensure ID is present and normalized to 32 chars (no hyphens)
        if (this.id == null || this.id.isEmpty()) {
            this.id = java.util.UUID.randomUUID().toString().replace("-", "").toLowerCase();
        } else {
            this.id = this.id.replace("-", "").toLowerCase();
            if (this.id.length() != 32) {
                throw new IllegalArgumentException("ID must be 32 hex chars; was: " + this.id);
            }
        }

        // 2) Timestamps
        if (this.created <= 0) this.created = now;
        this.modified = now;
    }

    @PreUpdate
    protected void onPreUpdate() {
        this.modified = TimeUtils.millis();
    }

    /**
     * Compares this Yipee object with another object for equality. Two objects are considered equal if they
     * have the same {@code id} and {@code name}.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbstractYipeeObject that = (AbstractYipeeObject) o;
        // If either ID is null, fall back to object identity (unsaved entities are not equal).
        return this.id != null && Objects.equals(this.id, that.id);
    }

    /**
     * Generates a hash code for this Yipee object. The hash code is based on the {@code id}, {@code name},
     * {@code created}, and {@code modified} properties.
     *
     * @return the hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(Hibernate.getClass(this), id);
    }

    /**
     * Returns a string representation of this Yipee object, including its class name, {@code id}, and {@code name}.
     *
     * @return a string representation of the Yipee object
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + this.getId() + "," + this.getName() + "]";
    }

    /**
     * Copies common properties from the parent Yipee object to another Yipee object.
     * This method ensures that the {@code id}, {@code name}, {@code created}, and {@code modified} fields are copied,
     * but the {@code id} is set to {@code null} to indicate it is a new instance.
     *
     * @param o the Yipee object to copy properties to
     */
    protected void copyParent(YipeeObject o) {
        if (o != null) {
            logger.debug("Copying parent attributes to: " + o);
            o.setId(null);
            o.setName(this.getName());
            long now = TimeUtils.millis();
            o.setCreated(now);
            o.setModified(now);
        }
    }
}
