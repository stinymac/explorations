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

package org.mac.explorations.corejava.collections.filling.generator;

import org.mac.explorations.corejava.base.Generator;

/**
 * 简单的string生成器
 *
 * @auther mac
 * @date 2019-10-12
 */
public class SimpleStringGenerator implements Generator<String> {

    private final String[] foundation = "strange women lying in ponds distributing swords is no basis for a system of government".split(" ");
    private int index;

    @Override
    public String next() {
        return foundation[index++ % foundation.length];
    }
}
