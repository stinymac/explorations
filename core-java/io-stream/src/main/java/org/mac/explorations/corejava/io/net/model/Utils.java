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

package org.mac.explorations.corejava.io.net.model;

import java.io.Closeable;

/**
 * @auther mac
 * @date 2019-12-14
 */
public final class Utils {

    private Utils(){throw new UnsupportedOperationException("Can not create instance.");}

    public static <T extends Closeable> void release(T... resources) {
        if (resources == null || resources.length <= 0) {
            return;
        }
        T closeableResource = null;
        try {
            for (T resource : resources) {
                if ((closeableResource = resource) != null) {
                    resource.close();
                }
            }
        } catch (Exception e) {
            logSimply("Close resource ["+closeableResource+"] fail:",e);
        }
    }

    public static void logSimply(String msg,Throwable... e) {
        System.out.println(msg);
        if (e != null && e.length > 0) {
            e[0].printStackTrace();
        }
    }
}
