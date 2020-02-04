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

package org.mac.explorations.framework.springboot.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot数据源自动配置
 *
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 * @see org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializationConfiguration
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
 *
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceConfiguration.Hikari
 * <pre>
 *  @Configuration(proxyBeanMethods = false)
 *  @ConditionalOnClass(HikariDataSource.class)
 *  @ConditionalOnMissingBean(DataSource.class)
 *  @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.zaxxer.hikari.HikariDataSource",matchIfMissing = true)
 * 	static class Hikari {
 *        @Bean
 *        @ConfigurationProperties(prefix = "spring.datasource.hikari")
 * 		  HikariDataSource dataSource(DataSourceProperties properties) {
 * 		      // @see {@link org.springframework.boot.jdbc.DataSourceBuilder}
 * 			  HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
 * 			  if (StringUtils.hasText(properties.getName())) {
 * 				  dataSource.setPoolName(properties.getName());
 *            }
 * 			  return dataSource;
 *        }
 *  }
 * </pre>
 * 即SpringBoot自动配置默认使用HikariDataSource
 *
 * <pre>
 *     @Configuration(proxyBeanMethods = false)
 *     @ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
 *     @EnableConfigurationProperties(DataSourceProperties.class)
 *     @Import({ DataSourcePoolMetadataProvidersConfiguration.class, DataSourceInitializationConfiguration.class })
 *     public class DataSourceAutoConfiguration {
 *         ......
 *     }
 * </pre>
 * 导入DataSourcePoolMetadataProvidersConfiguration和DataSourceInitializationConfiguration
 * <pre>
 *     @Import({ DataSourceInitializerInvoker.class, DataSourceInitializationConfiguration.Registrar.class })
 *     class DataSourceInitializationConfiguration {
 *         ......
 *     }
 * </pre>
 * 注册了
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerPostProcessor
 * 用于初始化
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker
 * <pre>
 *    class DataSourceInitializerInvoker implements ApplicationListener<DataSourceSchemaCreatedEvent>, InitializingBean {
 *         ......
 *    }
 * </pre>
 * DataSourceInitializerInvoker实现了ApplicationListener 可以在启动后执行SQL建表脚本和数据初始化脚本
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker#afterPropertiesSet()
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer#createSchema()
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker#initialize(org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer)
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer#initSchema()
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer#runScripts(java.util.List, java.lang.String, java.lang.String)
 * 默认的数据脚本位置和名称
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer#getScripts(java.lang.String, java.util.List, java.lang.String)
 *
 *
 * @auther mac
 * @date 2020-02-04 12:08
 */
@SpringBootApplication
public class SpringBootDataAccessApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataAccessApplication.class,args);
    }


}
