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

package org.mac.explorations.algs.ds.set;

import java.util.Objects;

/**
 * 唯一摩尔斯密码词
 *
 * @see  {https://leetcode-cn.com/problems/unique-morse-code-words/}
 *
 * 26个英文字母对应摩尔斯密码表如下
 *
 * [".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."]
 *
 * @auther mac
 * @date 2019-11-01
 */
public class UniqueMorseCodeword {
    /**
     * 26个英文字母对应摩尔斯密码表
     */
    private static final String[] LETTERS_MORSE_CODE = {".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};

    /**
     * 所有词单词翻译的不同数量
     *
     * 单词列表words 的长度不会超过 100。
     * 每个单词 words[i]的长度范围为 [1, 12]。
     * 每个单词 words[i]只包含小写字母。
     *
     * @param words
     * @return
     */
    public static int of (String[] words) {
        Objects.requireNonNull(words);

        Set<String> set = new BSTSet<>();

        int length = words.length;
        for (int i = 0; i < length; i++) {
            String morseCode = toMorseCode(words[i]);
            set.add(morseCode);
        }
        return set.size();
    }

    private static String toMorseCode(String word) {
        word = word.toLowerCase();
        int wordLength = word.length();
        StringBuilder morseCodeBuilder = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {

            morseCodeBuilder.append(LETTERS_MORSE_CODE[word.charAt(i) - 'a']);
        }
        return morseCodeBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(toMorseCode("Hello") );
        String[] words = {"gin", "zen", "gig", "msg"};
        System.out.println(of(words));
    }
}
