package asg.games.yipee.persistence;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class DateType implements UserType {
	private static final int SQL_TYPE = Types.BIGINT;

	public DateType() {
		//log.debug("Adding type registration DateType -> " + this);
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return this.deepCopy(cached);
	}

	@Override
	public Object deepCopy(Object o) throws HibernateException {
			Date dateType;
			
			if(o == null){
				dateType = null;
			} else if (!(o instanceof Date)){
				dateType = null;
			} else {
				Date date = (Date) o;
				dateType = new Date(date.getTime());

			}
			return dateType;
	}

	@Override
	public Serializable disassemble(Object o) throws HibernateException {
		return (Serializable) this.deepCopy(o);
	}

	@Override
	public boolean equals(Object o1, Object o2) throws HibernateException {
		boolean eq = false;
		if (o1 == o2) {
			eq = true;
		} else if (o1 instanceof Date && o2 instanceof Date) {
			Date d1 = (Date) o1;
			Date d2 = (Date) o2;
			eq = d1.getTime() == d2.getTime();
			
		}
		return eq;
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return arg0 == null ? 0 : arg0.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		Date result = null;
		
			long l = rs.getLong(names[0]);
			if(!rs.wasNull()){
				result = new Date(l);
			}
	
		return result;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if(value == null){
			st.setNull(index, SQL_TYPE );
		} else if (!(value instanceof Date)){
			st.setNull(index, SQL_TYPE );
		} else {
			Date d = (Date) value;
			st.setLong(index, d.getTime());
		}
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public Class<Date> returnedClass() {
		return Date.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[]{SQL_TYPE};
	}

}
