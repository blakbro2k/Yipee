package asg.games.yipee.libgdx.tools;

import com.badlogic.gdx.utils.Json;

public class NetUtil {

    // LibGDX serializer for client
    private static final Json libgdxJson = new Json();

    public static String toJsonClient(Object obj) {
        return libgdxJson.toJson(obj);
    }

    public static <T> T fromJsonClient(String json, Class<T> type) {
        return libgdxJson.fromJson(type, json);
    }
}
