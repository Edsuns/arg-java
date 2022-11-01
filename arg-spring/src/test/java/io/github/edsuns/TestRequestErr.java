package io.github.edsuns;

import org.springframework.http.HttpStatus;

/**
 * @author edsuns@qq.com
 * @date 2022/11/01 13:58
 */
public final class TestRequestErr extends RequestErr {

    private static final long serialVersionUID = 5858441456884657176L;

    private TestRequestErr(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static Obj<?> TEST = Obj.error(new TestRequestErr("should call constructor on every field"));
}
