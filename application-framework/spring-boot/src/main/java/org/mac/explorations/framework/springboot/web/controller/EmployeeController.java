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

package org.mac.explorations.framework.springboot.web.controller;

import org.mac.explorations.framework.springboot.web.repository.entity.Employee;
import org.mac.explorations.framework.springboot.web.service.DepartmentService;
import org.mac.explorations.framework.springboot.web.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * tip:
 *
 * 保持@Controller的简洁和专注
 *
 * 	Controller应该非常简单。
 * 	希望控制器作为协调和委派的角色，而不是执行实际的业务逻辑。
 *
 * 以下是主要做法：
 *
 * 	控制器应该是无状态的！默认情况下，控制器是单例，并且任何状态都可能导致大量问题。
 *
 * 	控制器不应该执行业务逻辑，而是依赖委托。
 *
 * 	控制器应该处理应用程序的HTTP层，这不应该传递给服务。
 *
 * 	控制器应该围绕用例/业务能力来设计。
 *
 *
 * 员工管理
 *
 * @auther mac
 * @date 2018-11-22
 */
@Controller
public class EmployeeController {

    /*
    private static Map<Integer, Employee> employees = new HashMap<>();
    static {
        employees.put(1001, new Employee(1001, "Jerry", "Jerry@163.com", 1, new Department(101, "D-AA")));
        employees.put(1002, new Employee(1002, "Tom", "Tom@163.com", 1, new Department(102, "D-BB")));
        employees.put(1003, new Employee(1003, "Alex", "Alex@163.com", 0, new Department(103, "D-CC")));
        employees.put(1004, new Employee(1004, "Jack", "Jack@163.com", 0, new Department(104, "D-DD")));
        employees.put(1005, new Employee(1005, "Spike", "Spike@163.com", 1, new Department(105, "D-EE")));
    }
    private static AtomicInteger autoIncrementId = new AtomicInteger(1006);

    private static Map<Integer, Department> departments = new HashMap<>();
    static{
        departments.put(101, new Department(101, "D-AA"));
        departments.put(102, new Department(102, "D-BB"));
        departments.put(103, new Department(103, "D-CC"));
        departments.put(104, new Department(104, "D-DD"));
        departments.put(105, new Department(105, "D-EE"));
    }
    */

    /*private DepartmentService departmentService;
    private EmployeeService employeeService;

    public EmployeeController(DepartmentService departmentService,EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }
    */
    private EmployeeService employeeService;
    private DepartmentService departmentService;

    public EmployeeController(EmployeeService employeeService,DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/employees")
    public String list(Model model) {
        model.addAttribute("employees",employeeService.getEmployees());
        return "employee/list";
    }

    @GetMapping("/employee")
    public String toAdd(Model model) {
        model.addAttribute("departments",departmentService.getDepartments());
        return "employee/edit";
    }

    @PostMapping("/employee")
    public String add(Employee employee) {

        employeeService.save(employee);

        return "redirect:/employees";
    }

    @GetMapping("/employee/{id}")
    public String toEdit(@PathVariable("id") Integer id,Model model) {

        Employee employee = employeeService.get(id);
        model.addAttribute("employee",employee);

        model.addAttribute("departments",departmentService.getDepartments());

        return "employee/edit";
    }


    @PutMapping("/employee")
    public String edit(Employee employee) {


        employeeService.update(employee);

        return "redirect:/employees";
    }

    @DeleteMapping ("/employee/{id}")
    public String remove(@PathVariable("id") Integer id) {

        employeeService.remove(id);

        return "redirect:/employees";
    }
}
