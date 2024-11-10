/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package asg.games.yipee.objects;

import asg.games.yipee.tools.TimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Base Abstract class for any Yipee Game objects
 * implements YipeeObject interface
 *
 * @author Blakbro2k
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = YipeeSeat.class, name = "YipeeSeat"),
        @JsonSubTypes.Type(value = YipeeClock.class, name = "YipeeClock"),
        @JsonSubTypes.Type(value = YipeeRoom.class, name = "YipeeRoom"),
        @JsonSubTypes.Type(value = YipeeTable.class, name = "YipeeTable")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractYipeeObject implements YipeeObject {
    @Id
    @GeneratedValue(generator = "asg.games.yipee.persistence.IdGenerator")
    @GenericGenerator(name = "uuid_gen_strategy_class",
            parameters = @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy"),
            strategy = "asg.games.yokel.persistence.IdGenerator")
    @Column(name = "id", nullable = false, length=32)

    @JsonProperty()
    protected String id;
    protected String name;
    protected long created;
    protected long modified;

    AbstractYipeeObject(){
        setCreated(TimeUtils.millis());
        setModified(TimeUtils.millis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractYipeeObject object = (AbstractYipeeObject) o;
        return getCreated() == object.getCreated() && getModified() == object.getModified() && Objects.equals(getId(), object.getId()) && Objects.equals(getName(), object.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCreated(), getModified());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + this.getId() + "," + this.getName() + "]";
    }

    /**
     * Sets the unique UUID of the object
     *
     * @param id String
     */
    public void setId(String id){ this.id = id;}

    /**
     * Returns the unique UUID of the object
     * @return
     */
    public String getId(){ return id;}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setCreated(long dateTime) {
        this.created = dateTime;
    }

    public long getCreated() {
        return created;
    }

    public void setModified(long dateTime) {
        this.modified = dateTime;
    }

    public long getModified() {
        return modified;
    }

    protected void copyParent(YipeeObject o){
        if(o != null) {
            o.setId(this.getId());
            o.setName(this.getName());
            o.setCreated(this.getCreated());
            o.setModified(this.getModified());
        }
    }
}
