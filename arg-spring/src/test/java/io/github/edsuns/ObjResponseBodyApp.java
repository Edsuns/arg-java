package io.github.edsuns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author edsuns@qq.com
 * @date 2022/10/30
 */
@SpringBootApplication
public class ObjResponseBodyApp {

    public static void main(String[] args) {
        SpringApplication.run(ObjResponseBodyApp.class, args);
    }

    @RestControllerAdvice
    static class ObjControllerAdvice extends ObjResponseBodyAdvice {
    }

    @RestController
    static class DemoController {

        @GetMapping("/api/error")
        public Obj<Integer> error() {
            return TestRequestErr.TEST.cast();
        }

        @GetMapping("/api/success")
        public Obj<Integer> success() {
            return Obj.of(1);
        }
    }
}
