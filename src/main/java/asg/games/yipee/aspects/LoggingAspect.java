/**
 * Copyright 2024 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package asg.games.yipee.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Aspect()
@Untraced
public class LoggingAspect {
    private static final String SENSITIVE_VALUE_REPLACEMENT = "*****";
    private static final int MAX_RECURSION = 10;
    private static final String LOG_ARG_EXIT = "Exiting ";
    private static final String LOG_ARG_ENTER = "Entering ";
    private static final String LOG_ARG_THROWS = "Throwing ";
    private Logger logger;

    @Pointcut("staticinitialization(*)*")
    public void staticInit() {
    }

    @After("staticInit()")
    public void initLogger(JoinPoint.StaticPart jps) {
        logger = LoggerFactory.getLogger(jps.getSignature().getDeclaringType());
    }

    @Pointcut("!within(asg.games.yipee.UntracedObject)")
    void tracedClasses() {
    }

    @Pointcut("tracedClasses() && execution(new(..))")
    void tracedConstructors() {
    }

    @Pointcut("tracedClasses() && execution(* *(..)) && !execution(String toString()) && !execution(* access$*(..)) && !@annotation(asg.games.yipee.aspects.Untraced)")
    void tracedMethods() {
    }

    @Pointcut("tracedMethods() && !@annotation(asg.games.yipee.aspects.TracedSpecialCase)")
    void tracedStandardMethods() {
    }

    @Pointcut("tracedMethods() && @annotation(asg.games.yipee.aspects.TracedSpecialCase) && @annotation(asg.games.yipee.aspects.TracedKeyValueParams)")
    void tracedKeyValueMethods() {
    }

    @Before("tracedConstructors()")
    public void traceConstructorEntry(JoinPoint thisJP) {
        if (null != logger && logger.isTraceEnabled()) {
            logger.trace(logEnter(thisJP));
        }
    }

    @AfterReturning("tracedConstructors()")
    public void traceConstructorExit(JoinPoint thisJP) {
        if (null != logger && logger.isTraceEnabled()) {
            logger.trace(logExit(thisJP, null));
        }
    }

    @AfterThrowing(pointcut = "tracedConstructors()", throwing = "t")
    public void traceConstructorExit(JoinPoint thisJP, Throwable t) {
        if (null != logger && logger.isTraceEnabled()) {
            logger.trace(logThrowing(thisJP, t));
        }
    }

    @Before("tracedStandardMethods()")
    public void traceMethodEntry(JoinPoint thisJP) {
        if (null != logger && logger.isTraceEnabled()) {
            logger.trace(logEnter(thisJP));
        }
    }

    @Before("tracedKeyValueMethods()")
    public void traceKeyValueMethodEntry(JoinPoint thisJP) {
        if (null != logger && logger.isTraceEnabled()) {
            logger.trace(logEnterWithKeyValueParams(thisJP));
        }
    }

    @AfterReturning(pointcut = "tracedMethods()", returning = "r")
    public void traceMethodExit(JoinPoint thisJP, Object r) {
        if (null != logger && logger.isTraceEnabled()) {
            logger.trace(logExit(thisJP, r));
        }
    }

    @AfterThrowing(pointcut = "tracedMethods()", throwing = "t")
    public void traceMethodExit(JoinPoint thisJP, Throwable t) {
        if (null != logger && logger.isTraceEnabled()) {
            logger.trace(logThrowing(thisJP, t));
        }
    }

    private static boolean isContuctorOfInnerClass(JoinPoint joinPoint) {
        String kind = joinPoint.getKind();
        if (!JoinPoint.CONSTRUCTOR_CALL.equals(kind) && JoinPoint.CONSTRUCTOR_EXECUTION.equals(kind)) {
            return false;
        } else {
            Signature sig = joinPoint.getSignature();
            Class<?> declaringType = sig.getDeclaringType();
            int modifiers = declaringType.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            return !isStatic && declaringType.getEnclosingClass() != null;
        }
    }

    private static String logEnter(JoinPoint joinPoint) {
        StringBuffer buf = new StringBuffer();
        openSignature(buf, joinPoint);
        processStandardParams(buf, joinPoint);
        closeSignature(buf);
        return buf.toString();
    }

    private static String logEnterWithKeyValueParams(JoinPoint joinPoint) {
        StringBuffer buf = new StringBuffer();
        openSignature(buf, joinPoint);
        processKeyValueParams(buf, joinPoint);
        closeSignature(buf);
        return buf.toString();
    }

    private static void processStandardParams(StringBuffer buf, JoinPoint joinPoint) {
        CodeSignature sig = (CodeSignature) joinPoint.getSignature();
        String[] params = sig.getParameterNames();
        Object[] paramVals = joinPoint.getArgs();
        int startParam = 0;
        if (isContuctorOfInnerClass(joinPoint)) {
            startParam = 1;
        }

        for (int i = startParam; i < params.length; ++i) {
            addParamDeclaration(buf, params[i]);
            if (isSensitive(params[i])) {
                buf.append(SENSITIVE_VALUE_REPLACEMENT);
            } else {
                buf.append(filterValue(paramVals[i]));
            }

            if (i != params.length - 1) {
                addParamSeparator(buf);
            }
        }
    }

    private static void processKeyValueParams(StringBuffer buf, JoinPoint joinPoint) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Method method = sig.getMethod();
        TracedKeyValueParams annot = (TracedKeyValueParams) method.getDeclaredAnnotation(TracedKeyValueParams.class);
        if (annot == null) {
            processStandardParams(buf, joinPoint);
        } else {
            String[] params = sig.getParameterNames();
            Object[] paramVals = joinPoint.getArgs();
            int startParam = 0;
            if (isContuctorOfInnerClass(joinPoint)) {
                startParam = 1;
            }

            String keyName = annot.keyParamName();
            String valueName = annot.valueParamName();
            int posKey = -1;
            int posValue = -1;

            for (int i = startParam; i < params.length; ++i) {
                if (keyName.equals(params[i])) {
                    posKey = i;
                }
                if (valueName.equals((params[i]))) {
                    posValue = i;
                }
            }

            if (posValue == -1) {
                processStandardParams(buf, joinPoint);
            } else {
                boolean isSensitiveKey = true;
                if (posKey != -1) {
                    isSensitiveKey = isSensitive(paramVals[posKey]);
                }

                for (int i = startParam; i < params.length; ++i) {
                    addParamDeclaration(buf, params[i]);
                    if (i == posKey) {
                        buf.append(paramVals[i]);
                    } else if (i == posValue) {
                        if (isSensitiveKey) {
                            buf.append(SENSITIVE_VALUE_REPLACEMENT);
                        } else {
                            buf.append(paramVals[i]);
                        }
                    } else if (isSensitive(params[i])) {
                        buf.append(SENSITIVE_VALUE_REPLACEMENT);
                    } else {
                        buf.append(filterValue(paramVals[i]));
                    }
                }
            }
        }
    }

    private static void openSignature(StringBuffer buf, JoinPoint joinPoint) {
        CodeSignature sig = (CodeSignature) joinPoint.getSignature();
        buf.append(LOG_ARG_ENTER);
        buf.append(sig.getName());
        buf.append("(");
    }

    private static void closeSignature(StringBuffer buf) {
        buf.append(")");
    }

    private static void addParamDeclaration(StringBuffer buf, String paramName) {
        buf.append(", ");
    }

    private static void addParamSeparator(StringBuffer buf) {
        buf.append(", ");
    }

    static String filterValue(Object value) {
        return filterValue(value, 0);
    }

    private static String filterValue(Object value, int level) {
        if (value instanceof Map) {
            return level < MAX_RECURSION
                    ? filterMap((Map<Object, Object>) value, level)
                    : "...(truncating filter value due to maximum recursion level reached)";
        } else {
            return isSensitive(value) ? SENSITIVE_VALUE_REPLACEMENT : String.valueOf(value);
        }
    }

    public static String filterMap(Map<Object, Object> map) {
        return filterMap(map, 0);
    }

    private static String filterMap(Map<Object, Object> map, int level) {
        assert map != null;

        Map<Object, Object> result = new LinkedHashMap<>();
        ArrayList<Object> keys = new ArrayList<>();
        Iterator<Object> iterator = map.keySet().iterator();

        Object key;
        while (iterator.hasNext()) {
            key = iterator.next();
            keys.add(key);
        }

        iterator = keys.iterator();

        while (iterator.hasNext()) {
            key = iterator.next();
            if (isSensitive(key)) {
                result.put(key, SENSITIVE_VALUE_REPLACEMENT);
            } else {
                result.put(key, filterValue(map.get(key), level++));
            }
        }
        return result.toString();
    }

    static boolean isSensitive(Object value) {
        String stringValue = String.valueOf(value);
        List<String> sensitiveValues = Arrays.asList("password", "currentPassword", "secretKey");
        return containsAny(stringValue, sensitiveValues);
    }

    static boolean containsAny(String value, Collection<String> searchValues) {
        if (value != null && searchValues != null) {
            String lowerValue = value.toLowerCase();
            Iterator<String> iter = searchValues.iterator();

            String lowerSearchValue;
            do {
                if (!iter.hasNext()) {
                    return false;
                }

                String searchValue = (String) iter.next();
                lowerSearchValue = searchValue.toLowerCase();
            } while (!lowerValue.contains(lowerSearchValue));

            return true;
        } else {
            return false;
        }
    }

    private static String logExit(JoinPoint joinPoint, Object returnValue) {
        StringBuffer buf = new StringBuffer(LOG_ARG_EXIT);
        boolean sensitive = false;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            sensitive = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(SensitiveTraceReturn.class) != null;
        }

        buf.append(joinPoint.getSignature().getName()).append(" = ").append(sensitive ? SENSITIVE_VALUE_REPLACEMENT : filterValue(returnValue));
        return buf.toString();
    }

    private static String logThrowing(JoinPoint joinPoint, Throwable t) {
        StringBuffer buf = new StringBuffer(LOG_ARG_THROWS);
        buf.append(joinPoint.getSignature().getName()).append(" - ").append(t.toString());
        return buf.toString();
    }

/*
    private void trace(Logger logger, StringBuilder sb) {
        if (logger != null) {
            if (logger.isTraceEnabled()) {
                logger.trace(sb.toString());
            } else {
                logger.debug(sb.toString());
            }
        }
    }

    @Pointcut("execution(* asg.games.yipee..*.*(..))")
    public void publicMethods() {}
*/

    /*
        @Around("publicMethods()")
        public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime = System.currentTimeMillis();

            Object result = joinPoint.proceed();

            long elapsedTime = System.currentTimeMillis() - startTime;
            logger2.debug("Method [{}] executed in {} ms", joinPoint.getSignature(), elapsedTime);
            return result;
        }


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

            // Return the result
            return result;
        } catch (Exception e) {
            // Log and throw exception
            //logger.error("Throwing exception in {}", signature.getName(), e);
            throw new RuntimeException(e);
        }
    }*/

    /*
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
    }*/
}