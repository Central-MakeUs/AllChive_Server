package allchive.server.api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:3000");
        allowedOriginPatterns.add("http://localhost:8080");
        allowedOriginPatterns.add("https://www.allchive.co.kr");

        String[] patterns = allowedOriginPatterns.toArray(String[]::new);
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns(patterns)
                .allowCredentials(true)
                .maxAge(3600L);
    }
}