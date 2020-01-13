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

package org.mac.explorations.framework.mybatis.dynamicsql;

import org.apache.ibatis.session.SqlSession;
import org.mac.explorations.framework.mybatis.BaseExample;
import org.mac.explorations.framework.mybatis.Employee;
import org.mac.explorations.framework.mybatis.EmployeeMapper;

/**
 * @auther mac
 * @date 2020-01-09
 */
public class DynamicSQLExample extends BaseExample {

    @Override
    protected void doExample(SqlSession openSession) {
        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

            /*Employee cdt = new Employee();
            cdt.setName("%y%");
            System.out.println(mapper.getEmployeeByCondition(cdt));

            System.out.println(mapper.getEmployeesByConditionWithTrim(cdt));

            cdt.setName(null);
            System.out.println(mapper.getEmployeesByConditionWithChoose(cdt));

            Employee employee = new Employee(1,"Jerry", 8, "1", "Jerry@gmail.com");
            employee.setGender(null);
            mapper.updateEmployeeDynamically(employee);
            openSession.commit();

            System.out.println(mapper.getEmployeeById(1));

            System.out.println(mapper.getEmployeeByIdsWithForeach(Arrays.asList(1,8)));
            */

            /*List<Employee> employees = new ArrayList<>();
            Department department = new Department();
            department.setId(1);
            //department.setName("DEV");
            for (int i = 0; i < 1000; i++){
                Employee emp = new Employee("name"+i, 18, "1", "name"+i+"@163.com");
                emp.setDepartment(department);
                employees.add(emp);
            }
            mapper.batchInsertEmployees(employees);
            openSession.commit();*/

        Employee param = new Employee();
        param.setName("a");
        System.out.println(mapper.getEmployeeWithInnerParameter(param));
    }

    public static void main(String[] args) {
        new DynamicSQLExample().execute();
    }
}
