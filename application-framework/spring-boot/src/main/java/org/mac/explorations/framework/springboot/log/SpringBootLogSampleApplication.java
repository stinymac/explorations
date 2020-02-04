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

package org.mac.explorations.framework.springboot.log;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 常见日志框架
 *
 * JUL、JCL、JBoss-logging、logback、log4j、log4j2、slf4j
 *
 * 日志门面 （日志的抽象层）
 * JCL（Jakarta Commons Logging）
 * SLF4j（Simple Logging Facade for Java）
 * jBoss-logging
 *
 * 日志实现
 * Log4j
 * JUL（java.util.logging）
 * Log4j2 Logback
 *
 * SpringBoot选用的是SLF4j和logback
 *
 * SLF4J API 适配各个日志框架
 * @see /explorations/application-framework/spring-boot/src/main/resources/static/images/concrete-bindings.png
 *
 * 统一应用中不同框架的日志框架
 * @see /explorations/application-framework/spring-boot/src/main/resources/static/images/legacy.png
 *
 * 1、将系统中其他框架使用的日志框架排除出去；
 * 2、用中间包来替换原有的日志框架；
 * 3、导入slf4j其他的实现
 *
 * SpringBoot日志
 * @see /explorations/application-framework/spring-boot/src/main/resources/static/images/spring-boot-logging.png
 *
 * SpringBoot使用slf4j+logback的方式进行日志记录
 * SpringBoot把其他的日志都替换成了slf4j
 * 如果要引入其他框架,一定要把这个框架的默认日志依赖移除掉
 *
 * 为SpringBoot自定义日志配置
 *
 * 给类路径下放上日志框架自己的配置文件即可；SpringBoot将不使用其默认配置
 * Logging System             Customization
 * Logback                    logback-spring.xml , logback-spring.groovy ,
 *                            logback.xml or logback.groovy
 *
 * Log4j2                     log4j2-spring.xml or log4j2.xml
 *
 * JDK (Java Util Logging)    logging.properties
 *
 * logback.xml：直接就被日志框架识别了；
 * logback-spring.xml：日志框架就不直接加载日志的配置项，
 *                     由SpringBoot解析日志配置，
 *                     可以使用SpringBoot 的高级Profile功能
 * @see /explorations/application-framework/spring-boot/src/main/resources/logback-spring.xml
 *
 * 切换日志框架(切换为log4j2)
 * 排除spring‐boot‐starter‐logging
 * 引入spring‐boot‐starter‐log4j2
 *
 * @auther mac
 * @date 2020-02-01 20:25
 */
//@SpringBootApplication
public class SpringBootLogSampleApplication {
    public static void main(String[] args) {

    }
}
