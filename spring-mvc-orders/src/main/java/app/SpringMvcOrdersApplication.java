package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point for Spring Boot.
 * This class bootstraps the Spring application context.
 */
@SpringBootApplication
public class SpringMvcOrdersApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringMvcOrdersApplication.class, args);
    }
}