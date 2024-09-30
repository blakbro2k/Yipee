package asg.games.yipee.json;

import asg.games.yipee.objects.AbstractYipeeObject;
import asg.games.yipee.objects.YipeePlayer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public abstract class AbstractYipeeObjectSerializer<T> extends StdSerializer<T> {

    public AbstractYipeeObjectSerializer(final Class<T> t) {
        super(t);
    }

    @Override
    public void serialize(final T value, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        serializeAbstractObjectValue(value, generator, provider);
        serialize(value, generator, provider);
        generator.writeEndObject();
    }

    @Override
    public void serializeWithType(final T value, final JsonGenerator generator,
                                  final SerializerProvider provider, final TypeSerializer serializer) throws IOException {
        generator.writeStartObject();
        serializer.writeTypePrefixForObject(value, generator);
        serializeAbstractObjectValue(value, generator, provider);
        serialize(value, generator, provider);
        serializer.writeTypeSuffixForObject(value, generator);
        generator.writeEndObject();
    }

    private void serializeAbstractObjectValue(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value instanceof AbstractYipeeObject) {
            gen.writeStringField("id", ((AbstractYipeeObject) value).getId());
            gen.writeStringField("name", ((AbstractYipeeObject) value).getId());
            gen.writeNumberField("created", ((AbstractYipeeObject) value).getCreated());
            gen.writeNumberField("modified", ((AbstractYipeeObject) value).getModified());
        }
    }

    protected abstract void serializeContent(YipeePlayer value, JsonGenerator generator, SerializerProvider provider) throws IOException;

}