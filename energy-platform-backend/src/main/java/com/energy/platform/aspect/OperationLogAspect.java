package com.energy.platform.aspect;


import com.energy.platform.entity.OperationLog;
import com.energy.platform.mapper.OperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {
    private final OperationLogMapper mapper;
    public OperationLogAspect(OperationLogMapper mapper) { this.mapper = mapper; }
    @Around("execution(* com.energy.platform.controller..*(..)) && !within(com.energy.platform.controller.AuthController)")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        OperationLog log = new OperationLog();
        log.setMethod(pjp.getSignature().toShortString());
        log.setCreatedAt(LocalDateTime.now());
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest req = attrs.getRequest();
            log.setRequestUri(req.getRequestURI()); log.setIp(req.getRemoteAddr());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.setUsername(auth == null ? "anonymous" : auth.getName());
        try {
            Object result = pjp.proceed();
            log.setStatus("SUCCESS"); log.setOperation("接口访问"); mapper.insert(log);
            return result;
        } catch (Throwable e) {
            log.setStatus("FAILED"); log.setErrorMessage(e.getMessage()); mapper.insert(log); throw e;
        }
    }
}
