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

package org.mac.explorations.framework.springboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mac.explorations.framework.springboot.autoconfigure.SpringBootAutoConfigureProcessAnalysisApplication;
import org.mac.explorations.framework.springboot.autoconfigure.bean.Person;
import org.mac.explorations.framework.springboot.autoconfigure.service.SimpleEchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @auther mac
 * @date 2020-01-31 16:23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootAutoConfigureProcessAnalysisApplication.class)
public class SpringBootAutoConfigureProcessAnalysisApplicationTest {
    @Autowired
    private Person person;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testConfigurationProperties(){
        System.err.println(person);
    }

    @Test
    public void testConfigurationAnnotation(){
        System.err.println(context.getBean(SimpleEchoService.class).echo("Test Configuration Annotation"));
    }
}
