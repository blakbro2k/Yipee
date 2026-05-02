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

import com.badlogic.gdx.utils.JsonValue;

/**
 * LibGDX JSON-mappable seat state.
 * IMPORTANT: booleans are named occupied/ready to match JSON ("occupied":true, "ready":false).
 */
public class GdxSeatStateUpdateResponse {
    public String serverId;
    public String gameId;
    public String sessionId;
    public long serverTick;
    public long serverTimestamp;
    public int tickRate;
    public int seatNumber;

    public String tableId;

    // Your server payload currently sends states:null. Keep as JsonValue (or Object) so it can be null or array.
    // If/when you send real state lists, you can swap this for ArrayList<YourGdxGameBoardState>.
    public JsonValue states;

    public int seatIndex;

    public boolean occupied;
    public boolean ready;
    public boolean seatReady;

    public GdxNetYipeePlayerDTO player; // can be null
    public String playerId;             // can be null
    public String seatId;             // can be null

    public String packetType; // "SeatStateUpdateResponse"
}