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

package org.mac.explorations.corejava.io.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @auther mac
 * @date 2019-12-19
 */
public class ObjectStreamSample {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>(Arrays.asList("A","B","C"));
        File file = new File("ser.data");

        // 对象序列化
        /**@see {@link ArrayList#writeObject(java.io.ObjectOutputStream)}*/
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(list);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对象反序列化
        /**@see {@link ArrayList#readObject(java.io.ObjectInputStream)}*/
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            List<String> copyList = (List) inputStream.readObject();
            System.out.println(copyList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
