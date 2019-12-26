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
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @auther mac
 * @date 2019-12-20
 */
public class FileAPISample {

    private class WinCMDDIRCommand {

        private final File directory;

        public WinCMDDIRCommand(File directory) {
            this.directory = directory;
        }

        public void exec() {
            Stream.of(directory.listFiles())
                    .map(file -> {
                        StringBuilder displayMessageBuilder = new StringBuilder();
                        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified()));
                        String type = file.isFile() ? "            " : "<DIR>";
                        String size = file.isFile() ? String.valueOf(file.length()) : "           0";
                        String name = file.getName();

                        return displayMessageBuilder.append(dateTime)
                                .append("            ")
                                .append(type)
                                .append("            ")
                                .append(size)
                                .append("            ")
                                .append(name).toString();
                    })
                    .forEach(System.out::println);
        }
    }

    /**
     * @see {@link FilenameFilter}
     */
    private interface FilePredicateAdapter extends Predicate<File>, FileFilter {
        @Override
        default boolean accept(File pathname) {
            return test(pathname);
        }
    }

    private class FileSpace {

        private final File file;
        private final FilePredicateAdapter filter;

        public FileSpace(File file,FilePredicateAdapter adapter) {
            this.file = file;
            this.filter = adapter;
        }

        public long size() {

            if (file.isFile()) {
                return filter.test(file) ? file.length() : 0L;
            }

            long totalSize = 0L;
            File[] subFiles = file.listFiles();

            totalSize += Stream.of(subFiles)
                    .filter(File::isFile)
                    .filter(filter)
                    .map(File::length)
                    .reduce(Long::sum)
                    .orElse(0L);

            totalSize += Stream.of(subFiles)
                    .filter(File::isDirectory)
                    .map(f -> new FileSpace(f,filter))
                    .map(FileSpace::size)
                    .reduce(Long::sum)
                    .orElse(0L);

            return totalSize;
        }
    }

    public static void main(String[] args) {
        FileAPISample fileAPI = new FileAPISample();
        fileAPI.new WinCMDDIRCommand(new File("C:\\users\\TMC")).exec();
        long size = fileAPI.new FileSpace(new File("C:\\Temp"),file -> {
            //System.out.println("fileName:"+file.getName());
            return file.getName().endsWith(".log");
        }).size();
        System.out.println("size:["+size +" Byte and "+(size / 1024.0D) + " KB]");
    }
}
