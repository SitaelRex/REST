package ru.kata.spring.boot_security.demo.configs;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user").setViewName("users/index");
        registry.addViewController("/admin").setViewName("users/index");
    }
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
