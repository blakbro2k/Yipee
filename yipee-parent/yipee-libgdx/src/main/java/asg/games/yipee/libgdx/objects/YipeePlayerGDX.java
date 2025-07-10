package asg.games.yipee.libgdx.objects;

import asg.games.yipee.libgdx.tools.NetUtil;
import com.badlogic.gdx.utils.Disposable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class YipeePlayerGDX extends AbstractYipeeObjectGDX implements Copyable<YipeePlayerGDX>, Disposable {
    public final static int DEFAULT_RATING_NUMBER = 1500;
    public final static int DEFAULT_ICON_NUMBER = 1;

    private int rating;
    private int icon;

    private String serializedKeyConfig;

    private YipeeKeyMapGDX keyConfig = new YipeeKeyMapGDX(this.getId());

    // Default constructor
    public YipeePlayerGDX() {
    }

    public YipeePlayerGDX(String name) {
        this(name, DEFAULT_RATING_NUMBER, DEFAULT_ICON_NUMBER);
    }

    public YipeePlayerGDX(String name, int rating) {
        this(name, rating, DEFAULT_ICON_NUMBER);
    }

    public YipeePlayerGDX(String name, int rating, int icon) {
        this();
        setName(name);
        setRating(rating);
        setIcon(icon);
    }

    public YipeeKeyMapGDX getKeyConfig() {
        if (keyConfig == null && serializedKeyConfig != null) {
            synchronized (this) {
                if (keyConfig == null) {
                    try {
                        keyConfig = NetUtil.fromJsonClient(serializedKeyConfig, YipeeKeyMapGDX.class);
                        if (keyConfig.getPlayerId() == null && getId() != null) {
                            keyConfig.setPlayerId(getId());
                        }
                    } catch (RuntimeException e) {
                        throw new RuntimeException("Failed to deserialize keyConfig", e);
                    }
                }
            }
        }
        return keyConfig;
    }

    public void setKeyConfig(YipeeKeyMapGDX keyConfig) {
        if (keyConfig != null && keyConfig.getPlayerId() == null && getId() != null) {
            keyConfig.setPlayerId(getId());
        }
        this.keyConfig = keyConfig;
        try {
            this.serializedKeyConfig = (keyConfig != null) ? NetUtil.toJsonClient(keyConfig) : null;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void increaseRating(int inc) {
        if (inc < 0) throw new IllegalArgumentException("Increment must be non-negative.");
        rating += inc;
    }

    public void decreaseRating(int dec) {
        if (dec < 0) throw new IllegalArgumentException("Decrement must be non-negative.");
        rating = Math.max(0, rating - dec);
    }

    @Override
    public YipeePlayerGDX copy() {
        YipeePlayerGDX copy = new YipeePlayerGDX();
        copyParent(copy);
        copy.setRating(this.rating);
        copy.setIcon(this.icon);
        return copy;
    }

    @Override
    public YipeePlayerGDX deepCopy() {
        YipeePlayerGDX copy = copy();
        copy.setKeyConfig(this.getKeyConfig() != null ? this.getKeyConfig().deepCopy() : null);
        return copy;
    }

    @Override
    public void dispose() {
        if (keyConfig != null) {
            keyConfig.dispose();
        }
    }
}
