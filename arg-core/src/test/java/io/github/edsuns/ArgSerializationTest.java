package io.github.edsuns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author edsuns@qq.com
 * @date 2022/11/01 13:57
 */
public class ArgSerializationTest {

    @Test
    void serialVersionUID() {
        assertEquals(StaticLoader.get(TestErr.class).getSerialVersionUID(), TestErr.INTERNAL_ERROR.getModuleId());
    }

    @Test
    void jackson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Obj<Integer> obj = Obj.of(1);
        assertEquals(obj, mapper.readValue(mapper.writeValueAsString(obj), Obj.class));

        Obj<Integer> err = Obj.error(TestErr.INTERNAL_ERROR);
        assertEquals(err, mapper.readValue(mapper.writeValueAsString(err), Obj.class));
    }

    // @Test
    // void fastjson() {
    //     Obj<Integer> obj = Obj.of(1);
    //     String text = JSON.toJSONString(obj);
    //     System.out.println(text);
    //     assertEquals(obj, JSON.parseObject(text, Obj.class));
    //
    //     Obj<Integer> err = TestErr.TEST.cast();
    //     assertEquals(err, JSON.parseObject(JSON.toJSONString(err), Obj.class));
    // }

    @Test
    void jdkSerializable() throws IOException, ClassNotFoundException {
        Obj<Integer> err = TestErr.TEST.cast();
        assertEquals(err, readObj(writeObj(err)));

        Obj<Integer> obj = Obj.of(1);
        assertEquals(obj, readObj(writeObj(obj)));
    }

    private static <T> ByteArrayOutputStream writeObj(Obj<T> obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out;
    }

    @SuppressWarnings("unchecked")
    private static <T> Obj<T> readObj(ByteArrayOutputStream out) throws IOException, ClassNotFoundException {
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        return (Obj<T>) objIn.readObject();
    }
}
