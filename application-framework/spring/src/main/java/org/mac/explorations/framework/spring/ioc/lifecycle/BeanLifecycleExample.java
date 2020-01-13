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

package org.mac.explorations.framework.spring.ioc.lifecycle;

import org.mac.explorations.framework.spring.ioc.lifecycle.bean.Car;
import org.mac.explorations.framework.spring.ioc.lifecycle.bean.Cat;
import org.mac.explorations.framework.spring.ioc.lifecycle.bean.Duck;
import org.mac.explorations.framework.spring.ioc.lifecycle.bean.SimpleBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Bean的生命周期：
 *     Bean创建--->初始化--->销毁的过程(容器管理Bean的生命周期)
 *
 * 可以自定义初始化和销毁方法；容器在Bean进行到当前生命周期的时候来调用自定义的初始化和销毁方法
 *
 * 构造（对象创建）
 *     单实例：在容器启动的时候创建对象
 * 	   多实例：在每次获取的时候创建对象
 *
 * (初始化方法调用前可以执行BeanPostProcessor.postProcessBeforeInitialization)
 * 初始化：对象创建完成，并且属性赋值完成，调用初始化方法。。。
 * (初始化方法调用后可以执行BeanPostProcessor.postProcessAfterInitialization)
 *
 * 销毁：
 *     单实例：容器关闭的时候
 * 	   多实例：容器不会管理这个Bean；容器不会调用销毁方法；
 *
 * 生命周期管理使用:
 *
 * 1、指定初始化和销毁方法；通过@Bean指定initMethod和destroyMethod；
 *
 * 2、Bean实现InitializingBean（定义初始化逻辑），DisposableBean（定义销毁逻辑）;
 *
 * 3、使用JSR250；
 *    @see javax.annotation.PostConstruct：Bean创建完成并且属性赋值完成；来执行初始化方法
 *    @see javax.annotation.PreDestroy：在容器销毁Bean之前通知进行清理工作
 *
 * 4、BeanPostProcessor接口@see {@link org.springframework.beans.factory.config.BeanPostProcessor}->Bean的后置处理器；在Bean初始化前后进行一些处理工作；
 *
 * 	  postProcessBeforeInitialization:在初始化之前工作
 * 	  postProcessAfterInitialization:在初始化之后工作
 *
 * Spring自身对BeanPostProcessor的使用 如:
 *
 *    @see org.springframework.context.support.ApplicationContextAwareProcessor
 *    @see org.springframework.validation.beanvalidation.BeanValidationPostProcessor
 *    // 对PostConstruct和PreDestro注解的处理
 *    @see org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor
 *    @see org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
 *
 *
 * @auther mac
 * @date 2020-01-13
 */
@Configuration
@Import({Cat.class,Duck.class,SimpleBeanPostProcessor.class})
public class BeanLifecycleExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanLifecycleExample.class);
        System.err.println("================context initialization complete==================");
        context.getBean(Car.class);
        ((AnnotationConfigApplicationContext)context).close();
        System.err.println("================context closed==================");
    }

    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Car car() {
        return new Car();
    }
}
