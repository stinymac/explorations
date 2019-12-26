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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class JarFileOperation {
    public static void main(String[] args) throws IOException {

        Class<IOUtils> ioUtilsClass = IOUtils.class;
        URL jarFileURL = ioUtilsClass.getProtectionDomain().getCodeSource().getLocation();

        try (JarFile jarFile = new JarFile(jarFileURL.getPath())) {
            Manifest manifest = jarFile.getManifest();
            System.out.println(new HashMap(manifest.getMainAttributes()));
        }
        //Collections.list(zipFile.entries()).forEach(System.out::println);
    }
}
