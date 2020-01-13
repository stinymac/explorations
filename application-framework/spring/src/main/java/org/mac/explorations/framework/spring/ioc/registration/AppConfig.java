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

import org.mac.explorations.framework.spring.ioc.registration.component.Box;
import org.mac.explorations.framework.spring.ioc.registration.component.CartoonCharacter;
import org.mac.explorations.framework.spring.ioc.registration.component.Color;
import org.mac.explorations.framework.spring.ioc.registration.component.GraphicsFactoryBean;
import org.mac.explorations.framework.spring.ioc.registration.component.PlainBean;
import org.mac.explorations.framework.spring.ioc.registration.component.PlainRepository;
import org.mac.explorations.framework.spring.ioc.registration.component.Shape;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

/**
 * @auther mac
 * @date 2020-01-12
 */

/**
 * excludeFilters = Filter[] ：指定扫描的时候按照什么规则排除那些组件
 * includeFilters = Filter[] ：指定扫描的时候只需要包含哪些组件(useDefaultFilters = false 禁用默认过滤器)
 *
 * FilterType.ANNOTATION：按照注解
 * FilterType.ASSIGNABLE_TYPE：按照给定的类型；
 * FilterType.ASPECTJ：使用ASPECTJ表达式
 * FilterType.REGEX：使用正则指定
 * FilterType.CUSTOM：使用自定义规则 (实现@see {@link org.springframework.core.type.filter.TypeFilter})
 *
 */
@ComponentScan(value = "org.mac.explorations.framework.spring.ioc.registration.component",
        includeFilters = {
                @Filter (type = FilterType.ANNOTATION,classes = {Service.class} ),
                @Filter (type = FilterType.ASSIGNABLE_TYPE,classes = {PlainRepository.class} ),
                @Filter (type = FilterType.CUSTOM,classes = {SimpleCustomTypeFilter.class})
        },
        useDefaultFilters = false
)
@Import({Color.class, Shape.class,SimpleCustomImportSelector.class,SimpleCustomImportBeanDefinitionRegistrar.class})
public class AppConfig {
    /**
     * {@link ConfigurableBeanFactory#SCOPE_SINGLETON SCOPE_SINGLETON}.
     * @since 4.2
     * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
     * @see ConfigurableBeanFactory#SCOPE_SINGLETON
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
     *
     * prototype：多实例的：ioc容器启动并不会去调用方法创建对象放在容器中。每次获取的时候才会调用方法创建对象；
     * singleton：单实例的（默认值）：ioc容器启动会调用方法创建对象放到ioc容器中。以后每次获取就是直接从容器（map.get()）中取得；
     * request：同一次请求创建一个实例
     * session：同一个session创建一个实例
     *
     * 懒加载：
     *      @see org.springframework.context.annotation.Lazy
     * 		单实例bean：默认在容器启动的时候创建对象；
     * 		懒加载：容器启动不创建对象。第一次使用(获取)Bean创建对象，并初始化；
     *
     * @return
     */
    @Scope("prototype")
    //@Scope("singleton")
    //@Lazy
    @Bean
    public PlainBean plainBean(){
        System.err.println("=================PlainBean construction=================");
        return new PlainBean();
    }

    // 按照一定的条件进行判断，满足条件则向容器中注册
    // @Conditional可以在方法和类上
    @Conditional(LocaleChinaCondition.class)
    @Bean
    public CartoonCharacter rabbit() {
        return new CartoonCharacter("rabbit", LocalDate.of(2015,3,5),"CN");
    }

    @Conditional(LocaleUSCondition.class)
    @Bean
    public CartoonCharacter jerry() {
        return new CartoonCharacter("Jerry", LocalDate.of(1940,2,10),"US");
    }

    @Bean
    public GraphicsFactoryBean graphicsFactoryBean() {
        return new GraphicsFactoryBean();
    }
}

class SimpleCustomImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.err.println("===>SimpleCustomImportBeanDefinitionRegistrar bean definition names:"+ Arrays.toString(registry.getBeanDefinitionNames()));
        if (registry.containsBeanDefinition("org.mac.explorations.framework.spring.ioc.registration.component.Color")
                && registry.containsBeanDefinition("org.mac.explorations.framework.spring.ioc.registration.component.Shape")) {
            registry.registerBeanDefinition("box", new RootBeanDefinition(Box.class));
        }
    }
}

class SimpleCustomImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        System.out.println("===>importingClass:"+importingClassMetadata.getClassName());
        //返回导入到容器中的组件全类名
        return new String[]{"org.mac.explorations.framework.spring.ioc.registration.component.Animal"};
    }
}

/**
 * @see org.springframework.context.annotation.Condition
 */
class LocaleChinaCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        //1、能获取到ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //2、获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        //3、获取当前环境信息
        Environment environment = context.getEnvironment();
        //4、获取到bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();
        //可以判断容器中的bean注册情况，也可以给容器中注册bean
        boolean contains = registry.containsBeanDefinition("jerry");
        System.out.println("Jerry definition contains ? "+contains);
        //registry.registerBeanDefinition("jerry",beanDefinition);

        Locale locale = Locale.getDefault();

        if(locale.getCountry().equals("CN")) {
            return true;
        }
        return false;
    }
}
class LocaleUSCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Locale locale = Locale.getDefault();
        if(locale.getCountry().equals("US")) {
            return true;
        }
        return false;
    }
}

class SimpleCustomTypeFilter implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前类(正在扫描的类)注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //System.out.println("Annotation metadata of class scanning :"+annotationMetadata);
        //获取当前正在扫描的类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //System.out.println("Class metadata of class scanning :"+classMetadata);
        //获取当前类资源（类的路径）
        Resource resource = metadataReader.getResource();
        //System.out.println("Resource of class scanning :"+resource);

        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        for (String annotationType :annotationTypes) {
            if ("org.springframework.stereotype.Controller".equals(annotationType)) {
                //System.err.println("=========================================================");
                return true;
            }
        }
        //System.err.println("=========================================================");
        return false;
    }
}
