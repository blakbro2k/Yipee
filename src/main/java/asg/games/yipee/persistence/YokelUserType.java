package asg.games.yipee.persistence;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import asg.games.yokel.objects.YokelObject;

abstract class YokelUserType implements UserType {
    protected static final int SQL_TYPE = Types.VARCHAR;

    @Override
    public int[] sqlTypes() {
        return new int[]{SQL_TYPE};
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        boolean eq = false;
        if (x == y) {
            eq = true;
        } else if (x instanceof YokelObject && y instanceof YokelObject) {
            YokelObject d1 = (YokelObject) x;
            YokelObject d2 = (YokelObject) y;
            eq = d1.equals(d2);
        }
        return eq;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public abstract Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException;

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if(value == null){
            st.setNull(index, SQL_TYPE );
        } else if (!(value instanceof YokelObject)){
            st.setNull(index, SQL_TYPE );
        } else {
            YokelObject d = (YokelObject) value;
            st.setString(index, d.getId());
        }
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) this.deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return this.deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
