package asg.games.yipee.net.packets;

public class GameAuthTokenResponse extends AbstractServerResponse {
    public String playerId;
    public String name;
    public int icon;
    public int rating;
    public String clientId;
    public String tableId;
    public String expiresAt; // keep as String, parse later if needed
}
