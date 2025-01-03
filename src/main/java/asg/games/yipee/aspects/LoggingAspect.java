/**
 * Copyright 2024 See AUTHORS file.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {
    public static final String LOG_ARG_EXIT = "Exiting ";
    public static final String LOG_ARG_ENTER = "Entering ";

    @Pointcut("execution(* asg.games.yipee..*.*(..))")
    public void publicMethods() {
    }
    /*
        @Around("publicMethods()")
        public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime = System.currentTimeMillis();

            Object result = joinPoint.proceed();

            long elapsedTime = System.currentTimeMillis() - startTime;
            logger2.debug("Method [{}] executed in {} ms", joinPoint.getSignature(), elapsedTime);
            return result;
        }
    */

    @Around("publicMethods()")
    public Object logMethodEntryAndExit(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());

        try {
            Object[] args = joinPoint.getArgs();

            logEnter(logger, joinPoint, args);

            // Execute the target method and capture the return value
            Object result = joinPoint.proceed();

            // Log method exit and return value
            logExit(logger, joinPoint, result);
            //logger.debug("Exiting method [{}] with result: {}", joinPoint.getSignature(), result);

            // Return the result
            return result;
        } catch (Exception e) {
            // Log and throw exception
            //logger.error("Throwing exception in {}", signature.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private void logEnter(Logger logger, ProceedingJoinPoint joinPoint, Object[] args) {
        if (logger != null && joinPoint != null) {
            Signature signature = joinPoint.getSignature();
            String methodName = signature.getName();

            StringBuilder sb = new StringBuilder();
            sb.append(LOG_ARG_ENTER);
            sb.append(methodName);
            sb.append("(");

            String prefix = "";
            for (Object arg : args) {
                if (arg != null) {
                    sb.append(prefix);
                    prefix = ", ";
                    sb.append(arg.getClass().getSimpleName());
                    sb.append("=");
                    sb.append(arg);
                }
            }
            sb.append(")");
            trace(logger, sb);
        }
    }

    private void logExit(Logger logger, ProceedingJoinPoint joinPoint, Object result) {
        if (logger != null && joinPoint != null) {
            Signature signature = joinPoint.getSignature();
            String methodName = signature.getName();

            StringBuilder sb = new StringBuilder();
            sb.append(LOG_ARG_EXIT);
            sb.append(methodName);
            sb.append("()= ");
            sb.append(result);
            trace(logger, sb);
        }
    }

    private void trace(Logger logger, StringBuilder sb) {
        if (logger != null) {
            if (logger.isTraceEnabled()) {
                logger.trace(sb.toString());
            } else {
                logger.debug(sb.toString());
            }
        }
    }
}