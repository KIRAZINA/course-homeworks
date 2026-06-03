package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 * <p>
 * The {@code @SpringBootApplication} annotation enables:
 * <ul>
 *   <li><b>Auto-configuration:</b> Automatically configures Spring Web, Data JPA, Security, and Thymeleaf based on classpath dependencies.</li>
 *   <li><b>Component Scanning:</b> Scans the {@code com.example} package and all sub-packages for {@code @Controller}, {@code @Service}, {@code @Repository}, {@code @Configuration}, etc.</li>
 *   <li><b>Externalized Configuration:</b> Loads properties from {@code application.properties}.</li>
 * </ul>
 *
 * @author University Assignment Team
 * @since 2024
 */
@SpringBootApplication
public class SpringSecurityApplication {

    /**
     * Bootstraps and runs the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }
}