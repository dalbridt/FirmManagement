package pet.config;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // todo - переделать
    @Before("execution(* pet..*.*(..))")
    public void logBeforeMethod (JoinPoint joinPoint) {
        logger.info("Calling method: {} with args: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());

    }
}
