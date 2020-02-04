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

package org.mac.explorations.framework.springboot.web.repository.mybatis.xml;


import org.apache.ibatis.annotations.Mapper;
import org.mac.explorations.framework.springboot.web.repository.entity.Employee;

import java.util.List;

/**
 * Employee mapper
 *
 * @auther mac
 * @date 2018-12-04
 */
@Mapper
public interface EmployeeMapper {

    Employee getEmployeeById(Integer id);

    void insert(Employee employee);

    List<Employee> getAll();

    int update(Employee employee);

    int deleteEmployeeById(Integer id);
}
