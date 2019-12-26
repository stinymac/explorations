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

import java.io.FileDescriptor;
import java.lang.reflect.Field;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class FileDescriptorSample {

    public static void main(String[] args) throws Exception {
        displayFileDescriptor(FileDescriptor.in);
        displayFileDescriptor(FileDescriptor.out);
        displayFileDescriptor(FileDescriptor.err);
    }

    private static void displayFileDescriptor(FileDescriptor fileDescriptor) throws Exception {
        Integer fd = getFieldValue(fileDescriptor, "fd");
        Long handle = getFieldValue(fileDescriptor, "handle");
        Boolean closed = getFieldValue(fileDescriptor, "closed");
        System.out.printf("FileDescriptor[ fd : %d , handle %d , closed : %s ]\n", fd, handle, closed);
    }

    private static <T> T getFieldValue(FileDescriptor fileDescriptor, String fieldName) throws Exception {
        Field field = FileDescriptor.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(fileDescriptor);
    }
}
