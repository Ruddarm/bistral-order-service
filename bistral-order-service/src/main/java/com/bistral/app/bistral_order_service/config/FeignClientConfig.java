package com.bistral.app.bistral_order_service.config;

import com.bistral.app.bistral_order_service.contexts.AuthContext;
import com.bistral.app.bistral_order_service.contexts.UserContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return template -> {

            AuthContext auth = UserContextHolder.getAuthContext();

            System.err.println("Auth context is "+auth);

            if (auth != null) {

                template.header(
                        "X-User-Id",
                        auth.getUserId().toString()
                );

                if (auth.getBistroId() != null) {
                    template.header(
                            "X-Bistro-Id",
                            auth.getBistroId().toString()
                    );
                }

                if (auth.getBranchId() != null) {
                    template.header(
                            "X-Branch-Id",
                            auth.getBranchId().toString()
                    );
                }

                if (auth.getRoleId() != null) {
                    template.header(
                            "X-Role-Id",
                            auth.getRoleId().toString()
                    );
                }
            }
        };
    }
}

