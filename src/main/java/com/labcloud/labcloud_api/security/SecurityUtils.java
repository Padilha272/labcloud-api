package com.labcloud.labcloud_api.security;

import com.labcloud.labcloud_api.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    // Retorna o usuário autenticado atual
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Nenhum usuário autenticado");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof User) {
            return (User) principal;
        }
        
        throw new RuntimeException("Usuário não encontrado no contexto");
    }

    // Retorna o ID do usuário autenticado
    public static String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    // Retorna o tenantId do usuário autenticado
    public static String getCurrentTenantId() {
        return getCurrentUser().getTenantId();
    }

    // Retorna o email do usuário autenticado
    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }
}