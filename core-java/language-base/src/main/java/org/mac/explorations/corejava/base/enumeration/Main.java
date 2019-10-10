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

package org.mac.explorations.corejava.base.enumeration;

import java.util.Arrays;

/**
 * @auther mac
 * @date 2019-10-03
 */

public class Main {

    public static void main(String[] args) {

        System.out.println(Seasons.SPRING);
        System.out.println(org.mac.explorations.corejava.base.enumeration.analog.Seasons.SPRING);
        System.out.println(Seasons.SUMMER);
        System.out.println(org.mac.explorations.corejava.base.enumeration.analog.Seasons.SUMMER);
        System.out.println(Seasons.AUTUMN);
        System.out.println(org.mac.explorations.corejava.base.enumeration.analog.Seasons.AUTUMN);
        System.out.println(Seasons.WINTER);
        System.out.println(org.mac.explorations.corejava.base.enumeration.analog.Seasons.WINTER);

        System.out.println(Arrays.toString(Seasons.values()));
        System.out.println(Arrays.toString(org.mac.explorations.corejava.base.enumeration.analog.Seasons.values()));
    }
}
