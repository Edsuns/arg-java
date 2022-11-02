package io.github.edsuns;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author edsuns@qq.com
 * @date 2022/10/29
 */
public class ObjResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Obj.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return convertBody((Obj<?>) body, response);
    }

    protected <T> Object convertBody(Obj<T> body, ServerHttpResponse response) {
        if (body.isError()) {
            response.setStatusCode(getHttpStatus(body.getError()));
            return createErrorBody(body.getError());
        } else {
            return createSuccessfulBody(body.unwrap());
        }
    }

    protected Object createSuccessfulBody(Object data) {
        return data;
    }

    protected Object createErrorBody(ErrorTrace errorTrace) {
        return new ErrorBody(errorTrace);
    }

    protected HttpStatus getHttpStatus(ErrorTrace errorTrace) {
        if (errorTrace instanceof RequestErr) {
            return ((RequestErr) errorTrace).getStatus();
        }
        return HttpStatus.BAD_REQUEST;
    }

    public static class ErrorBody {
        private final ErrorTrace error;

        public ErrorBody(ErrorTrace error) {
            this.error = error;
        }

        @SuppressWarnings("unused")
        public ErrorTrace getError() {
            return error;
        }
    }
}
