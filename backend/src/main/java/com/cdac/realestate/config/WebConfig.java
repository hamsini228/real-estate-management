package com.cdac.realestate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /photos/** to the local directory
        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:///C:/Users/pilli/OneDrive/Desktop/photos/");
        
        // Serve uploaded files from the local project workspace
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
