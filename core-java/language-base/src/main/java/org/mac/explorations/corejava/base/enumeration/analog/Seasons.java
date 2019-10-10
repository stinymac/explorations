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

package org.mac.explorations.corejava.base.enumeration.analog;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 模拟实现Java枚举
 *
 * @auther mac
 * @date 2019-10-03
 */

public final class Seasons {

    public static final Seasons SPRING = new Seasons(0);
    public static final Seasons SUMMER = new Seasons(1);
    public static final Seasons AUTUMN = new Seasons(2);
    public static final Seasons WINTER = new Seasons(3);

    private int value;

    private Seasons(int value) {
        this.value = value;
    }

    public  static final Seasons[] values() {

        return Stream.of(Seasons.class.getDeclaredFields())
                .filter(field -> {

                    int modifiers = field.getModifiers();

                    return Modifier.isPublic(modifiers)
                            && Modifier.isStatic(modifiers)
                            && Modifier.isFinal(modifiers);
                })
                .map(field -> {
                    try {
                        return (Seasons) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .collect(Collectors.toList())
                .toArray(new Seasons[0]);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
