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

package org.mac.explorations.framework.mybatis;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.mac.explorations.framework.mybatis.others.MyBatisOthersExample;

import java.util.List;
import java.util.Map;

/**
 * @auther mac
 * @date 2020-01-08
 */
public interface EmployeeMapper {

	Employee getEmployeeById(Integer id);

	Long addEmployee(Employee employee);

	boolean updateEmployee(Employee employee);

	void deleteEmployeeById(Integer id);

	Employee getEmployeeByIdAndName(@Param("id") Integer id,@Param("name")  String name);

	List<Employee> getEmployeeByIds(List<Integer> ids);

	Map<String,Object> getEmployeeMapById(Integer id);

	@MapKey("id")//使用给定的属性列作为key
	Map<Integer,Employee> getEmployeesMapByName(String name);

	Employee getEmployeeByIdWithResultMap(Integer id);

	List<Employee> getEmployeeByCondition(Employee employee);

	List<Employee> getEmployeesByConditionWithTrim(Employee employee);

	List<Employee> getEmployeesByConditionWithChoose(Employee employee);

	boolean updateEmployeeDynamically(Employee employee);

	List<Employee> getEmployeeByIdsWithForeach(@Param("ids") List<Integer> ids);

	int batchInsertEmployees(List<Employee> employees);

	List<Employee> getEmployeeWithInnerParameter(Employee employee);

	List<Employee> getEmployeeByNameWithPage(@Param("name") String name,
											 @Param("pageNum") int pageNum,
											 @Param("pageSize") int pageSize);

	List<Employee> callProcedure(MyBatisOthersExample.ProducerCallParam param);
}
