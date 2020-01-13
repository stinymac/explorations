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

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @auther mac
 * @date 2020-01-13
 */
public class Cat implements InitializingBean,DisposableBean {

    public Cat() {
        System.err.println("=====Cat construction=====");
    }

    @Override
    public void destroy() throws Exception {
        System.err.println("DisposableBean.destroy()=====Cat destroy=====");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.err.println("InitializingBean. afterPropertiesSet()=====Cat init=====");
    }
}
