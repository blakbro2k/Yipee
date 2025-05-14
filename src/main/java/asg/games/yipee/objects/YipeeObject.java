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
package asg.games.yipee.objects;


import asg.games.yipee.net.YipeeSerializable;

/**
 * A game object
 *
 * @author Blakbro2k
 */
public interface YipeeObject extends YipeeSerializable {
    /**
     * Sets the Object ID
     *
     * @param id String
     */
    void setId(String id);

    /**
     * @return Id String
     */
    String getId();

    /**
     * Sets the Object Name
     *
     * @param name String
     */
    void setName(String name);

    /**
     * Returns the Object Name
     *
     * @return name String
     */
    String getName();

    /**
     * Sets the Object created date
     *
     * @param dateTime long string
     */
    void setCreated(long dateTime);

    /**
     * Returns the Object created date
     *
     * @return date created long
     */
    long getCreated();

    /**
     * Sets the Object modified date
     *
     * @param dateTime long string
     */
    void setModified(long dateTime);

    /**
     * Returns the Object modified date
     *
     * @return date modified long string
     */
    long getModified();
}
