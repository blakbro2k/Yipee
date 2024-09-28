package asg.games.yipee.objects;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public abstract class ObjectSerializer<T> extends StdSerializer<T> {

    public ObjectSerializer(final Class<T> t) {
        super(t);
    }

    @Override
    public void serialize(final T value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        doWriteProperties(value, gen, provider);
        gen.writeEndObject();
    }

    protected abstract void doWriteProperties(final T value, final JsonGenerator gen, final SerializerProvider provider) throws IOException;
    protected abstract T doReadProperties(final DeserializationContext context, final JsonNode node) throws IOException;
}