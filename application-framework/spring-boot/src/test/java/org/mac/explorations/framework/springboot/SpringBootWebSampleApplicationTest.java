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
import org.mac.explorations.framework.springboot.web.SpringBootWebSampleApplication;
import org.mac.explorations.framework.springboot.web.repository.mybatis.annotation.DepartmentMapper;
import org.mac.explorations.framework.springboot.web.repository.mybatis.xml.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @auther mac
 * @date 2020-02-04 19:45
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootWebSampleApplication.class)
public class SpringBootWebSampleApplicationTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(SpringBootWebSampleApplicationTest.class);

    @Test
    public void testSpringBootMyBatis(@Autowired DepartmentMapper departmentMapper, @Autowired EmployeeMapper employeeMapper){
        LOGGER.info(departmentMapper.getAll().toString());
        LOGGER.info(employeeMapper.getEmployeeById(1).toString());
    }
}
