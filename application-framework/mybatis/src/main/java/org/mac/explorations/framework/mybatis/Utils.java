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

import java.io.Closeable;

/**
 * @auther mac
 * @date 2020-01-08
 */
public final class Utils {

    private Utils(){throw new UnsupportedOperationException("Can not create instance.");}

    public static <T extends Closeable> void release(T... resources) {
        if (resources == null || resources.length <= 0) {
            return;
        }
        try {
            for (T resource : resources) {
                if (resource != null) {
                    resource.close();
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
