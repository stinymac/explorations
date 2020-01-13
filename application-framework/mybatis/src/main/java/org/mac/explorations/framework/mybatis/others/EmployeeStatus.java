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

package org.mac.explorations.framework.mybatis.others;

/**
 * @auther mac
 * @date 2020-01-12
 */
public enum EmployeeStatus {

    LEAVE("0","离职"),
    ON("1","在职");

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    EmployeeStatus(String status, String name) {
        this.code = status;
        this.name = name;
    }

    public static EmployeeStatus getEmployeeStatusByCode(String code) {
        for (EmployeeStatus status : EmployeeStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        //throw new IllegalArgumentException(code +" is not a valid code ");
        return null;
    }
}
