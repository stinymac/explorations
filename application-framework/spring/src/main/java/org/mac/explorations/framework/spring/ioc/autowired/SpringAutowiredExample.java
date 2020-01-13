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

package org.mac.explorations.framework.spring.ioc.autowired;

import org.mac.explorations.framework.spring.ioc.autowired.bean.Person;
import org.mac.explorations.framework.spring.ioc.autowired.bean.PersonRepository;
import org.mac.explorations.framework.spring.ioc.autowired.bean.PersonService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * 自动装配
 *
 * Spring利用依赖注入(DI)，完成对IOC容器中中各个组件的依赖关系赋值；
 *
 * 1、@Autowired：自动注入：
 * 		1)、默认优先按照类型去容器中找对应的组件:applicationContext.getBean(Type.class);找到就赋值;
 * 		2)、如果找到多个相同类型的组件，抛出NoUniqueBeanDefinitionException;
 * 		3)、自动装配默认一定要将属性赋值好，没有就会报错；可以使用@Autowired(required=false);
 * 		4)、@Primary：Spring进行自动装配的时候，默认使用首选的Bean;
 *
 *     AutowiredAnnotationBeanPostProcessor:解析完成自动装配功能；
 *
 * 2、Spring还支持使用@Resource(JSR250)和@Inject(JSR330)[java规范的注解]
 *
 * 		@Resource:
 * 			可以和@Autowired一样实现自动装配功能；默认是按照组件名称进行装配的；
 * 			不支持 @Primary 不支持@Autowired(required=false);
 *
 * 		@Inject:
 * 			需要导入javax.inject的包，和Autowired的功能一样。没有required=false的功能；
 *
 * 3、 @Autowired:构造器，参数，方法，属性；都是从容器中获取参数组件的值
 * 		1)、[标注在方法位置]：参数从容器中获取;默认不写@Autowired效果是一样的；都能自动装配
 * 		2)、[标在构造器上]：如果组件只有一个有参构造器，这个有参构造器的@Autowired可以省略，参数位置的组件还是可以自动从容器中获取
 * 		3)、放在参数位置
 *
 * 4、自定义组件想要使用Spring容器底层的一些组件(ApplicationContext，BeanFactory，...)；
 * 	 需要自定义组件实现对应的Aware接口；在创建对象的时候，会调用接口规定的方法注入相关组件；
 *   @see org.springframework.beans.factory.Aware
 *
 *       @see org.springframework.context.ApplicationContextAware
 *       @see org.springframework.beans.factory.BeanNameAware
 *       @see org.springframework.context.EmbeddedValueResolverAware
 *       ......
 *
 *       这些Aware都是使用对应的BeanPostProsessor来处理的
 *
 * @auther mac
 * @date 2020-01-13
 */
@Configuration
@ComponentScan("org.mac.explorations.framework.spring.ioc.autowired.bean")
//@PropertySource读取外部配置文件中的k/v保存到运行的环境变量中;加载完外部的配置文件以后使用${}取出配置文件的值
@PropertySource({"classpath:/person.properties"})
public class SpringAutowiredExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringAutowiredExample.class);
        Person person = context.getBean(Person.class);
        System.out.println(person);
        System.out.println(context.getEnvironment().getProperty("person.nickName"));

        System.out.println(context.getBean(PersonRepository.class));
        System.out.println(context.getBean(PersonService.class));
    }

    @Bean
    @Primary
    public PersonRepository otherPersonRepository () {
        return new PersonRepository(" by config @Bean @Primary");
    }
}
