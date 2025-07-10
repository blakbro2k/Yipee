package asg.games.yipee.net.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NetUtil {
    // Jackson serializer for server
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonServer(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T fromJsonServer(String json, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(json, type);
    }
}
