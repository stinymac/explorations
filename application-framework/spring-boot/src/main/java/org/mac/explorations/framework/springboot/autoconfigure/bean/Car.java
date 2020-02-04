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

package org.mac.explorations.framework.springboot.autoconfigure.bean;

import lombok.Data;

/**
 * @auther mac
 * @date 2020-01-31 16:05
 */
@Data
public class Car {
    private String brand;
    private String age;

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
