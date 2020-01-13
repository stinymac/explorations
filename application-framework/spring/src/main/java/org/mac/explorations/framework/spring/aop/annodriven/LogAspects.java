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

package org.mac.explorations.framework.spring.aop.annodriven;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/**
 * @auther mac
 * @date 2020-01-13
 */
@Aspect
public class LogAspects {
    /**
     * 抽取公共的切入点表达式
     * 1、本类引用
     * 2、其他的切面引用
     *
     */
    @Pointcut("execution(public * org.mac.explorations.framework.spring.aop.annodriven.PlainMathematicalCalculationsService.*(..))")
    public void pointCut(){}

    //在目标方法之前切入；切入点表达式(指定在哪个方法切入)
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        System.err.println(pointCutMethodSignature(joinPoint) + " start execute");
    }

    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint){
        System.err.println(pointCutMethodSignature(joinPoint) + " finish execute");
    }

    //JoinPoint一定是参数列表中的第一参数 否则"IllegalArgumentException:0 formal unbound in pointcut"
    @AfterReturning(value = "pointCut()",returning = "result")
    public void logReturn(JoinPoint joinPoint,Object result){
        System.err.println(pointCutMethodSignature(joinPoint) + " returning and return value:["+result+"]");
    }

    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void logReturn(JoinPoint joinPoint,Exception exception){
        System.err.println(pointCutMethodSignature(joinPoint) + " throw exception:["+exception+"]");
    }

    private String pointCutMethodSignature(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName() + "("+ Arrays.toString(joinPoint.getArgs())+")";
    }
}
