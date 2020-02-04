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
import org.mac.explorations.framework.springboot.data.SpringBootDataAccessApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @auther mac
 * @date 2020-02-04 13:42
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootDataAccessApplication.class)
public class SpringBootDataAccessApplicationTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(SpringBootDataAccessApplicationTest.class);

    @Test
    public void testAutoConfiguredDataSource(@Autowired DataSource dataSource){
       LOGGER.info("Data source spring-boot auto configure : " + dataSource);
    }

    @Test
    public void testAutoConfiguredJdbcTemplate(@Autowired JdbcTemplate jdbcTemplate){
        List<Map<String, Object>> departments = jdbcTemplate.queryForList("select * from department");
        LOGGER.info(departments.toString());
    }
}
