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

package org.mac.explorations.framework.springboot.web.repository.entity;

/**
 * Department
 *
 * @auther mac
 * @date 2018-11-22
 */

public class Department {

    private Integer id;
    private String name;

    public Department() {
    }

    public Department(int i, String string) {
        this.id = i;
        this.name = string;
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

    public void setName(String departmentName) {
        this.name = departmentName;
    }

    @Override
    public String toString() {
        return "Department [id=" + id + ", departmentName=" + name + "]";
    }
}
