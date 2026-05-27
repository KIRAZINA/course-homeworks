package app.jpapracticeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class JpaPracticeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaPracticeServiceApplication.class, args);
    }
}