package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for CORS and static resource serving.
 * 
 * This configuration:
 * 1. Enables CORS for local development (frontend on different port)
 * 2. Configures static resource serving (HTML, CSS, JS)
 * 3. Maps root path (/) to serve index.html for SPA routing
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS to allow local development.
     * 
     * In production, restrict origins to your actual domain.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:5173", 
                               "http://localhost:5174", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Configure static resource serving.
     * 
     * Serves static files from classpath:/static/ at root context path.
     * This allows index.html, styles.css, and script.js to be accessed
     * from the root URL (http://localhost:8080).
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600); // Cache for 1 hour
    }
}
