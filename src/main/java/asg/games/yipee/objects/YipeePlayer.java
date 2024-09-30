package asg.games.yipee.objects;

import asg.games.yipee.tools.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Objects;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YipeePlayer extends AbstractYipeeObject implements Copyable<YipeePlayer> {
    @JsonIgnore
    public final static int DEFAULT_RATING_NUMBER = 1500;
    @JsonIgnore
    public final static YipeePlayer BLANK_PLAYER = new YipeePlayer("", DEFAULT_RATING_NUMBER, 0);

    private int rating;
    private int icon;

    //Empty Constructor required for Json.Serializable
    public YipeePlayer(){}

    public YipeePlayer(Class<YipeePlayer> clazz, String data) throws JsonProcessingException {
        YipeePlayer temp = Util.getObjectFromJsonString(clazz, data);
        if(temp != null) {
            setId(temp.getId());
            setName(temp.getName());
            setCreated(temp.getCreated());
            setModified(temp.getModified());
            setRating(temp.getRating());
            setIcon(temp.getIcon());
        }
    }

    public YipeePlayer(String name){
        this(name, DEFAULT_RATING_NUMBER, 1);
    }

    public YipeePlayer(String name, int rating){
        this(name, rating, 1);
    }

    public YipeePlayer(String name, int rating, int icon){
        this();
        setName(name);
        setRating(rating);
        setIcon(icon);
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public int getIcon(){
        return this.icon;
    }

    public void increaseRating(int inc){
        rating += inc;
    }

    public void decreaseRating(int dec){
        rating -= dec;
    }

    @Override
    public YipeePlayer copy() {
        YipeePlayer copy = new YipeePlayer();
        copyParent(copy);
        return copy;
    }

    @Override
    public YipeePlayer deepCopy() {
        YipeePlayer copy = copy();
        copy.setRating(this.rating);
        copy.setIcon(this.icon);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YipeePlayer that = (YipeePlayer) o;
        return getRating() == that.getRating() && getIcon() == that.getIcon();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRating(), getIcon());
    }
}