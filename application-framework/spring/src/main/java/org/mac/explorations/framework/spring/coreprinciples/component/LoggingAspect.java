/*
 *          (          (
 *          )\ )  (    )\   )  )     (
 *  (  (   (()/( ))\( ((_| /( /((   ))\
 *  )\ )\   ((_))((_)\ _ )(_)|_))\ /((_)
 * ((_|(_)  _| (_))((_) ((_)__)((_|_))
 * / _/ _ \/ _` / -_|_-< / _` \ V // -_)
 * \__\___/\__,_\___/__/_\__,_|\_/ \___|
 *
 * 东隅已逝，桑榆非晚。(The time has passed,it is not too late.)
 * 虽不能至，心向往之。(Although I can't, my heart is longing for it.)
 *
 */

package org.mac.explorations.framework.spring.coreprinciples.component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @auther mac
 * @date: 2020-01-17 22:49
 */
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(public * org.mac.explorations.framework.spring.coreprinciples.component.service.SimpleSampleService.*(..))")
    public void pointCut(){}

    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        System.err.println(pointCutMethodSignature(joinPoint) + " start execute");
    }

    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint){
        System.err.println(pointCutMethodSignature(joinPoint) + " finish execute");
    }

    @AfterReturning(value = "pointCut()",returning = "result")
    public void logReturn(JoinPoint joinPoint,Object result){
        System.err.println(pointCutMethodSignature(joinPoint) + " returning and return value:["+result+"]");
    }

    private String pointCutMethodSignature(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName() + "("+ Arrays.toString(joinPoint.getArgs())+")";
    }
}
