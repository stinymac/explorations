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
import java.util.Observable;
import java.util.function.Consumer;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class FileChangedEventPublisher extends Observable {


    public void publish(File changedFile) {
        publish(new FileChangedEvent(changedFile));
    }

    public void publish(FileChangedEvent event) {
        // 标记状态已经改变
        super.setChanged();
        super.notifyObservers(event);
    }

    public void publishTo(Consumer<FileChangedEvent> consumer,File changedFile) {
        consumer.accept(new FileChangedEvent(changedFile));
    }

    public void addFileChangedListener(FileChangedListener listener) {
        addObserver(listener);
    }
}
