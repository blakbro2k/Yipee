package asg.games.yipee.net.tools;

public interface NetCodec {
    <T> T fromJson(String json, Class<T> type);

    String toJson(Object obj);
}