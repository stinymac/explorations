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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.UserPrincipal;
import java.util.stream.Stream;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class FileOperation {
    private static final String USER_DIR_LOCATION = System.getProperty("user.dir");

    public static void displayFileExists() {
        Path path = Paths.get(USER_DIR_LOCATION);
        System.out.println(path + " is exists:"+Files.exists(path));
    }

    public static void displayFileAccessibility() {
        Path path = Paths.get(USER_DIR_LOCATION);
        System.out.println(path + "\n is readable:"+Files.isReadable(path) +
                "\n is writable:"+Files.isWritable(path)+
                "\n is executable:"+Files.isExecutable(path)+
                "\n is regular file:"+Files.isRegularFile(path));
    }

    public static void fileWR() {
        Path srcPath = Paths.get(USER_DIR_LOCATION,"build.gradle");
        Path dstPath = Paths.get(USER_DIR_LOCATION,"build-copy.txt");
        SeekableByteChannel srcChannel = null;
        SeekableByteChannel dstChannel = null;
        try {
            srcChannel = Files.newByteChannel(srcPath);
            dstChannel = Files.newByteChannel(dstPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            System.out.println("[------------------------------------------");
            while (srcChannel.read(buffer) > 0) {
                buffer.flip();
                System.out.println(Charset.forName("UTF-8").decode(buffer));
                buffer.rewind();
                while (buffer.hasRemaining()) {
                    dstChannel.write(buffer);
                }
                buffer.clear();
            }
            System.out.println("------------------------------------------]");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (srcChannel != null) {
                try {
                    srcChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dstChannel != null) {
                try {
                    dstChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void displayFileEquals() throws IOException {
        Path path = Paths.get(USER_DIR_LOCATION);
        Path path2 = Paths.get(USER_DIR_LOCATION);
        System.out.println(Files.isSameFile(path, path2));
    }

    public static void createDirectories() {
        String classPath = System.getProperty("java.class.path");
        Stream.of(classPath.split(File.pathSeparator))
                .map(Paths::get) // String -> Path
                .filter(Files::isDirectory) // 过滤目录
                .filter(Files::isReadable)
                .filter(Files::isWritable)
                .map(Path::toString)        // Path -> String
                .map(dirPath -> Paths.get(dirPath, "parent-dir", "sub-dir"))  // Path -> new Path
                .forEach(newDir -> {
                    try {
                        Path newDirectory = Files.createDirectories(newDir);
                        System.out.printf("新的目录[%s] 已被创建!\n", newDirectory);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void displayMetadata() throws IOException {
        Path path = Paths.get(USER_DIR_LOCATION);
        UserPrincipal userPrincipal = Files.getOwner(path);
        System.out.printf("Path[%s]'s owner is : %s\n", path, userPrincipal);
    }

    public static void main(String[] args) throws IOException {
        displayMetadata();
        displayFileExists();
        displayFileAccessibility();
        displayFileEquals();
        fileWR();
        createDirectories();
    }
}
