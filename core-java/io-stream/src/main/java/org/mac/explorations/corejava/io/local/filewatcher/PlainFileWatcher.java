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

package org.mac.explorations.corejava.io.local.filewatcher;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class PlainFileWatcher {

    private Map<File, Long> lastModifiedSnapshot = new LinkedHashMap<>();

    private ScheduledExecutorService pollingWatchingService = Executors.newScheduledThreadPool(1);

    private FileChangedEventPublisher eventPublisher = new FileChangedEventPublisher();

    public void monitor(final File monitoredFile) {
        updateLastModifiedSnapshot(monitoredFile,monitoredFile.lastModified());
        // 判断是否变化
        pollingWatchingService.scheduleAtFixedRate(() -> {
            long currentLastModified = monitoredFile.lastModified();
            long previousLastModified = lastModifiedSnapshot.putIfAbsent(monitoredFile, monitoredFile.lastModified());

            if (currentLastModified > previousLastModified) {
                eventPublisher.publish(monitoredFile);
                eventPublisher.publishTo(event -> {
                    System.out.println("默认的处理文件变化事件：" + event);
                },monitoredFile);
                updateLastModifiedSnapshot(monitoredFile,currentLastModified);
            }

        }, 0L, 5L, TimeUnit.SECONDS);
    }

    private void updateLastModifiedSnapshot(File monitoredFile,long lastModified) {
        lastModifiedSnapshot.put(monitoredFile, lastModified);
    }

    public void addListeners(FileChangedListener listener, FileChangedListener... otherListeners) {
        eventPublisher.addFileChangedListener(listener);
        Stream.of(otherListeners).forEach(eventPublisher::addFileChangedListener);
    }

    public static void main(String[] args) {
        PlainFileWatcher fileWatcher = new PlainFileWatcher();

        fileWatcher.addListeners(event -> {
            System.out.println("处理文件变化事件：" + event);
        });

        fileWatcher.monitor(new File("C:\\Temp"));
    }

}
