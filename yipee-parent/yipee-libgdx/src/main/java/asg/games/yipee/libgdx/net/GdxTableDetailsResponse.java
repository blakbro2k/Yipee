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
package asg.games.yipee.libgdx.net;


import com.badlogic.gdx.utils.Array;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import lombok.Getter;
import lombok.Setter;

/**
 * LibGDX JSON-mappable snapshot of a table.
 * Matches server JSON keys exactly so com.badlogic.gdx.utils.Json can deserialize without reflection surprises.
 */
@Setter
@Getter
public class GdxTableDetailsResponse {
    public String serverId;
    public String gameId;
    public String sessionId;
    public long serverTick;
    public long serverTimestamp;
    public int tickRate;

    public String tableId;
    public int tableNumber;
    public String roomName;
    public boolean isSoundOn;
    public boolean isRated;
    public String tableAccessType;
    public Array<GdxSeatStateUpdateResponse> seats = GdxArrays.newArray();
    public Array<GdxNetYipeePlayerDTO> watchers = GdxArrays.newArray();

    // Present in your JSON ("packetType":"TableDetailsResponse") — keep it so Json doesn't choke.
    public String packetType;
}