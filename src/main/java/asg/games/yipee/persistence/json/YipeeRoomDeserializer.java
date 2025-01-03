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
import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
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
        JsonNode node = jp.getCodec().readTree(jp);
        YipeeRoom room = null;
        room = new YipeeRoom();
        room.setId(node.get("id").asText());
        room.setName(node.get("name").asText());
        room.setCreated(node.get("created").asLong());
        room.setModified(node.get("modified").asLong());
        room.setLoungeName(node.get("lounge").asText());

        Set<YipeePlayer> allPlayers = Util.listToSet(Util.jsonNodeToCollection(YipeePlayer.class, node.get("players")));
        Set<YipeeTable> allTables = Util.listToSet(Util.jsonNodeToCollection(YipeeTable.class, node.get("tables")));
        room.setPlayers(allPlayers);
        room.setTables(allTables);
        return room;
    }
}