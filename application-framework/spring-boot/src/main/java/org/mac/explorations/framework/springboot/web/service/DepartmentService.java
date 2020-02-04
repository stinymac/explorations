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

package org.mac.explorations.framework.springboot.web.service;

import org.mac.explorations.framework.springboot.web.repository.entity.Department;
import org.mac.explorations.framework.springboot.web.repository.mybatis.annotation.DepartmentMapper;
import org.springframework.stereotype.Service;

/**
 *  Department service
 *
 * @auther mac
 * @date 2018-12-04
 */
@Service
public class DepartmentService {

    private DepartmentMapper departmentMapper;

    /**
     * 构造器注入的方式,能够保证注入的组件不可变,并且确保需要的依赖不为空。
     * 此外,构造器注入的依赖总是能够在返回客户端（组件）代码的时候保证完全初始化的状态。
     *
     * 组件不可变:
     * final 关键字
     *
     * 确保需要的依赖不为空:
     * 由于实现了有参数的构造函数,所以不会调用默认构造函数,那么就需要Spring容器传入所需要的参数,
     * 所以就两种情况:
     * 1.有该类型的参数->传入,OK 。
     * 2.无该类型的参数->报错,所以保证不会为空。
     *
     * 保证完全初始化的状态:
     * Java类加载实例化的过程中，构造方法是最后一步。
     * @param departmentMapper
     */
    //@Autowired注解在构造函数上是可选的
    public DepartmentService(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public Department save(Department department) {
        departmentMapper.insert(department);
        return department;
    }

    public Department get(Integer departmentId){
       return departmentMapper.getDepartmentBy(departmentId);
    }

    public Object getDepartments() {
        return departmentMapper.getAll();
    }
}
