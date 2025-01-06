/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Custom JSON Deserializer for YipeeRoom Game Object
 * implements StdDeserializer interface
 *
 * @author Blakbro2k
 */

public class YipeeRoomDeserializer extends StdDeserializer<YipeeRoom> {

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
        YipeeRoom room = new YipeeRoom();
        room.setId(node.get("id").asText());
        room.setName(node.get("name").asText());
        room.setCreated(node.get("created").asLong());
        room.setModified(node.get("modified").asLong());
        room.setLoungeName(node.get("lounge").asText());

        Set<YipeePlayer> allPlayers = new LinkedHashSet<>();
        Set<YipeeTable> allTables = new LinkedHashSet<>();

        // Handle seats (deserialize nested objects)
        if (node.has("players")) {
            JsonNode playersNode = node.get("players");
            System.out.println("playersNode: " + playersNode);
            for (JsonNode playerNode : playersNode) {
                YipeePlayer player = jp.getCodec().treeToValue(playersNode, YipeePlayer.class);
                //table.getSeats().add(seat);
                System.out.println("playersNode: " + playersNode);
                System.out.println("player: " + player);
                allPlayers.add(player);
            }
            room.setPlayers(allPlayers);
        }

        // Handle seats (deserialize nested objects)
        if (node.has("tables")) {
            JsonNode seatsNode = node.get("tables");
            System.out.println("seatsNode: " + seatsNode);
            for (JsonNode seatNode : seatsNode) {
                YipeeTable table = jp.getCodec().treeToValue(seatNode, YipeeTable.class);
                //table.getSeats().add(seat);
                System.out.println("seatNode: " + seatNode);
                System.out.println("table: " + table);
                allTables.add(table);
            }
            room.setTables(allTables);
        }
        System.out.println("Enter YipeeRoom deserialize()");
        return room;
    }
}