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

import java.nio.charset.Charset;

/**
 * @auther mac
 * @date 2019-12-15
 */
public interface Constant {

    String DEFAULT_SERVER_HOST = "127.0.0.1";
    int DEFAULT_SERVER_PORT = 8888;

    /**
     * 客户端从服务端注销命令
     */
   String COMMAND_LOGOUT = "exit";

   int DEFAULT_BUFFER_SIZE = 1024;
   int DEFAULT_THREAD_POOL_SIZE = 10;

   Charset CHARSET = Charset.forName("UTF-8");
}
