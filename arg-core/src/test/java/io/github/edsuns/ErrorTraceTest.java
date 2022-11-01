package io.github.edsuns;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by Edsuns@qq.com on 2022/10/30.
 */
public class ErrorTraceTest {

    @Test
    void autoFillName() {
        assertNotNull(TestErr.INTERNAL_ERROR);
        assertEquals("TEST", TestErr.TEST.getError().getName());
        assertEquals("THE_THIRD_ERR", TestErr.THE_THIRD_ERR.getName());
    }
}
