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

import org.mac.explorations.corejava.collections.filling.Countries;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @auther mac
 * @date 2019-10-17
 */

public class Main {

    public static void main(String[] args) {
        SlowMap<String,String> map = new SlowMap<>();
        map.putAll(Countries.capitals(15));
        System.out.println(map);
        System.out.println(map.get("ALGERIA"));
        System.out.println(map.entrySet());

        System.out.println("==============================================================================");

        SimpleHashMap<String,String> smap = new SimpleHashMap();
        smap.putAll(Countries.capitals(15));
        System.out.println(smap);
        System.out.println(smap.get("ALGERIA"));
        System.out.println(smap.entrySet());

        System.out.println("==============================================================================");
        Map<PlainHashCode,Integer> csim = new HashMap<>();
        PlainHashCode[] cs = new PlainHashCode[5];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = new PlainHashCode("hi");
            csim.put(cs[i],i);
        }
        System.out.println(csim);
        for (PlainHashCode i : cs) {
            System.out.println("Look up "+i + " ->"+csim.get(i));
        }
    }
}
