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
import java.util.EventObject;

/**
 * @auther mac
 * @date 2019-12-20
 */
public class FileChangedEvent extends EventObject{

    public FileChangedEvent(File source) {
        super(source);
    }

    @Override
    public File getSource() {
        return (File) super.getSource();
    }
}
