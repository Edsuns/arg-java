package io.github.edsuns;

import org.springframework.http.HttpStatus;

/**
 * @author edsuns@qq.com
 * @date 2022/10/30
 */
public abstract class RequestErr extends ErrorTrace {

    private static final long serialVersionUID = -2389865799389747381L;

    private final HttpStatus status;

    protected RequestErr(String message, HttpStatus status) {
        super(message);
        if (status.is2xxSuccessful() || status.is3xxRedirection())
            throw new IllegalArgumentException("status");
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
