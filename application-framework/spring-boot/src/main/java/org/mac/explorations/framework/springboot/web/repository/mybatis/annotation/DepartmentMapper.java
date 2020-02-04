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

package org.mac.explorations.framework.springboot.web.repository.mybatis.annotation;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mac.explorations.framework.springboot.web.repository.entity.Department;

import java.util.List;

/**
 * 部门
 *
 * @auther mac
 * @date 2018-12-04
 */
@Mapper
public interface DepartmentMapper {
    @Select("select * from department where id=#{id}")
    Department getDepartmentBy(Integer id);

    @Delete("delete from department where id=#{id}")
    int deleteDepartmentBy(Integer id);

    @Options(useGeneratedKeys = true/*,keyProperty = "id"*/)//String keyProperty() default "id";
    @Insert("insert into department(name) values(#{name})")
    int insert(Department department);

    @Update("update department set name=#{name} where id=#{id}")
    int update(Department department);

    @Select("select * from department")
    List<Department> getAll();
}
