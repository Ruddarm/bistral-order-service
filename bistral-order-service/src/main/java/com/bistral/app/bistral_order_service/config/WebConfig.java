package com.bistral.app.bistral_order_service.config;

import com.bistral.app.bistral_order_service.interceptors.UserLoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserLoginInterceptor userLoginInterceptor;
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // sab endpoints ke liye
//                        .allowedOrigins("http://localhost:5173") // frontend origin
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
//            }
//        };
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(userLoginInterceptor);
    }
}