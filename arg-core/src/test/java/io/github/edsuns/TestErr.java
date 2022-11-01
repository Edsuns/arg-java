package io.github.edsuns;

/**
 * Created by Edsuns@qq.com on 2022/10/29.
 */
public final class TestErr extends ErrorTrace {

    private static final long serialVersionUID = -427444491997346706L;

    private TestErr(String message) {
        super(message);
    }

    public static TestErr INTERNAL_ERROR = new TestErr("internal error");

    public static Obj<?> TEST = Obj.error(new TestErr("should call constructor on every field"));

    public static TestErr THE_THIRD_ERR = new TestErr("the 3rd error");
}
