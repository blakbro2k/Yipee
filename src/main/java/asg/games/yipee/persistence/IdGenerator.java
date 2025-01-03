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
package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeeObject;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

public class IdGenerator extends UUIDGenerator {

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