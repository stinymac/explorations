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

package org.mac.explorations.framework.spring.aop.tx;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 基于注解的声明式事务
 *
 * 1.向容器中注册事务管理器 PlatformTransactionManager
 * 2.@EnableTransactionManagement注解启用事务
 * 3.在需要事务的方法加@Transactional注解
 *
 * Spring 事务源码分析
 *
 * 使用@EnableTransactionManagement开启事务 其向容器中注册了AutoProxyRegistrar 和 ProxyTransactionManagementConfiguration
 * <pre>
 *     //@see {@link org.springframework.context.annotation.AutoProxyRegistrar#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)}
 *     //@see {@link org.springframework.transaction.annotation.TransactionManagementConfigurationSelector#selectImports(org.springframework.context.annotation.AdviceMode)}
 *     @Import(TransactionManagementConfigurationSelector.class)
 *     public @interface EnableTransactionManagement {
 *     }
 * </pre>
 *
 * @see org.springframework.context.annotation.AutoProxyRegistrar
 * <pre>
 *     registerOrEscalateApcAsRequired(InfrastructureAdvisorAutoProxyCreator.class, registry, source);
 * </pre>
 * 向容器中注册了InfrastructureAdvisorAutoProxyCreator 这是一个BeanPostProcessor 在Bean初始换后负责创建目标
 * 对象的代理类
 * @see org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator
 * 其生成道理对象的流程和AOP是类似的
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessAfterInitialization(Object, String)
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#wrapIfNecessary(Object, String, Object)
 *
 * @see org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration 这个配置类
 * 向容器中注册了BeanFactoryTransactionAttributeSourceAdvisor、TransactionAttributeSource、TransactionInterceptor
 *
 * @see org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor
 * @see org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
 * @see org.springframework.transaction.interceptor.TransactionInterceptor
 *
 * 代理创建器InfrastructureAdvisorAutoProxyCreator创建代理时会将BeanFactoryTransactionAttributeSourceAdvisor设置到代理工厂中
 *
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#createProxy(Class, String, Object[], org.springframework.aop.TargetSource)
 * @see org.springframework.aop.framework.CglibAopProxy#getProxy(ClassLoader)
 * @see org.springframework.aop.framework.ObjenesisCglibAopProxy#createProxyClassAndInstance(org.springframework.cglib.proxy.Enhancer, org.springframework.cglib.proxy.Callback[])
 *
 * 最终植入TransactionInterceptor拦截器
 *
 * 事务方法调用时拦截器介入(AOP的拦截器链调用 这里只有一个TransactionInterceptor拦截器)
 *
 * @see org.springframework.transaction.interceptor.TransactionAspectSupport#invokeWithinTransaction(java.lang.reflect.Method, Class, org.springframework.transaction.interceptor.TransactionAspectSupport.InvocationCallback)
 * <pre>
 *     Object retVal;
 *     try {
 *         // This is an around advice: Invoke the next interceptor in the chain.
 *         // This will normally result in a target object being invoked.
 *         retVal = invocation.proceedWithInvocation();
 *     }
 *     catch (Throwable ex) {
 *         // target invocation exception
 *         completeTransactionAfterThrowing(txInfo, ex);
 *         throw ex;
 *     }
 *     finally {
 *         cleanupTransactionInfo(txInfo);
 *     }
 *     ......
 *     commitTransactionAfterReturning(txInfo);
 *     return retVal;
 * </pre>
 *
 * @auther mac
 * @date 2020-01-14
 */
@Configuration
@PropertySource({"classpath:/jdbc.properties"})
@EnableTransactionManagement
public class SpringDeclarativeTransactionExample {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringDeclarativeTransactionExample.class);
        PlainTransactionRequiredService plainTransactionRequiredService = context.getBean(PlainTransactionRequiredService.class);
        plainTransactionRequiredService.addEntity();
    }

    @Bean
    public DataSource dataSource(@Value("${jdbc.driver}") String driverClassName,
                                 @Value("${jdbc.dev.url}") String jdbcUrl,
                                 @Value("${jdbc.username}") String username,
                                 @Value("${jdbc.password}") String password) {

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws Exception {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) throws Exception{
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public PlainEntityRepository plainEntityRepository() {
        return new PlainEntityRepository();
    }

    @Bean
    public PlainTransactionRequiredService plainTransactionRequiredService(){
        return new PlainTransactionRequiredService();
    }
}
