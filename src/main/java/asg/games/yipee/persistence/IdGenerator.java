package asg.games.yipee.persistence;

import asg.games.yipee.objects.YokelObject;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

public class IdGenerator extends UUIDGenerator {

	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		Serializable id = null;
		if(object instanceof YokelObject){
			id = ((YokelObject) object).getId();
		}
		
		if(id == null) {
			id = String.valueOf(super.generate(session, object)).replaceAll("-", "");
		}
		return id;
	}
}