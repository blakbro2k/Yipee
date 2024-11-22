package asg.games.yipee.persistence;

/*
public class IdGenerator extends UUIDGenerator {

	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		Serializable id = null;
		if(object instanceof YipeeObject){
			id = ((YipeeObject) object).getId();
		}
		
		if(id == null) {
			id = String.valueOf(super.generate(session, object)).replaceAll("-", "");
		}
		return id;
	}
}*/