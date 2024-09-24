package asg.games.yipee.objects;

import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

@Deprecated
public class YokelLounge extends AbstractYokelObject {
    public static final String SOCIAL_GROUP = "Social";
    public static final String BEGINNER_GROUP = "Beginner";
    public static final String INTERMEDIATE_GROUP = "Intermediate";
    public static final String ADVANCED_GROUP = "Advanced";
    public static final String DEFAULT_LOUNGE = "Default";

    final private OrderedMap<String, YokelRoom> rooms = GdxMaps.newOrderedMap();

    //Empty Constructor required for Json.Serializable
    public YokelLounge(){}

    public YokelLounge(String name) {
        if(name == null) throw new IllegalArgumentException("GameLounge name cannot be null.");
        setName(name);
    }

    public void addRoom(YokelRoom room){
        if(room == null) throw new IllegalArgumentException("Cannot add room. Room is null");
        rooms.put(room.getName(), room);
    }

    public OrderedMap<String, YokelRoom> getAllRooms(){
        return rooms;
    }

    public YokelRoom getRoom(String roomName){
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