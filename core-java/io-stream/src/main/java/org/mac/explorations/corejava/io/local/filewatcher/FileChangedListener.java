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

import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;

/**
 * @auther mac
 * @date 2019-12-20
 */
@FunctionalInterface
public interface FileChangedListener extends EventListener,Observer, Consumer<FileChangedEvent> {

    void onChanged(FileChangedEvent event);

    default void accept(FileChangedEvent event) {
        this.onChanged(event);
    }

    default void update(Observable o, Object event) {
        if (event instanceof FileChangedEvent) {
            onChanged((FileChangedEvent) event);
        }
    }
}
