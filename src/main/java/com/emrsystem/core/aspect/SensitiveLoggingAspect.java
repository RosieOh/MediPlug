package com.emrsystem.core.aspect;

import com.emrsystem.core.annotation.LogSensitive;
import com.emrsystem.global.audit.AccessLog;
import com.emrsystem.global.audit.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SensitiveLoggingAspect {

    private final AccessLogRepository accessLogRepository;

    @Pointcut("@annotation(com.emrsystem.core.annotation.LogSensitive)")
    public void logSensitivePointcut() {}

    @AfterReturning("logSensitivePointcut() && @annotation(annotation)")
    public void afterSensitive(JoinPoint jp, LogSensitive annotation) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        String resource = annotation.resource();
        String action = annotation.action();
        String details = jp.getSignature().toShortString();
        accessLogRepository.save(AccessLog.of(username, resource, action, details));
    }
}


