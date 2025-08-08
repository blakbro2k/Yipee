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
package asg.games.yipee.core.persistence;

import asg.games.yipee.core.objects.YipeeObject;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Custom ID generator for Hibernate entities that extends {@link UUIDGenerator}.
 * <p>
 * This generator will reuse an existing ID from a {@link YipeeObject} if present,
 * or generate a new UUID (with dashes removed) if no ID is set.
 * </p>
 *
 * <p>
 * This is useful for preserving object identity during serialization or replication,
 * where entities may already carry a pre-assigned UUID.
 * </p>
 *
 * @author Blakbro2k
 */
public class IdGenerator extends UUIDGenerator {
    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    /**
     * Generates a unique identifier for a Hibernate entity.
     * <ul>
     *     <li>If the object is an instance of {@link YipeeObject} and already has a non-null ID, it reuses it.</li>
     *     <li>Otherwise, a new UUID is generated and formatted by removing dashes.</li>
     * </ul>
     *
     * @param session the Hibernate session
     * @param object  the entity object to generate an ID for
     * @return the existing or newly generated UUID string (without dashes)
     * @throws HibernateException if ID generation fails
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Serializable id = null;

        if (object instanceof YipeeObject) {
            id = ((YipeeObject) object).getId();
        }

        if (id == null) {
            id = String.valueOf(super.generate(session, object)).replaceAll("-", "");
        }

        return id;
    }
}
