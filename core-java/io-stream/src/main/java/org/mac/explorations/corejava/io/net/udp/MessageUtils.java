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

package org.mac.explorations.corejava.io.net.udp;

/**
 * @auther mac
 * @date 2019-12-21
 */
public final class MessageUtils {

    private MessageUtils(){}

    private static final String SN_PREFIX = "SN:";
    private static final String PORT_PREFIX = "PORT:";

    public static String ofSN(String sn){
        return SN_PREFIX+sn;
    }

    public static String ofPort(int port){
        return PORT_PREFIX+port;
    }

    public static String getSN(String message) {
        if (message == null){
            return "";
        }
        if (message.startsWith(SN_PREFIX)) {
            return message.substring(SN_PREFIX.length());
        }
        return "";
    }

    public static int getPort(String message) {
        if (message == null){
            return -1;
        }
        if (message.startsWith(PORT_PREFIX)) {
            String port = message.substring(PORT_PREFIX.length());
            if (port == null) {
                return -1;
            }
            return Integer.valueOf(port);
        }
        return -1;
    }
}
