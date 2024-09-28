package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeeRoom;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class YokelRoomType extends YokelUserType {
    @Override
    public Class<YipeeRoom> returnedClass() {
        return YipeeRoom.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {

        //id	created	modified	name	lounge_name
        YipeeRoom result = null;
        String id = rs.getString(names[0]);
        long created = rs.getLong(names[1]);
        long modified = rs.getLong(names[2]);
        String name = rs.getString(names[3]);
        String lounge_name = rs.getString(names[4]);


        if (!rs.wasNull()) {
            result = new YipeeRoom();
            result.setId(id);
            result.setName(name);
            result.setCreated(created);
            result.setModified(modified);
            result.setLoungeName(lounge_name);
        }
        return result;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        YipeeRoom copy;

        if (value == null) {
            copy = null;
        } else if (!(value instanceof YipeeRoom)) {
            copy = null;
        } else {
            copy = ((YipeeRoom) value).deepCopy();
        }
        return copy;
    }
}
