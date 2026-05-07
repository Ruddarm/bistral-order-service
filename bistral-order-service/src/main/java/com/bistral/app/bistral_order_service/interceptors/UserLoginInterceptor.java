package com.bistral.app.bistral_order_service.interceptors;

import com.bistral.app.bistral_order_service.contexts.AuthContext;
import com.bistral.app.bistral_order_service.contexts.UserContextHolder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.err.println("Inside Interceptor");
        String userId = request.getHeader("X-User-Id");
        String bistroId = request.getHeader("X-Bistro-Id");
        String branchId = request.getHeader("X-Branch-Id");
        String roleId = request.getHeader("X-Role-Id");
        Set<String> permissions = null;
        if (request.getHeader("X-Permissions") != null && !request.getHeader("X-Permissions").trim().isEmpty()) {
            permissions = new ObjectMapper()
                    .readValue(Base64.getDecoder().decode(request.getHeader("X-Permissions")),
                            new TypeReference<Set<String>>() {
                            });
        }
        System.err.println("Bistro Id is " + bistroId);

        if (userId != null) {
            UserContextHolder.setAuthContext(
                    AuthContext.builder()
                            .roleId(roleId == null ? null : UUID.fromString(roleId))
                            .bistroId(bistroId == null ? null : UUID.fromString(bistroId))
                            .branchId(branchId == null ? null : UUID.fromString(branchId))
                            .userId(UUID.fromString(userId))
                            .permissions(permissions == null ? new HashSet<>() : permissions)
                            .build()
            );
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
