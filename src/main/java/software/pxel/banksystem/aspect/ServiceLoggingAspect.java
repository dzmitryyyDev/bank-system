package software.pxel.banksystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    @Before("execution(* software.pxel.banksystem.service.impl.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("Entering method: {}.{}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName()
            );
        }
    }

    @AfterReturning("execution(* software.pxel.banksystem.service.impl.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("Exiting method: {}.{}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName()
            );
        }
    }

}
