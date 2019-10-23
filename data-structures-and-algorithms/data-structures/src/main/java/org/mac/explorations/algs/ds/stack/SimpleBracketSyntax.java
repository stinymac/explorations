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

package org.mac.explorations.algs.ds.stack;

/**
 * 有效的括号
 *
 * @link {https://leetcode-cn.com/problems/valid-parentheses/}
 *
 * @auther mac
 * @date 2019-10-22
 */
public class SimpleBracketSyntax {

    public enum Brackets {

        OPEN_BRACE('{'),
        CLOSE_BRACE('}'),

        OPEN_BRACKETS('['),
        CLOSE_BRACKETS(']'),

        OPEN_PARENTHESIS('('),
        CLOSE_PARENTHESIS(')');

        private final char symbol;

        Brackets(char symbol) {
            this.symbol = symbol;
        }

        public static boolean isMatching (char openSymbol,char closeSymbol) {

            return (openSymbol == OPEN_BRACE.symbol && closeSymbol == CLOSE_BRACE.symbol)
                    || (openSymbol == OPEN_BRACKETS.symbol && closeSymbol == CLOSE_BRACKETS.symbol)
                    || (openSymbol == OPEN_PARENTHESIS.symbol && closeSymbol == CLOSE_PARENTHESIS.symbol) ;
        }
    }

    /**
     * 一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
     *
     * 有效字符串需满足：
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {

        Stack<Character> stack = new ArrayStack<>();

        int length = s.length();
        for (int i = 0; i < length; i++) {

            char c = s.charAt(i);

            if (c == Brackets.OPEN_BRACE.symbol
                    || c == Brackets.OPEN_BRACKETS.symbol
                    || c == Brackets.OPEN_PARENTHESIS.symbol ) {
                // 左括号入栈
                stack.push(c);
            }
            else { // 右括号检查是否匹配
                // 没有配对的左括号
                if (stack.isEmpty()) {
                    return false;
                }

                char topElement = stack.pop();
                if (!Brackets.isMatching(topElement,c)) {
                    return false;
                }
            }
        }
        //栈中若有元素则表示括号没有关闭
        return stack.isEmpty();
    }
}
