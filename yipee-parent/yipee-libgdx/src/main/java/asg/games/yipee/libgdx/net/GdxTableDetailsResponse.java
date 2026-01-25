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