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

package org.mac.explorations.corejava.collections.hashcode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @date 2019-10-18
 */
public class PlainHashCode {
    private static final List<String> CREATED = new ArrayList<>();

    private String s;
    private int id;

    public PlainHashCode(String str) {
        this.s = str;
        CREATED.add(s);
        for (String i : CREATED) {
            if (i.equals(s)) {
                id++;
            }
        }
    }

    @Override
    public String toString() {
        return "String:" + s + " id:"+id+" hashCode:"+hashCode();
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + s.hashCode();
        result = 37 * result + id;

        return result;
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof PlainHashCode
                && s.equals(((PlainHashCode) another).s)
                && id == ((PlainHashCode) another).id;
    }
}
