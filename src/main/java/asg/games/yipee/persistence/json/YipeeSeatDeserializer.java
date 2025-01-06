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
import asg.games.yipee.objects.YipeeSeat;
import asg.games.yipee.objects.YipeeTable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Custom JSON Deserializer for YipeeSeat Game Object
 * implements StdDeserializer interface
 *
 * @author Blakbro2k
 */

public class YipeeSeatDeserializer extends StdDeserializer<YipeeSeat> {

    public YipeeSeatDeserializer() {
        this(null);
    }

    public YipeeSeatDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public YipeeSeat deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
        System.out.println("enter deserialize YipeeSeat: ");

        JsonNode node = jp.getCodec().readTree(jp);
        System.out.println("node: " + node);
        YipeeSeat seat = new YipeeSeat();
        seat.setId(node.get("id").asText());
        seat.setName(node.get("name").asText());
        seat.setCreated(node.get("created").asLong());
        seat.setModified(node.get("modified").asLong());


        // Handle parentRoom (you might need to fetch or construct it)
        if (node.has("parentTable")) {
            //String parentTableNode = node.get("parentTable").asText();
            JsonNode parentTable = node.get("parentTable");
            // Resolve the parentRoom using an external service or database if necessary
            System.out.println("parentTable: " + parentTable);
            System.out.println("node.parentTable: " + node.findParent("parentTable"));

            System.out.println("ctx node: " + ctx.readTree(jp).toPrettyString());
            YipeeTable parentRoom = resolveParentTable(jp, parentTable);
            //System.out.println("parentRoom: " + parentRoom);
            //seat.setParentTable(parentRoom);
        }

        // Handle parentRoom (you might need to fetch or construct it)
        if (node.has("seatedPlayer")) {
            //String seatedPlayerNode = node.get("seatedPlayer").asText();
            // Resolve the parentRoom using an external service or database if necessary
            System.out.println("seatedPlayer: " + node.get("seatedPlayer"));
            YipeePlayer seatedPlayer = resolveSeatedPlayerNode(jp, node.get("seatedPlayer"));
            System.out.println("seatedPlayer: " + seatedPlayer);
            seat.setSeatedPlayer(seatedPlayer);
        }

        /*YipeePlayer player = Util.getObjectFromJsonString(YipeePlayer.class, node.get("seatedPlayer").toString());
        System.out.println("player: " + player);
        seat.setSeatedPlayer(player);

        String noteGet = node.get("parentTable").toString();
        System.out.println("tableNode: " + noteGet);
        //YipeeTable table = Util.getObjectFromJsonString(YipeeTable.class, noteGet);
        //seat.setParentTable(table);
        //System.out.println("table: " + table);*/
        System.out.println("exit deserialize YipeeSeat");
        return seat;
    }

    private YipeePlayer resolveSeatedPlayerNode(JsonParser jp, JsonNode seatedPlayerNode) throws JsonProcessingException {
        // Mocked method: Replace with actual lookup logic

        //parentRoom.setId(seatedPlayerNode);
        return jp.getCodec().treeToValue(seatedPlayerNode, YipeePlayer.class);
    }

    private YipeeTable resolveParentTable(JsonParser jp, JsonNode parentTableNode) throws JsonProcessingException {
        // Mocked method: Replace with actual lookup logic
        System.out.println("resolveParentTable: " + parentTableNode);
        System.out.println("???: " + parentTableNode.has("@class"));

        //parentRoom.setId(parentRoomId);
        return jp.getCodec().treeToValue(parentTableNode, YipeeTable.class);
    }
}