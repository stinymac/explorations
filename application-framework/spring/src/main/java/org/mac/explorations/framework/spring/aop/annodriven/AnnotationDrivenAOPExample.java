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

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * AOP:[动态代理] 指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式；
 *
 * 注解驱动的 Spring AOP 的使用示例:
 *
 * 1、导入aop模块；Spring AOP：(spring-aspects)
 * 2、定义一个业务逻辑类(PlainMathematicalCalculationsService)；在业务逻辑运行的时候将日志进行打印(方法之前、方法运行结束、方法出现异常)
 * 3、定义一个日志切面类(LogAspects)：切面类里面的方法需要动态感知PlainMathematicalCalculationsService.divide(int int)运行到哪里然后执行；
 * 	 通知方法：
 * 			前置通知(@Before)：logStart：在目标方法(divide)运行之前运行
 * 			后置通知(@After)：logEnd：在目标方法(divide)运行结束之后运行（无论方法正常结束还是异常结束）
 * 			返回通知(@AfterReturning)：logReturn：在目标方法(divide)正常返回之后运行
 * 			异常通知(@AfterThrowing)：logException：在目标方法(divide)出现异常以后运行
 * 			环绕通知(@Around)：动态代理，手动推进目标方法运行[joinPoint.proceed()]
 *
 * 4、给切面类的目标方法标注何时何地运行(通知注解)；
 *
 * 5、将切面类(切面类上加一个注解：@Aspect 告诉Spring哪个类是切面类)和业务逻辑类（目标方法所在类）都加入到容器中;
 *
 * 6、给配置类中加 @EnableAspectJAutoProxy [开启基于注解的aop模式]
 *
 * =====================================================================================================================
 *
 *
 * =====================================================================================================================
 *
 * @auther mac
 * @date 2020-01-13
 */
@Configuration
@EnableAspectJAutoProxy
@Import({PlainMathematicalCalculationsService.class,LogAspects.class})
public class AnnotationDrivenAOPExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AnnotationDrivenAOPExample.class);
        PlainMathematicalCalculationsService mathematicalCalculationsService = context.getBean(PlainMathematicalCalculationsService.class);
        mathematicalCalculationsService.divide(100,5);
    }
}
