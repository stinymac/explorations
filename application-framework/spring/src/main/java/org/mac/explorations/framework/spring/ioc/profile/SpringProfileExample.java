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

package org.mac.explorations.framework.spring.ioc.profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.stream.Stream;

/**
 * @Profile：指定组件在哪个环境的情况下才能被注册到容器中，不指定，任何环境下都能注册这个组件
 *
 * 1）、加了@Profile环境标识的Bean，只有这个环境被激活的时候才能注册到容器中。默认是default环境
 * 2）、@Profile在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效
 * 3）、没有标注@Profile环境标识的Bean在，任何环境下都是加载的；
 *
 * profile环境切换
 *
 * 1、使用命令行动态参数: 在虚拟机参数位置加载 -Dspring.profiles.active=dev
 * 2、代码的方式激活某种环境；
 *
 * @auther mac
 * @date 2020-01-13
 */
@Configuration
@PropertySource({"classpath:/jdbc.properties"})
public class SpringProfileExample {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //指定激活环境
        context.getEnvironment().setActiveProfiles("dev"/*,"prod"*/);
        //注册主配置类
        context.register(SpringProfileExample.class);
        //启动刷新容器
        context.refresh();

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

    @Profile("dev")
    @Bean
    public HikariDataSource devDataSource(@Value("${jdbc.driver}") String driverClassName,
                                          @Value("${jdbc.dev.url}") String jdbcUrl,
                                          @Value("${jdbc.username}") String username,
                                          @Value("${jdbc.password}") String password){

        return new HikariDataSource(createHikariConfig(driverClassName,jdbcUrl,username,password));
    }

    @Profile("prod")
    @Bean
    public HikariDataSource prodDataSource(@Value("${jdbc.driver}") String driverClassName,
                                           @Value("${jdbc.prod.url}") String jdbcUrl,
                                           @Value("${jdbc.username}") String username,
                                           @Value("${jdbc.password}") String password){

        return new HikariDataSource(createHikariConfig(driverClassName,jdbcUrl,username,password));
    }

    private HikariConfig createHikariConfig(String driverClassName,String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return config;
    }
}
