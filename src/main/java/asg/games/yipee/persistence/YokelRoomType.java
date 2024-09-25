package asg.games.yipee.persistence;

import asg.games.yipee.objects.YokelRoom;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class YokelRoomType extends YokelUserType {
    @Override
    public Class<YokelRoom> returnedClass() {
        return YokelRoom.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {

        //id	created	modified	name	lounge_name
        YokelRoom result = null;
        String id = rs.getString(names[0]);
        long created = rs.getLong(names[1]);
        long modified = rs.getLong(names[2]);
        String name = rs.getString(names[3]);
        String lounge_name = rs.getString(names[4]);


        if (!rs.wasNull()) {
            result = new YokelRoom();
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
        YokelRoom copy;

        if (value == null) {
            copy = null;
        } else if (!(value instanceof YokelRoom)) {
            copy = null;
        } else {
            copy = ((YokelRoom) value).deepCopy();
        }
        return copy;
    }
}
