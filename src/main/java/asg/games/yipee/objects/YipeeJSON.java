package asg.games.yipee.objects;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public abstract class YipeeJSON<T> {
    class Serialize extends StdSerializer<T> {
        protected Serialize(final Class<T> clazz) {
            super(clazz);
        }

        @Override
        public void serialize(T clazz, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            doWriteProperties(jsonGenerator, serializerProvider);
        }
    }

    class Deserialize extends StdDeserializer<T> {
        public Deserialize(final Class<T> clazz) {
            super(clazz);
        }

        @Override
        public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            return doReadProperties(new Object(), deserializationContext, node);
        }
    }

    protected abstract <T> void doWriteProperties(final JsonGenerator gen, final SerializerProvider provider) throws IOException;
    protected abstract T doReadProperties(Object obj, final DeserializationContext context, final JsonNode node) throws IOException;
}
