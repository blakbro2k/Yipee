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
import asg.games.yipee.objects.YipeeSeat;
import asg.games.yipee.objects.YipeeTable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Custom JSON Deserializer for YipeeSeat Game Object
 * implements StdDeserializer interface
 *
 * @author Blakbro2k
 */

public class YipeeTableDeserializer extends StdDeserializer<YipeeTable> {
    Logger logger = LoggerFactory.getLogger(YipeeTableDeserializer.class);

    public YipeeTableDeserializer() {
        this(null);
    }

    public YipeeTableDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public YipeeTable deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        System.out.println("enter deserialize YipeeTable: ");

        /* Extract fields from JSON
        String id = node.get("id").asText();
        String name = node.get("name").asText();
        boolean isRated = node.get("rated").asBoolean();
        boolean isSoundOn = node.get("soundOn").asBoolean();
        */


        System.out.println("node: " + node);
        YipeeTable table = new YipeeTable();
        table.setId(node.get("id").asText());
        table.setName(node.get("name").asText());
        table.setCreated(node.get("created").asLong());
        table.setModified(node.get("modified").asLong());
        table.setRated(node.get("rated").asBoolean());
        table.setSound(node.get("soundOn").asBoolean());
        //table.setParentRoom(Util.getObjectFromJsonString(YipeeRoom.class, node.get("parentRoom").toString()));
//        System.out.println("playerNode: " + node.get("parentRoom").toString());

        //Set<YipeePlayer> allPlayers = Util.listToSet(Util.jsonNodeToCollection(YipeePlayer.class, node.get("watchers")));
        //Set<YipeeSeat> allSeats = Util.listToSet(Util.jsonNodeToCollection(YipeeSeat.class, node.get("seats")));
        //table.setSeats(allSeats);
        //table.setWatchers(allPlayers);

        //YipeeRoom parentRoom = ;
        //System.out.println("parentRoom: " + parentRoom);
        //table.setSeatedPlayer(player);

        //String noteGet = node.get("parentTable").toString();
        //System.out.println("tableNode: " + noteGet);
        // YipeeTable table = Util.getObjectFromJsonString(YipeeTable.class, noteGet);

        // Handle parentRoom (you might need to fetch or construct it)
        if (node.has("parentRoom")) {
            String parentRoomId = node.get("parentRoom").asText();
            // Resolve the parentRoom using an external service or database if necessary
            YipeeRoom parentRoom = resolveParentRoom(parentRoomId);
            table.setParentRoom(parentRoom);
        }

        // Handle seats (deserialize nested objects)
        if (node.has("seats")) {
            JsonNode seatsNode = node.get("seats");
            System.out.println("seatsNode: " + seatsNode);
            Set<YipeeSeat> allSeats = new LinkedHashSet<>();
            for (JsonNode seatNode : seatsNode) {
                YipeeSeat seat = jp.getCodec().treeToValue(seatNode, YipeeSeat.class);
                //table.getSeats().add(seat);
                System.out.println("seatNode: " + seatNode);
                System.out.println("seat: " + seat);
                seat.setParentTable(table); // Set bidirectional relationship
                allSeats.add(seat);
            }
            table.setSeats(allSeats);
        }

        // Handle seats (deserialize nested objects)
        if (node.has("watchers")) {
            JsonNode watchersNode = node.get("watchers");
            Set<YipeePlayer> allPlayers = new LinkedHashSet<>();
            for (JsonNode watcherNode : watchersNode) {
                //seat.setParentTable(table); // Set bidirectional relationship
                System.out.println("watcherNode: " + watcherNode);
                YipeePlayer player = jp.getCodec().treeToValue(watcherNode, YipeePlayer.class);
                player.addWatcher(table);
                System.out.println("player: " + player);

                allPlayers.add(player);
            }
            table.setWatchers(allPlayers);
        }

        //seat.setParentTable(table);
        System.out.println("table: " + table);
        System.out.println("exit deserialize YipeeTable");
        return table;
    }


    private YipeeRoom resolveParentRoom(String parentRoomId) {
        // Mocked method: Replace with actual lookup logic
        System.out.println("parentRoomId: " + parentRoomId);
        YipeeRoom parentRoom = new YipeeRoom();
        parentRoom.setId(parentRoomId);
        return parentRoom;
    }
}