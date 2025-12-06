package com.emrsystem.global.security.authorization;

import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAuthorizationAspect {

    @Before("@annotation(com.emrsystem.global.security.authorization.RequireRole)")
    public void checkRole(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);

        if (requireRole == null) {
            return;
        }

        RoleType requiredRole = requireRole.value();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CommonException(ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Optional<RoleType> userHighestRole = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> {
                    try {
                        return RoleType.fromCode(authority);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(roleType -> roleType != null)
                .min((r1, r2) -> Integer.compare(r1.getLevel(), r2.getLevel()));

        if (userHighestRole.isEmpty()) {
            throw new CommonException(ErrorCode.FORBIDDEN, "권한이 없습니다.");
        }

        RoleType userRole = userHighestRole.get();
        if (userRole.hasLowerAuthority(requiredRole)) {
            throw new CommonException(ErrorCode.FORBIDDEN,
                    String.format("%s 이상의 권한이 필요합니다.", requiredRole.getName()));
        }
    }
}

