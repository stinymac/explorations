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

import org.mac.explorations.framework.mybatis.others.EmployeeStatus;

import java.io.Serializable;

/**
 * @auther mac
 * @date 2020-01-08
 */
public class Employee implements Serializable{

    private Integer id;
    private String name;
    private int age;
    private String gender;
    private String email;

    EmployeeStatus status = EmployeeStatus.ON;

    private Department department;

    public Employee() {
    }

    public Employee(String name, int age, String gender, String email) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
    }

    public Employee(String name, int age, String gender, String email, EmployeeStatus status, Department department) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.status = status;
        this.department = department;
    }

    public Employee(Integer id, String name, int age, String gender, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", department=" + department +
                '}';
    }
}
