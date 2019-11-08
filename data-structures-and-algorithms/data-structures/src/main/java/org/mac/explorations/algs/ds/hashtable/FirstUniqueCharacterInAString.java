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

package org.mac.explorations.algs.ds.hashtable;

import java.util.Objects;

/**
 *  字符串中的第一个唯一字符
 *
 *  @see {https://leetcode-cn.com/problems/first-unique-character-in-a-string/}
 *
 *  给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
 *
 *  s = "leetcode"
 *  返回 0.
 *
 *  s = "loveleetcode",
 *  返回 2.
 *
 *  假定该字符串只包含小写字母。
 *
 * @auther mac
 * @date 2019-11-07
 */
public class FirstUniqueCharacterInAString {

    private static final int NUMBER_OF_LETTERS = 26;

    public int firstUniqChar(String s) {

        Objects.requireNonNull(s);
        int length  = s.length();
        int[] charFrequency = new int[NUMBER_OF_LETTERS];

        for (int i = 0; i < length; i++) {
            int index = s.charAt(i) - 'a';
            int frequency = charFrequency[index];
            charFrequency[index] = frequency + 1;
        }

        for (int i = 0; i < length; i++) {
            if (charFrequency[s.charAt(i) - 'a'] == 1){
                return i;
            }
        }

        return -1;
    }
}
