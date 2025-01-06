package backend.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import backend.model.SpaceMarine;

import java.io.IOException;

public class SpaceMarineSerializer extends JsonSerializer<SpaceMarine> {
    @Override
    public void serialize(SpaceMarine value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("id", String.valueOf(value.getId()));
        gen.writeStringField("name", value.getName());
        gen.writeObjectField("coordinates", value.getCoordinates());
        gen.writeStringField("creationDate", value.getCreationDate().toString());
        gen.writeObjectField("chapter", value.getChapter());
        gen.writeStringField("health", String.valueOf(value.getHealth()));
        gen.writeStringField("achievements", value.getAchievements());
        gen.writeStringField("category", value.getCategory().toString());
        gen.writeStringField("weaponType", value.getWeaponType().toString());
        gen.writeNumberField("userId", value.getUser().getId());

        gen.writeEndObject();
    }
}
