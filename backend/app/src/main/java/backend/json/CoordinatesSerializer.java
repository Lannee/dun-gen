package backend.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import backend.model.Coordinates;

import java.io.IOException;

public class CoordinatesSerializer extends JsonSerializer<Coordinates> {
    @Override
    public void serialize(Coordinates value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("x", value.getX());
        gen.writeNumberField("y", value.getY());

        gen.writeEndObject();
    }
}
