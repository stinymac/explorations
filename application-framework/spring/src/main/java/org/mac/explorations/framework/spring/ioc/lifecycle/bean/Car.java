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

package org.mac.explorations.framework.spring.ioc.lifecycle.bean;

/**
 * @auther mac
 * @date 2020-01-13
 */
public class Car {

    public Car() {

        System.err.println("=====Car construction=====");
    }

    public void init() {
        System.err.println("=====Car init=====");
    }

    public void destroy() {
        System.err.println("=====Car destroy=====");
    }
}
