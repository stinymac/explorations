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

package org.mac.explorations.algs.ds.map;

/**
 * @auther mac
 * @date 2019-11-01
 */
public class Main {
    public static void main(String[] args) {
        Map<String,Integer> wordsCount = new LinkedListMap<>();
        String[] words = "The time has passed, it is not too late Although I can't, my heart is longing for it".split(" ");

        for (String word : words) {
            if (wordsCount.containsKey(word)) {
                Integer count = wordsCount.get(word);
                wordsCount.put(word,count + 1);
            }
            else {
                wordsCount.put(word,1);
            }
        }
        System.out.println(wordsCount);
        System.out.println(wordsCount.get("it"));
    }
}
