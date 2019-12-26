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
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class PathAPISample {

    private static final String USER_DIR_LOCATION = System.getProperty("user.dir");

    public static void main(String[] args) {
        Path path = Paths.get(USER_DIR_LOCATION);
        System.out.println(path.toAbsolutePath());
        System.out.println(path.getFileName()+","+path.getName(0));
        int nameCount = path.getNameCount();
        for (int i = 0; i < nameCount; i++) {
            System.out.println(path.getName(i));
        }
        for(Path p : path) {
            System.out.println(p);
        }
        System.out.println(path.getRoot());

        FileSystems.getDefault().getRootDirectories().forEach(System.out::println);

        Path p = Paths.get("E:\\IDEAWorkspace\\stinymac\\..");
        System.out.println(p.normalize());

        Path pathFromLocation = Paths.get(USER_DIR_LOCATION);
        File file = new File(USER_DIR_LOCATION);
        Path pathFromFile = file.toPath();
        System.out.println("pathFromFile.toUri:"+pathFromFile.toUri());
        Path pathFromURI = Paths.get(pathFromFile.toUri());
        System.out.println("pathFromURL : " + pathFromURI);
        System.out.println("pathFromLocation : " + pathFromLocation);
        System.out.println("pathFromFile : " + pathFromFile);
        System.out.println("pathFromURL == pathFromLocation ? " + pathFromURI.equals(pathFromLocation));
        System.out.println("pathFromFile == pathFromLocation ? " + pathFromFile.equals(pathFromLocation));
    }
}
