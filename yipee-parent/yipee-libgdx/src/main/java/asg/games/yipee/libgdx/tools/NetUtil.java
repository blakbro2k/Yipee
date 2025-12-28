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
package asg.games.yipee.libgdx.tools;

import asg.games.yipee.common.dto.NetYipeePlayer;
import asg.games.yipee.libgdx.objects.YipeePlayerGDX;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class NetUtil {

    // LibGDX Json instance (thread-safe if used read-only)
    private static final Json json = new Json();

    static {
        json.setOutputType(JsonWriter.OutputType.json);
    }

    /** Serialize an object to JSON for network transmission (LibGDX). */
    public static String toJsonClient(Object obj) {
        return json.toJson(obj);
    }

    /** Deserialize an object from JSON for network transmission (LibGDX). */
    public static <T> T fromJsonClient(String jsonString, Class<T> type) {
        return json.fromJson(type, jsonString);
    }

    public static YipeePlayerGDX getLibGDXNetPlayer(NetYipeePlayer netYipeePlayer) {
        if(netYipeePlayer instanceof YipeePlayerGDX) {
            YipeePlayerGDX player = (YipeePlayerGDX) netYipeePlayer;
            YipeePlayerGDX retPlayer = new YipeePlayerGDX();
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

    public static String getLibGDXNetPlayerName(NetYipeePlayer netYipeePlayer) {
        if(getLibGDXNetPlayer(netYipeePlayer) != null) {
            return getLibGDXNetPlayer(netYipeePlayer).getName();
        }
        return "";
    }
}
