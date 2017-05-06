package no.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    private static final String CLASSPATH_RESOURCE_LOCATIONS = "classpath:/static/b2b/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/b2b/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}

