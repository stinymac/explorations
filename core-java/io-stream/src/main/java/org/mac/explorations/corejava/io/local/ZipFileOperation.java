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
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class ZipFileOperation {

    public static void main(String[] args) {
        Class<IOUtils> ioUtilsClass = IOUtils.class;
        URL jarFileURL = ioUtilsClass.getProtectionDomain().getCodeSource().getLocation();
        System.out.println("jarFileURL:"+jarFileURL);
        try (ZipFile zipFile = new ZipFile(jarFileURL.getPath(), Charset.forName("UTF-8"))) {
            ZipEntry manifestEntry = zipFile.getEntry("META-INF/MANIFEST.MF");
            try (InputStream inputStream = zipFile.getInputStream(manifestEntry)) {
                System.out.println(IOUtils.toString(inputStream, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
