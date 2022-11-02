package io.github.edsuns;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author edsuns@qq.com
 * @date 2022/10/30
 */
public class ErrorTraceStdSerializer extends StdSerializer<ErrorTrace> {

    private static final long serialVersionUID = -3777350705576270371L;

    protected ErrorTraceStdSerializer() {
        super(ErrorTrace.class);
    }

    @Override
    public void serialize(ErrorTrace errorTrace, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", errorTrace.getName());
        jsonGenerator.writeStringField("message", errorTrace.getMessage());
        // moduleId is a 64-bit number, which can be overflow in JavaScript, so transferred to String
        jsonGenerator.writeStringField("moduleId", String.valueOf(errorTrace.getModuleId()));
        jsonGenerator.writeEndObject();
    }
}
