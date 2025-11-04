/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.core.tools;

import asg.games.yipee.common.net.NetYipeePlayer;
import asg.games.yipee.common.net.NetYipeeTable;
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

/**
 * <p>
 * Utility class for networking and JSON (de)serialization related to core Yipee game objects.
 * </p>
 *
 * This class wraps a configured Jackson {@link ObjectMapper} and provides convenience methods for:
 * <ul>
 *     <li>Converting between JSON strings and Java objects</li>
 *     <li>Handling network-specific representations like {@link NetYipeePlayer}</li>
 *     <li>Mapping collections and typed objects from {@link JsonNode}</li>
 * </ul>
 *
 *
 * <p>Supports polymorphic deserialization using class metadata via {@link JsonTypeInfo}.</p>
 *
 * @author
 */
public class NetUtil {

    /** Internal ObjectMapper with Yipee custom configuration for deserialization. */
    private static final ObjectMapper json;

    /** Jackson module holding custom deserializers for Yipee objects. */
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

    /**
     * Casts a {@link NetYipeePlayer} to its core {@link YipeePlayer} implementation if possible.
     *
     * @param netYipeePlayer the network representation of a player
     * @return the core {@link YipeePlayer} if compatible, or {@code null} if not
     */
    public static YipeePlayer getCoreNetPlayer(NetYipeePlayer netYipeePlayer) {
        if (netYipeePlayer instanceof YipeePlayer) {
            YipeePlayer player = (YipeePlayer) netYipeePlayer;
            YipeePlayer retPlayer = new YipeePlayer();
            retPlayer.setId(player.getId());
            retPlayer.setName(player.getName());
            retPlayer.setCreated(player.getCreated());
            retPlayer.setModified(player.getModified());
            retPlayer.setIcon(player.getIcon());
            retPlayer.setRating(player.getRating());
            return retPlayer;
        }
        return null;
    }

    /**
     * Gets the name of the player from a {@link NetYipeePlayer} if it is a {@link YipeePlayer}.
     *
     * @param netYipeePlayer the network player object
     * @return the player's name, or empty string if not a {@link YipeePlayer}
     */
    public static String getCoreNetPlayerName(NetYipeePlayer netYipeePlayer) {
        YipeePlayer player = getCoreNetPlayer(netYipeePlayer);
        return player != null ? player.getName() : "";
    }

    /**
     * Casts a {@link NetYipeeTable} to its core {@link YipeeTable} implementation if possible.
     *
     * @param netYipeeTable the network player object
     * @return the core {@link YipeeTable} if compatible, or {@code null} if not
     */
    public static YipeeTable getCoreNetTable(NetYipeeTable netYipeeTable) {
        if (netYipeeTable instanceof YipeeTable) {
            YipeeTable table = (YipeeTable) netYipeeTable;
            YipeeTable retTable = new YipeeTable();
            retTable.setId(table.getId());
            retTable.setName(table.getName());
            retTable.setCreated(table.getCreated());
            retTable.setModified(table.getModified());
            retTable.setTableNumber(table.getTableNumber());
            retTable.setRated(table.isRated());
            retTable.setSoundOn(table.isSoundOn());
            return retTable;
        }
        return null;
    }

    /**
     * Deserializes a JSON string to a Java object.
     *
     * @param jsonValue the JSON content
     * @param clazz     the class to convert to
     * @param <T>       the expected return type
     * @return the deserialized object
     * @throws JsonProcessingException if the content is invalid or does not match the class
     */
    public static <T> T readValue(String jsonValue, Class<T> clazz) throws JsonProcessingException {
        return json.readValue(jsonValue, clazz);
    }

    /**
     * Serializes a Java object into its JSON string representation.
     *
     * @param object the object to serialize
     * @return the JSON string
     * @throws JsonProcessingException if serialization fails
     */
    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return json.writeValueAsString(object);
    }

    /**
     * Converts a Yipee core object into a JSON string.
     *
     * @param o the Yipee object
     * @return JSON representation of the object
     * @throws JsonProcessingException if serialization fails
     */
    public static String getJsonString(AbstractYipeeObject o) throws JsonProcessingException {
        return json.writeValueAsString(o);
    }

    /**
     * Parses a JSON string into a Java object of the specified type.
     *
     * @param type    the target class
     * @param jsonStr the JSON content
     * @param <T>     the expected return type
     * @return the deserialized object
     * @throws JsonProcessingException if deserialization fails
     */
    public static <T> T getObjectFromJsonString(Class<T> type, String jsonStr) throws JsonProcessingException {
        return json.readValue(jsonStr, type);
    }

    /**
     * Converts a {@link JsonNode} array to a list of objects of the specified type using polymorphic deserialization.
     *
     * @param inClass the expected base type
     * @param node    the {@link JsonNode} representing an array of objects
     * @param <T>     the type of objects to return
     * @return a list of deserialized objects
     */
    public static <T> List<T> jsonNodeToCollection(Class<T> inClass, JsonNode node) {
        List<T> collection = new ArrayList<>();
        if (node instanceof ArrayNode) {
            for (JsonNode o : node) {
                if (o != null) {
                    JsonNode typeNode = o.get(JsonTypeInfo.Id.CLASS.getDefaultPropertyName());
                    if (typeNode == null || !typeNode.isTextual()) continue;

                    String localClassName = typeNode.asText();
                    try {
                        Class<?> clazz = Class.forName(localClassName);
                        Object object = getObjectFromJsonString(clazz, o.toString());
                        if (inClass.isInstance(object)) {
                            collection.add(inClass.cast(object));
                        }
                    } catch (ClassNotFoundException | JsonProcessingException e) {
                        e.printStackTrace(); // Consider replacing with a logger
                    }
                }
            }
        }
        return collection;
    }

    /**
     * Converts a {@link JsonNode} into a map of {@link YipeeTable} objects indexed by their table number.
     *
     * @param inClass expected type of elements (ignored, kept for legacy support)
     * @param node    the JSON node to parse
     * @param <T>     unused
     * @return a map from table number to {@link YipeeTable}
     */
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
