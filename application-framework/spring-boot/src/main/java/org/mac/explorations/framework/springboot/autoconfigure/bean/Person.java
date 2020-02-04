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

package org.mac.explorations.framework.springboot.autoconfigure.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 配置属性绑定
 *
 * @ConfigurationProperties需要依赖
 * <pre>
 *    compile group: 'org.springframework.boot', name: 'spring-boot-autoconfigure-processor'
 *    // gradle4.6之后
 *    annotationProcessor 'org.springframework.boot:spring-boot-autoconfigure-processor'
 * </pre>
 * 属性绑定的类(Person 、Car、 Pet)需要注册到容器并提供getter和setter方法
 *
 * SpringBoot自动装配也用到了@ConfigurationProperties注解
 * @see org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties
 * ......
 *
 * @auther mac
 * @date 2020-01-31 15:57
 */
@Component
@ConfigurationProperties(prefix = "person")
@Data
public class Person {
    private String name;
    private int age;
    private boolean boss;
    private LocalDate birthday;
    private Car car;
    private Map<String,Object> chronicleOfEvents;
    private List<Pet> pets;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", boss=" + boss +
                ", birthday=" + birthday +
                ", car=" + car +
                ", chronicleOfEvents=" + chronicleOfEvents +
                ", pets=" + pets +
                '}';
    }
}
