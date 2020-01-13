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

import org.springframework.beans.factory.FactoryBean;

/**
 * @auther mac
 * @date 2020-01-12
 */
public class GraphicsFactoryBean implements FactoryBean<Graphics> {

    @Override
    public Graphics getObject() throws Exception {
        return new Graphics();
    }

    @Override
    public Class<?> getObjectType() {
        return Graphics.class;
    }
}
