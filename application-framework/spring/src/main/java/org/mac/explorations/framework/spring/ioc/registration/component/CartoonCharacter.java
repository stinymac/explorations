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

package org.mac.explorations.framework.spring.ioc.registration.component;

import java.time.LocalDate;

/**
 * @auther mac
 * @date 2020-01-12
 */
public class CartoonCharacter {

    private String name;
    private LocalDate birthday;
    private String nationality;

    public CartoonCharacter(String name, LocalDate birthday, String nationality) {
        this.name = name;
        this.birthday = birthday;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return "CartoonCharacter{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
