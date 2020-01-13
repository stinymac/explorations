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

package org.mac.explorations.framework.spring.ioc.registration;

import org.mac.explorations.framework.spring.ioc.registration.component.CartoonCharacter;
import org.mac.explorations.framework.spring.ioc.registration.component.GraphicsFactoryBean;
import org.mac.explorations.framework.spring.ioc.registration.component.PlainBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.stream.Stream;

/**
 * 给容器中注册组件
 *
 * 1、包扫描,组件标注注解(@Controller/@Service/@Repository/@Component 一般是自定义的类)
 *
 * 2、@Bean(导入的第三方包里面的组件)
 *
 * 3、@Import(快速给容器中导入一个组件)
 * 		1)、@Import(要导入到容器中的组件)；容器中就会自动注册这个组件，id默认是全类名
 * 		2)、ImportSelector:返回需要导入的组件的全类名数组；
 * 		3)、ImportBeanDefinitionRegistrar:手动注册bean到容器中
 *
 * 4、使用Spring提供的 FactoryBean(工厂Bean);
 * 		1)、默认获取到的是工厂bean调用getObject创建的对象
 * 		2)、要获取工厂Bean本身，需要给id前面加一个&
 *
 * @auther mac
 * @date 2020-01-12
 */
public class ComponentRegisterExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.err.println("=================context initialization complete=================");
        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
        // scope("prototype") - false
        System.out.println(context.getBean(PlainBean.class) == context.getBean(PlainBean.class));

        // CartoonCharacter 条件注入
        Stream.of(context.getBeansOfType(CartoonCharacter.class).values()).forEach(System.out::println);

        // FactoryBean

        System.out.println(context.getBean("graphicsFactoryBean"));
        System.out.println(context.getBean("&graphicsFactoryBean"));
        System.out.println(context.getBean(GraphicsFactoryBean.class));
    }
}
