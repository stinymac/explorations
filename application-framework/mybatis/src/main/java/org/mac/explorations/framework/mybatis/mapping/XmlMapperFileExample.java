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

package org.mac.explorations.framework.mybatis.mapping;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.SqlSession;
import org.mac.explorations.framework.mybatis.BaseExample;
import org.mac.explorations.framework.mybatis.Department;
import org.mac.explorations.framework.mybatis.DepartmentMapper;
import org.mac.explorations.framework.mybatis.Employee;
import org.mac.explorations.framework.mybatis.EmployeeDepartmentMapper;
import org.mac.explorations.framework.mybatis.EmployeeMapper;

import java.util.Arrays;

/**
 * MyBatis对SQL需要的参数处理逻辑
 *
 * @see org.apache.ibatis.binding.MapperProxy#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
 * 调用Mapper接口代理对象的方法调用处理器
 *
 * 如果是Object的方法直接返回
 * 如果是接口的default方法
 * invokeDefaultMethodJava8/invokeDefaultMethodJava9
 *
 * 是Mapper接口的方法则对方法封装为MapperMethod对象进行缓存
 * @see org.apache.ibatis.binding.MapperProxy#cachedMapperMethod(java.lang.reflect.Method)
 *
 * methodCache.computeIfAbsent(method,
 *                     k -> new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
 * @see org.apache.ibatis.binding.MapperMethod#MapperMethod(java.lang.Class, java.lang.reflect.Method, org.apache.ibatis.session.Configuration)
 *
 * 即解析调用的方法对应的SqlCommand和MethodSignature
 *     @see org.apache.ibatis.binding.MapperMethod.SqlCommand#SqlCommand(org.apache.ibatis.session.Configuration, java.lang.Class, java.lang.reflect.Method)
 *     @see org.apache.ibatis.binding.MapperMethod.SqlCommand#resolveMappedStatement(java.lang.Class, java.lang.String, java.lang.Class, org.apache.ibatis.session.Configuration)
 *
 * 缓存方法后执行调用的方法
 * @see org.apache.ibatis.binding.MapperMethod#execute(org.apache.ibatis.session.SqlSession, java.lang.Object[])
 *
 * 根据方法(MapperMethod)对应的类型@see {@link SqlCommandType}分别处理
 * 处理前都先调用convertArgsToSqlCommandParam将参数转化为SqlCommandParam
 * @see org.apache.ibatis.binding.MapperMethod.MethodSignature#convertArgsToSqlCommandParam(java.lang.Object[])
 * 即调用@see {@link ParamNameResolver}的getNamedParams方法解析参数
 *
 * @see org.apache.ibatis.reflection.ParamNameResolver#ParamNameResolver(org.apache.ibatis.session.Configuration, java.lang.reflect.Method)
 * 参数解析器在构建时会对参数名进行解析
 *
 *     参数名上无@param注解
 *     if (config.isUseActualParamName()) {
 *         //方法参数实际定义的名称
 *         name = getActualParamName(method, paramIndex);
 *     }
 *     if (name == null) {
 *         // use the parameter index as the name ("0", "1", ...)
 *         // gcode issue #71
 *         name = String.valueOf(map.size());
 *     }
 *     参数名上有@param注解使用@param给定的name
 *
 *     解析等到方法参数名后按顺序放入names的sortmap容器中
 *     map.put(paramIndex, name);
 *
 * 方法调用时获取命名参数
 * @see org.apache.ibatis.reflection.ParamNameResolver#getNamedParams(java.lang.Object[])
 *
 * 只有一个参数且没有@param注解直接返回参数
 * return args[names.firstKey()];
 *
 * 其他情况则放入Map中
 *
 * final Map<String, Object> param = new ParamMap<>();
 * int i = 0;
 * for (Map.Entry<Integer, String> entry : names.entrySet()) {
 *     param.put(entry.getValue(), args[entry.getKey()]);
 *     // add generic param names (param1, param2, ...)
 *     final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
 *     // ensure not to overwrite parameter named with @Param
 *     if (!names.containsValue(genericParamName)) {
 *         param.put(genericParamName, args[entry.getKey()]);
 *     }
 *     i++;
 * }
 * return param;
 *
 * 对于参数是集合类型的在执行方法时会做进一步处理
 * @see org.apache.ibatis.session.defaults.DefaultSqlSession#wrapCollection(java.lang.Object)
 *
 * if (object instanceof Collection) {
 *     StrictMap<Object> map = new StrictMap<>();
 *     map.put("collection", object);
 *     if (object instanceof List) {
 *         map.put("list", object);
 *     }
 *     return map;
 * } else if (object != null && object.getClass().isArray()) {
 *     StrictMap<Object> map = new StrictMap<>();
 *     map.put("array", object);
 *     return map;
 * }
 * return object;
 *
 * @auther mac
 * @date 2020-01-08
 */
public class XmlMapperFileExample extends BaseExample {

    @Override
    protected void doExample(SqlSession openSession) {
        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

        Employee employee = new Employee("Tom", 18, "0", "767189271@qq.com");
        //mapper.addEmployee(employee);
        // 注意提交 才能生效
        //openSession.commit();
        //System.out.println(employee);

        //Employee upt = new Employee(8,"Tom", 28, "0", "767189271@qq.com");
        //mapper.updateEmployee(upt);
        //openSession.commit();

        //System.out.println(mapper.getEmployeeById(8));

        // 多个参数
        System.out.println(mapper.getEmployeeByIdAndName(8,"Tom"));
        // 一个List类型参数
        System.out.println(mapper.getEmployeeByIds(Arrays.asList(1,8)));

        System.out.println(mapper.getEmployeeMapById(8));
        System.out.println(mapper.getEmployeesMapByName("%y%"));
        System.out.println(mapper.getEmployeeByIdWithResultMap(10));

        EmployeeDepartmentMapper employeeDepartmentMapper = openSession.getMapper(EmployeeDepartmentMapper.class);
        System.out.println(employeeDepartmentMapper.getEmployeeById(9));

        System.out.println(employeeDepartmentMapper.getEmployeeByIdWithStep(9));

        System.out.println(employeeDepartmentMapper.getEmployeeByIdWithDiscriminator(10));

        DepartmentMapper departmentMapper = openSession.getMapper(DepartmentMapper.class);
        System.out.println(departmentMapper.getDepartmentAndItsEmpById(2));

        Department department = departmentMapper.getDepartmentAndItsEmpByIdWithStep(2);
        System.out.println(department.getEmployees());
    }

    public static void main(String[] args) {
        new XmlMapperFileExample().execute();
    }
}
