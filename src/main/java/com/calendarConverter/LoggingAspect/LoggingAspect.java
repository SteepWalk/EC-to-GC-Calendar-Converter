package com.calendarConverter.LoggingAspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LogManager.getLogger(LoggingAspect.class);

    public LoggingAspect() {
        System.out.println("Logging initialized...");
    }

    @Around("execution(* com.calendarConverter.utility..*(..)) " +
            "|| execution(* com.calendarConverter.service..*(..)) " +
            "|| execution(* com.calendarConverter.utility.ECOperations.*(..))" +
            "|| execution(* com.calendarConverter.utility.GCOperations.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        logger.info("Inside method " + methodName);
        Object result;
        try {
            result = joinPoint.proceed();
            if (joinPoint.getSignature() instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
                Class<?> returnType = methodSignature.getReturnType();

                if (returnType.isArray()) {
                    Class<?> componentType = returnType.getComponentType();
                    if (componentType == int.class) {
                        logger.info(methodName + " returns an array of int: " + java.util.Arrays.toString((int[]) result));
                    } else {
                        logger.info(methodName + " returns an array of: " + componentType.getName());
                    }
                } else if (returnType.isPrimitive()) {
                    logger.info(methodName + " returns a primitive type object: " + result);
                } else if (returnType == void.class) {
                    logger.info(methodName + " does not return an object.");
                } else {
                    logger.info(methodName + " returns a non-primitive object: " + result.getClass());
                }
            }
        } finally {
            logger.info("Exiting method " + methodName + "\n\n\n");
        }

        return result;
    }

}