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

package org.mac.explorations.framework.springboot.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * SpringBoot 应用入口
 *
 * 注解@SpringBootApplication标记的类被SpringBoot作为其启动入口
 * 通过Main方法执行
 * @see org.springframework.boot.SpringApplication#run(java.lang.Class, java.lang.String...)
 * 启动SpringBoot应用
 *
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * <pre>
 *     .........
 *     @SpringBootConfiguration
 *     @EnableAutoConfiguration
 *     @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
 *                @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
 *     public @interface SpringBootApplication {
 *      ......
 *     }
 * </pre>
 * 注解@SpringBootApplication是一个组合注解
 *
 * @SpringBootConfiguration - 指定配置类
 * @EnableAutoConfiguration - 指定自动配置包
 * @see org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * <pre>
 *     ......
 *     @AutoConfigurationPackage
 *     @Import(AutoConfigurationImportSelector.class)
 *     public @interface EnableAutoConfiguration {
 *         ......
 *     }
 * </pre>
 *
 * 注解@AutoConfigurationPackage功能
 *
 * @see org.springframework.boot.autoconfigure.AutoConfigurationPackages.Registrar
 * <pre>
 *      ......
 *     	register(registry, new PackageImport(metadata).getPackageName());
 *     	......
 * </pre>
 * @see org.springframework.boot.autoconfigure.AutoConfigurationPackages#register(org.springframework.beans.factory.support.BeanDefinitionRegistry, java.lang.String...)
 * 将启动Main函数所在的类所在的包为basePackage,即将basePackage其下的所有组件Bean注册到容器
 *
 * 注解@Import(AutoConfigurationImportSelector.class)功能
 *
 * @see org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#selectImports(org.springframework.core.type.AnnotationMetadata)
 * @see org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#getCandidateConfigurations(org.springframework.core.type.AnnotationMetadata, org.springframework.core.annotation.AnnotationAttributes)
 * <pre>
 *     List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
 * 				getBeanClassLoader());
 * </pre>
 * 加载默认指定的自动配置类
 * @see org.springframework.core.io.support.SpringFactoriesLoader#loadSpringFactories(java.lang.ClassLoader)
 * <pre>
 *     ......
 *     // public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
 *     Enumeration<URL> urls = (classLoader != null ?
 * 					classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
 * 					ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
 * 	  ......
 * </pre>
 * 即从*.jar！META-INF/spring.factories文件中
 * 解析默认的自动装配配置类和其他一些组件
 *
 * @auther mac
 * @date 2020-01-31 12:49
 */
@SpringBootApplication
//@PropertySource注解默认只能处理.properties文件 不能处理.yml/.yaml文件
@PropertySource(value = {"classpath:person.properties"})
//@ImportResource
public class SpringBootAutoConfigureProcessAnalysisApplication {
    /**
     * SpringBoot 自动配置流程
     *
     * 1.Main方法驱动SpringBoot启动
     * 调用SpringApplication的run方法驱动Spring容器启动
     * @see org.springframework.boot.SpringApplication#run(java.lang.Class[], java.lang.String[])
     * <pre>
     *     public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
     * 		   // 指定应用启动入口类 即被@SpringBootApplication注解标记的类
     * 		   return new SpringApplication(primarySources).run(args);
     *     }
     * </pre>
     * 首先创建了SpringApplication对象实例
     * @see org.springframework.boot.SpringApplication#SpringApplication(org.springframework.core.io.ResourceLoader, java.lang.Class[])
     * <pre>
     *      this.resourceLoader = resourceLoader;// null
     *
     * 		Assert.notNull(primarySources, "PrimarySources must not be null");
     * 		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
     *
     *      // 自动识别(通过判断类路径下特定的类是否存在)WEB应用类型 即 REACTIVE NONE(非WEB应用) SERVLET
     *      // @see {@link org.springframework.boot.WebApplicationType#deduceFromClasspath()}
     * 		this.webApplicationType = WebApplicationType.deduceFromClasspath();
     *
     *      // 加载类路径下META-INF/spring.factories文件中指定应用上下文初始化器,并通过反射创建实例对象后对它们排序
     *      // 完成后保存到SpringApplication类型实例对象的initializers属性中
     *
     *      // 0 = "org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer"
     *      // 1 = "org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener"
     *      // 2 = "org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer"
     *      // 3 = "org.springframework.boot.context.ContextIdApplicationContextInitializer"
     *      // 4 = "org.springframework.boot.context.config.DelegatingApplicationContextInitializer"
     *      // 5 = "org.springframework.boot.rsocket.context.RSocketPortInfoApplicationContextInitializer"
     *      // 6 = "org.springframework.boot.web.context.ServerPortInfoApplicationContextInitializer"
     *
     *      // @see {@link org.springframework.core.io.support.SpringFactoriesLoader#loadSpringFactories(java.lang.ClassLoader)}
     * 		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
     *
     * 	    // 按同initializers一样的方法加载ApplicationListener
     * 		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
     *
     * 	    // 查找Main方法所在的类
     * 		this.mainApplicationClass = deduceMainApplicationClass();
     * </pre>
     * 执行SpringApplication的run方法
     * @see org.springframework.boot.SpringApplication#run(java.lang.String...)
     *
     * 2.SpingApplication的启动过程
     * <pre>
     *     // 创建环境对象 当前WEB环境创建
     *     // @see {@link org.springframework.web.context.support.StandardServletEnvironment}
     *     // 创建后会对环境对象做一些初始化
     *     // @see {@link org.springframework.boot.SpringApplication#prepareEnvironment(org.springframework.boot.SpringApplicationRunListeners, org.springframework.boot.ApplicationArguments)}
     *     ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
     *
     * 	   configureIgnoreBeanInfo(environment);
     * 	   Banner printedBanner = printBanner(environment);
     *
     * 	   // 创建Spring应用上下文
     * 	   // @see {@link org.springframework.boot.SpringApplication#createApplicationContext()}
     * 	   // 当前WEB应用创建的是
     * 	   // @see {@link org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext}
     * 	   // 上下文的创建即一系列对象的初始化 核心是 创建BeanFactory
     * 	   // <pre>
     * 	   // public GenericApplicationContext() {
     * 	   //   this.beanFactory = new DefaultListableBeanFactory();
     *     // }
     * 	   // </pre>
     * 	   // 以及设置注解读取器和条件解析器
     * 	   // @see {@link org.springframework.context.annotation.AnnotatedBeanDefinitionReader#AnnotatedBeanDefinitionReader(org.springframework.beans.factory.support.BeanDefinitionRegistry, org.springframework.core.env.Environment)}
     * 	   // 同时向BeanFactory中注册了注解处理器
     * 	   // @see {@link org.springframework.context.annotation.AnnotationConfigUtils#registerAnnotationConfigProcessors(org.springframework.beans.factory.support.BeanDefinitionRegistry, java.lang.Object)}
     * 	   // 这些处理器分别为 ConfigurationClassPostProcessor、AutowiredAnnotationBeanPostProcessor、CommonAnnotationBeanPostProcessor、EventListenerMethodProcessor、DefaultEventListenerFactory
     * 	   context = createApplicationContext();
     *
     * 	   // 加载SpringBoot启动异常报表处类
     * 	   exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
     * 					new Class[] { ConfigurableApplicationContext.class }, context);
     *
     *     // 准备上下文即对创建的上下文做初始化配置
     *     // @see {@link org.springframework.boot.SpringApplication#prepareContext(org.springframework.context.ConfigurableApplicationContext, org.springframework.core.env.ConfigurableEnvironment, org.springframework.boot.SpringApplicationRunListeners, org.springframework.boot.ApplicationArguments, org.springframework.boot.Banner)}
     *     // 主要的逻辑是依次调用之前从META-INF/spring.factories加载的上下文初始化器
     *     // @see {@link org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)}
     *     // @see {@link org.springframework.boot.context.config.DelegatingApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)}
     * 	   // @see {@link org.springframework.boot.context.ContextIdApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)}
     * 	   // @see {@link org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener#initialize(org.springframework.context.ConfigurableApplicationContext)}
     * 	   // @see {@link org.springframework.boot.context.ConfigurationWarningsApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)}
     * 	   // @see {@link org.springframework.boot.rsocket.context.RSocketPortInfoApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)}
     * 	   // @see {@link org.springframework.boot.web.context.ServerPortInfoApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)}
     * 	   // 同时加载Main函数所在的类所指定的Bean定义信息资源 ->这里会将应用入口类注册到BeanFactory中
     * 	   // @see {@link org.springframework.boot.BeanDefinitionLoader#BeanDefinitionLoader(org.springframework.beans.factory.support.BeanDefinitionRegistry, java.lang.Object...)
     * 	   // @see {@link org.springframework.boot.BeanDefinitionLoader#load(java.lang.Object)
     * 	   prepareContext(context, environment, listeners, applicationArguments, printedBanner);
     *
     *     // 刷新上下文 即Sping容器应用上下文的初始化 - 即解析配置 -> 注册Bean定义 -> 初始化单例Bean到容器中
     *     // 在这个过程中invokeBeanFactoryPostProcessors方法的执行完成了配置解析和Bean定义注册
     *     // 因此SpringBoot的自动装配就发生在这里
     *     // @see {@link org.springframework.context.support.AbstractApplicationContext#invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)}
     * 	   refreshContext(context);
     *
     *     // do nothing
     * 	   afterRefresh(context, applicationArguments);
     *
     * </pre>
     *
     * 3. SpringBoot 自动装配过程
     *
     * 通过之前的分析得出自动装配由Spring容器上下文初始化过程中的
     * BeanFactoryPostProcessor(具体是ConfigurationClassPostProcessor)执行完成 其详细过程为
     * @see org.springframework.context.annotation.ConfigurationClassPostProcessor#processConfigBeanDefinitions(org.springframework.beans.factory.support.BeanDefinitionRegistry)
     * @see org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader#loadBeanDefinitionsForConfigurationClass(org.springframework.context.annotation.ConfigurationClass, org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.TrackedConditionEvaluator)
     * @see org.springframework.context.annotation.ConfigurationClassParser.DeferredImportSelectorGrouping#getImports()
     *
     * 解析SpringBoot应用的入口类(@SpringBootApplication标注的类)时 通过
     * 注解@EnableAutoConfiguration和@AutoConfigurationPackage分别导入
     * @see org.springframework.boot.autoconfigure.AutoConfigurationPackages.Registrar
     * 和
     * @see org.springframework.boot.autoconfigure.AutoConfigurationImportSelector
     *
     * AutoConfigurationPackages.Registrar向容器中注册了应用自动配置的basepacke 即@SpringBootApplication注解标注的类所在的包
     *
     * AutoConfigurationImportSelector向容器中注册自动配置类 即从META-INF/spring.factories中加载
     * key为org.springframework.boot.autoconfigure.EnableAutoConfiguration的配置指定的自动配置
     * 类(当前共124个) 例如: @see {@link org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration}
     *
     * @see org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.AutoConfigurationGroup#process(org.springframework.core.type.AnnotationMetadata, org.springframework.context.annotation.DeferredImportSelector)
     * @see org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.AutoConfigurationGroup#selectImports()
     *
     * 满足条件的自动配置类被注册到容器中 这些自动配置类都是一个Sping的Bean定义配置 在接下来的过程中会被解析
     * 其中的Bean定义注册到容器中
     *
     * 4. 一个自动配置类分析(以HttpEncodingAutoConfiguration为例)
     * <pre>
     *     // 标记为配置类
     *     @Configuration(proxyBeanMethods = false)
     *     // 开启HttpProperties类属性和配置中相应配置的绑定
     *     @EnableConfigurationProperties(HttpProperties.class)
     *     // 条件 当前应用为WEB应用生效
     *     @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
     *     // 条件 当前应用类路径下存在CharacterEncodingFilter生效
     *     @ConditionalOnClass(CharacterEncodingFilter.class)
     *     // 条件 当前应用配置了spring.http.encoding.enabled = true生效 (matchIfMissing = true)-默认为spring.http.encoding.enabled = true
     *     @ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)
     *     public class HttpEncodingAutoConfiguration {
     *         // 自动配置对应的配置属性数据类
     *         // @see {@link org.springframework.boot.autoconfigure.http.HttpProperties.Encoding}
     *         // @EnableConfigurationProperties(HttpProperties.class)开启属性绑定 自动配置类实例化时
     *         // 由容器注入
     *         // 自动装配的组件的属性设置 可以参考其配置类对应的Properties数据类的属性 例如:
     *         // <pre>
     *         //      @ConfigurationProperties(prefix = "spring.http")
     *         //      public class HttpProperties {
     *         //          ......
     *         //     }
     *         // </pre>
     *         // 即可以在配置文件中通过spring.http.encoding = utf-8 配置对应值
     * 	       private final HttpProperties.Encoding properties;
     *
     * 	       public HttpEncodingAutoConfiguration(HttpProperties properties) {
     * 		       this.properties = properties.getEncoding();
     *         }
     *
     *         @Bean // 定义Bean
     *         @ConditionalOnMissingBean // 条件 - 容器中无这个Bean定义生效
     *         public CharacterEncodingFilter characterEncodingFilter() {
     * 		       CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
     * 		       filter.setEncoding(this.properties.getCharset().name());
     * 		       filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
     * 		       filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
     * 		       return filter;
     *         }
     *         ......
     *     }
     * </pre>
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAutoConfigureProcessAnalysisApplication.class, args);
    }
}
