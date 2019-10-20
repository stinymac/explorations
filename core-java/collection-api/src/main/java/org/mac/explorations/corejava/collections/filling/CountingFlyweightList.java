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

package org.mac.explorations.corejava.collections.filling;

import java.util.AbstractList;

/**
 *
 *
 * @auther mac
 * @date 2019-10-16
 */

public class CountingFlyweightList extends AbstractList<Integer>{

    private final int size;

    public CountingFlyweightList(int size) {
        this.size = size < 0 ? 0 : size;
    }

    @Override
    public Integer get(int index) {

        if (index < 0 || index > size -1) {
            throw new IndexOutOfBoundsException("index "+index+" out of bounds:[0,"+(size-1)+"]");
        }
        return Integer.valueOf(index);
    }

    @Override
    public int size() {
        return size;
    }
}
