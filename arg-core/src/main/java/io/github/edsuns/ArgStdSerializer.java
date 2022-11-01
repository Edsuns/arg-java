package io.github.edsuns;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Created by Edsuns@qq.com on 2022/10/29.
 */
@SuppressWarnings("rawtypes")
public class ArgStdSerializer extends StdSerializer<Arg> {

    private static final long serialVersionUID = -8460889594925395280L;

    public ArgStdSerializer() {
        super(Arg.class);
    }

    @Override
    public void serialize(Arg obj, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (obj.isError()) {
            jsonGenerator.writeObjectField("error", ((Arg<?, ?>) obj).getError());
        } else {
            jsonGenerator.writeObjectField("value", obj.unwrap());
        }
        jsonGenerator.writeEndObject();
    }
}
