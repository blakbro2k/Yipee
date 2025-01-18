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
package asg.games.yipee.persistence.json;

import asg.games.yipee.objects.YipeePlayer;
import asg.games.yipee.objects.YipeeRoom;
import asg.games.yipee.objects.YipeeTable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Custom JSON Deserializer for YipeeRoom Game Object
 * implements StdDeserializer interface
 *
 * @author Blakbro2k
 */

public class YipeeRoomDeserializer extends StdDeserializer<YipeeRoom> {
    private final Logger logger = LoggerFactory.getLogger(YipeeRoomDeserializer.class);

    public YipeeRoomDeserializer() {
        this(null);
    }

    public YipeeRoomDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public YipeeRoom deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
        System.out.println("Enter YipeeRoom deserialize()");

        JsonNode node = jp.getCodec().readTree(jp);

        System.out.println("deserialize()  node: " + node.toPrettyString());
        YipeeRoom room = new YipeeRoom();
        room.setId(node.get("id").asText());
        room.setName(node.get("name").asText());
        room.setCreated(node.get("created").asLong());
        room.setModified(node.get("modified").asLong());
        room.setLoungeName(node.get("lounge").asText());

        Set<YipeePlayer> allPlayers = new LinkedHashSet<>();

        // Deserialize players
        if (node.has("players") && !node.get("players").isNull()) {
            for (JsonNode playerNode : node.get("players")) {
                try {
                    YipeePlayer player = jp.getCodec().treeToValue(playerNode, YipeePlayer.class);
                    allPlayers.add(player);
                } catch (Exception e) {
                    logger.error("Failed to deserialize player: " + playerNode + ", error: " + e.getMessage());
                }
            }
            room.setPlayers(allPlayers);
        }

/*
        // Deserialize tables

            for (JsonNode tableNode : node.get("tables")) {
                System.out.println("tableNode: " + tableNode);

                try {
                    YipeeTable table = jp.getCodec().treeToValue(tableNode, YipeeTable.class);
                    allTables.add(table);
                } catch (Exception e) {
                    logger.error("Failed to deserialize table: " + tableNode + ", error: " + e.getMessage());
                }
            }
            //room.setTables(allTables);
        }*/

        System.out.println("deserialize(tableIndexMap)");
        System.out.println("node: " + node);

        if (node.has("tableIndexMap") && !node.get("tableIndexMap").isNull()) {
            Map<Integer, YipeeTable> map = new HashMap<>();
            //System.out.println("node: " + node.get("tableIndexMap"));
            JsonNode mapNode = node.get("tableIndexMap");

            Iterator<Map.Entry<String, JsonNode>> fields = mapNode.fields();
            System.out.println("fields: " + mapNode);

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                System.out.println("entry: " + entry);
                Integer tableNumber = Integer.valueOf(entry.getKey());
                YipeeTable table = jp.getCodec().treeToValue(entry.getValue(), YipeeTable.class);

                map.put(tableNumber, table);
            }
            room.setTableIndexMap(map);
        }

        System.out.println("Exit YipeeRoom deserialize()");
        return room;
    }
}

