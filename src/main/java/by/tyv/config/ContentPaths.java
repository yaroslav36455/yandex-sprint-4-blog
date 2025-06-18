package by.tyv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContentPaths {

    @Value("${spring.application.content-path}")
    private String imageDir;

    public String getImagePathStr() {
        return imageDir;
    }
}
