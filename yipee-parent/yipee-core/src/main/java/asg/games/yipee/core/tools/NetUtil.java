package asg.games.yipee.core.tools;

import asg.games.yipee.common.net.NetYipeePlayer;
import asg.games.yipee.core.objects.AbstractYipeeObject;
import asg.games.yipee.core.objects.YipeePlayer;
import asg.games.yipee.core.objects.YipeeRoom;
import asg.games.yipee.core.objects.YipeeTable;
import asg.games.yipee.core.persistence.json.YipeeRoomDeserializer;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetUtil {
    private static final ObjectMapper json;
    private static final SimpleModule module = new SimpleModule();

    static {
        module.addDeserializer(YipeeRoom.class, new YipeeRoomDeserializer());
        json = JsonMapper.builder()
            .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
            .disable(StreamReadFeature.AUTO_CLOSE_SOURCE)
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .build();
        json.registerModule(module);
    }

    public static YipeePlayer getCoreNetPlayer(NetYipeePlayer netYipeePlayer) {
        if(netYipeePlayer instanceof YipeePlayer) {
            return (YipeePlayer) netYipeePlayer;
        }
        return null;
    }

    public static String getCoreNetPlayerName(NetYipeePlayer netYipeePlayer) {
        if(getCoreNetPlayer(netYipeePlayer) != null) {
            return getCoreNetPlayer(netYipeePlayer).getName();
        }
        return "";
    }

    public static <T> T readValue(String jsonValue, Class<T> clazz) throws JsonProcessingException {
        return json.readValue(jsonValue, clazz);
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return json.writeValueAsString(object);
    }


    public static String getJsonString(AbstractYipeeObject o) throws JsonProcessingException {
        return json.writeValueAsString(o);
    }

    public static <T> T getObjectFromJsonString(Class<T> type, String jsonStr) throws JsonProcessingException {
        return json.readValue(jsonStr, type);
    }

    public static <T> List<T> jsonNodeToCollection(Class<T> inClass, JsonNode node) {
        List<T> collection = new ArrayList<>();
        if (node instanceof ArrayNode) {
            for (JsonNode o : node) {
                if (o != null) {
                    String localClassName = o.get(JsonTypeInfo.Id.CLASS.getDefaultPropertyName()).asText();
                    try {
                        Class<?> clazz = Class.forName(localClassName);
                        Object object = getObjectFromJsonString(clazz, o.toString());
                        if (inClass.isInstance(object)) {
                            collection.add(inClass.cast(object));
                        }
                    } catch (ClassNotFoundException | JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return collection;
    }

    public static <T> Map<Integer, YipeeTable> jsonNodeToTableIndexMap(Class<T> inClass, JsonNode node) {
        Map<Integer, YipeeTable> tableMap = new HashMap<>();
        List<YipeeTable> collection = jsonNodeToCollection(YipeeTable.class, node);
        for (YipeeTable table : collection) {
            if (table != null) {
                int tableNum = table.getTableNumber();
                tableMap.put(tableNum, table);
            }
        }
        return tableMap;
    }
}
