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

package org.mac.explorations.framework.spring.coreprinciples.component.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @auther mac
 * @date: 2020-01-17 21:37
 */
@Repository
public class SimpleSampleRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SimpleSampleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save() {
        String sql = "INSERT INTO `employee`(`name`, age, gender, email,status) VALUES("+"\"johon\","+"18,"+"\"1\","+"\"johon@gmail.com\","+"\"0\")";
        return jdbcTemplate.update(sql);
    }
}
