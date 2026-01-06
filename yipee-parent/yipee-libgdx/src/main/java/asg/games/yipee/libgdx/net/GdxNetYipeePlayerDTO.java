package asg.games.yipee.libgdx.net;

/**
 * Minimal LibGDX-side player DTO matching your JSON.
 * Your JSON shows player has id/name/created/modified/rating/icon/roomsCount.
 */
public class GdxNetYipeePlayerDTO {
    public String id;
    public String name;
    public long created;
    public long modified;
    public int rating;
    public int icon;
    public int roomsCount;
}