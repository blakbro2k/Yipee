package asg.games.yipee.persistence;

import asg.games.yipee.objects.YipeePlayer;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.ResultSet;
import java.sql.SQLException;


public class YokelPlayerType extends YokelUserType {
    @Override
    public Class<YipeePlayer> returnedClass() {
        return YipeePlayer.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        YipeePlayer result = null;
        //id	created	modified	name	icon	rating
        String id = rs.getString(names[0]);
        long created = rs.getLong(names[1]);
        long modified = rs.getLong(names[2]);
        String name = rs.getString(names[3]);
        int icon = rs.getInt(names[4]);
        int rating = rs.getInt(names[5]);

        if (!rs.wasNull()) {
            result = new YipeePlayer(name, rating, icon);
            result.setId(id);
            result.setCreated(created);
            result.setModified(modified);
        }

        return result;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        YipeePlayer copy;

        if (value == null) {
            copy = null;
        } else if (!(value instanceof YipeePlayer)) {
            copy = null;
        } else {
            copy = ((YipeePlayer) value).deepCopy();
        }
        return copy;
    }
}
