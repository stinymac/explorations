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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

/**
 * @auther mac
 * @date 2020-01-14
 */
public class PlainEntityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert() {
        String sql = "INSERT INTO `employee`(`name`, age, gender, email,status) VALUES(?,?,?,?,?)";
        String name = UUID.randomUUID().toString().substring(0, 5);
        int age = 18;
        String gender = "1";
        String email = name+"@163.com";
        String status = "0";
        int rows = jdbcTemplate.update(sql, name,age,gender,email,status);
        //int i = 10/0;
        return rows;
    }
}
