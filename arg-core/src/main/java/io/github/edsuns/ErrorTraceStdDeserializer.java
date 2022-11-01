package io.github.edsuns;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Created by Edsuns@qq.com on 2022/10/29.
 */
public class ErrorTraceStdDeserializer extends StdDeserializer<ErrorTrace> {

    private static final long serialVersionUID = -3370822974199256943L;

    protected ErrorTraceStdDeserializer() {
        super(ErrorTrace.class);
    }

    @Override
    public ErrorTrace deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String message = node.get("message").asText();
        String name = node.get("name").asText();
        long moduleId = node.get("moduleId").asLong();
        return new ErrorTrace(name, message, moduleId) {
            private static final long serialVersionUID = -5702483762565305336L;
        };
    }
}
