package backend.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import backend.model.Chapter;

import java.io.IOException;

public class ChapterSerializer extends JsonSerializer<Chapter> {
    @Override
    public void serialize(Chapter value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeNumberField("marines_count", value.getMarinesCount());
        gen.writeStringField("world", value.getWorld());

        gen.writeEndObject();
    }
}