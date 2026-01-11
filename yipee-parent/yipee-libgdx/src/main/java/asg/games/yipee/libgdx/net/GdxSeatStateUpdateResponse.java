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

    public String tableId;

    // Your server payload currently sends states:null. Keep as JsonValue (or Object) so it can be null or array.
    // If/when you send real state lists, you can swap this for ArrayList<YourGdxGameBoardState>.
    public JsonValue states;

    public int seatIndex;

    public boolean occupied;
    public boolean ready;

    public GdxNetYipeePlayerDTO player; // can be null
    public String playerId;             // can be null

    public String packetType; // "SeatStateUpdateResponse"
}