package io.github.edsuns;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Edsuns@qq.com on 2022/10/30.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = {ObjResponseBodyApp.class})
public class ObjResponseBodyAdviceTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void test() throws Exception {
        mvc.perform(get("/api/error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.name", is("TEST")))
                .andExpect(jsonPath("$.error.moduleId", not(blankString())))
                .andExpect(jsonPath("$.error.message", notNullValue()));
        mvc.perform(get("/api/success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
    }
}
