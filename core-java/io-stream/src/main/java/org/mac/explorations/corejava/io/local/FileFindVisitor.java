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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class FileFindVisitor extends SimpleFileVisitor<Path> {

    private final PathMatcher pathMatcher;

    private AtomicInteger foundCount = new AtomicInteger(0);

    public FileFindVisitor(String pattern) {
        this.pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
        matchFile(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) {
        matchFile(dir);
        return FileVisitResult.CONTINUE;
    }

    private void matchFile(Path file) {
        Path name = file.getFileName();
        if (name != null && pathMatcher.matches(name)) {
            foundCount.getAndIncrement();
            System.out.printf("Found file : %s\n", file);
        }
    }

    public int getFoundCount() {
        return foundCount.get();
    }

    public static void main(String[] args) throws IOException {
        String pattern = "F[a-zA-Z]*.java";
        FileFindVisitor findVisitor = new FileFindVisitor(pattern);
        Files.walkFileTree(Paths.get(System.getProperty("user.dir")), findVisitor);
        System.out.printf("Found count : %s\n", findVisitor.getFoundCount());
    }
}
