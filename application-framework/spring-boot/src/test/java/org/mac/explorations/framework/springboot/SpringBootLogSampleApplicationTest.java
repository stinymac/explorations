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
import org.mac.explorations.framework.springboot.log.SpringBootLogSampleApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @auther mac
 * @date 2020-02-01 21:32
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootLogSampleApplication.class)
public class SpringBootLogSampleApplicationTest {

    @Test
    public void testSpringBootLog(){
        Logger logger = LoggerFactory.getLogger(SpringBootLogSampleApplicationTest.class);
        logger.trace("--->log level trace...");
        logger.debug("--->log level debug...");
        //SpringBoot默认使用的是info级别的，没有指定级别的就用SpringBoot默认规定的级别；root级别
        logger.info("--->log level info...");
        logger.warn("--->log level warn...");
        logger.error("--->log level error...");
    }
}
