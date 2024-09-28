package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeeTable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.ResultSet;
import java.sql.SQLException;


public class YokelTableType extends YokelUserType {
    @Override
    public Class<YipeeTable> returnedClass() {
        return YipeeTable.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        //id	created	modified	name	access_type	rated	sound	room
        YipeeTable result = null;
        String id = rs.getString(names[0]);
        long created = rs.getLong(names[1]);
        long modified = rs.getLong(names[2]);
        String name = rs.getString(names[3]);
        String accessType = rs.getString(names[4]);
        boolean rated = rs.getBoolean(names[5]);
        boolean sound = rs.getBoolean(names[6]);
        String roomId = rs.getString(names[7]);

        if (!rs.wasNull()) {
            result = new YipeeTable();
            result.setId(id);
            result.setName(name);
            result.setCreated(created);
            result.setModified(modified);
            result.setAccessType(accessType);
            result.setRated(rated);
            result.setSound(sound);
            //session.getPersistenceContext().getSession().get
            //result.setRoom(roomId);
        }
        return result;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        YipeeTable copy;

        if (value == null) {
            copy = null;
        } else if (!(value instanceof YipeeTable)) {
            copy = null;
        } else {
            copy = ((YipeeTable) value).deepCopy();
        }
        return copy;
    }
}
