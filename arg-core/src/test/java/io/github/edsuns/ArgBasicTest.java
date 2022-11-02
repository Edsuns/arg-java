package io.github.edsuns;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author edsuns@qq.com
 * @date 2022/11/01 14:03
 */
public class ArgBasicTest {

    @Test
    void staticOfMethods() {
        assertTrue(Arg.of(null, "just an error").isError());
        assertFalse(Arg.of(1, "just an error").isError());
        assertThrows(NullPointerException.class, () -> Arg.of(1, null));
    }

    @Test
    void errorObj() {
        Obj<Integer> obj = Obj.error(TestErr.INTERNAL_ERROR);
        assertTrue(obj.isError());
        assertEquals(TestErr.INTERNAL_ERROR, obj.getError());
        assertThrows(ArgException.class, obj::unwrap);
        assertThrows(ArgException.class, () -> obj.map(String::valueOf).unwrap());
        assertThrows(ArgException.class, () -> obj.map(String::valueOf).unwrap());

        Arg<Integer, String> flatMap = obj.flatMap(val -> Arg.of(1, "string err"), "just err");
        assertTrue(flatMap.isError());
        assertEquals("just err", flatMap.getError());

        assertThrows(IllegalStateException.class,
            () -> obj.map(String::valueOf, "mapped error")
                .unwrap(err -> {
                    assertEquals(err, "mapped error");
                    return new IllegalStateException(err);
                }));
    }

    @Test
    void nonNullObj() {
        Obj<Integer> obj = Obj.of(1);
        assertFalse(obj.isError());
        assertThrows(NullPointerException.class, obj::getError);
        assertEquals(1, obj.unwrap());

        assertThrows(NullPointerException.class, () -> obj.map(val -> null).unwrap());

        assertEquals("1", obj.map(String::valueOf).unwrap());
        assertEquals("1", obj.map(String::valueOf, "mapped error").unwrap(IllegalArgumentException::new));

        assertThrows(ArgException.class,
            () -> obj.flatMap(val -> Arg.error(TestErr.INTERNAL_ERROR)).unwrap());
    }

    @Test
    void equals() {
        assertEquals(Obj.of(1), Obj.of(1));
        assertEquals(Arg.notNull(1), Obj.of(1));

        assertNotEquals(Optional.of(1), Obj.of(1));
    }

    @Test
    void cast() {
        Obj<Integer> errObj = TestErr.TEST.cast();
        assertTrue(errObj.isError());
    }
}
