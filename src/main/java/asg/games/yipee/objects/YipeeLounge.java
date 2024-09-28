package asg.games.yipee.objects;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class YipeeLounge extends AbstractYipeeObject implements Disposable{
    public static final String SOCIAL_GROUP = "Social";
    public static final String BEGINNER_GROUP = "Beginner";
    public static final String INTERMEDIATE_GROUP = "Intermediate";
    public static final String ADVANCED_GROUP = "Advanced";
    public static final String DEFAULT_LOUNGE = "Default";

    final private Map<String, YipeeRoom> rooms = new HashMap<>();

    //Empty Constructor required for Json.Serializable
    public YipeeLounge(){
        super();
    }

    public YipeeLounge(String name) {
        this();
        if(name == null) throw new IllegalArgumentException("GameLounge name cannot be null.");
        setName(name);
    }

    public void addRoom(YipeeRoom room){
        if(room == null) throw new IllegalArgumentException("Cannot add room. Room is null");
        rooms.put(room.getName(), room);
    }

    public Map<String, YipeeRoom> getAllRooms(){
        return rooms;
    }

    public YipeeRoom getRoom(String roomName){
        if(roomName == null) return null;
        return rooms.get(roomName);
    }

    public void removeRoom(String roomName){
        if(roomName == null) return;
        rooms.remove(roomName);
    }

    @Override
    public void dispose() {
        rooms.clear();
        name = null;
    }
}