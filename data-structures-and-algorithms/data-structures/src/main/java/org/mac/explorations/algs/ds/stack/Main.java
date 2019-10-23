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
 * @auther mac
 * @date 2019-10-22
 */
public class Main {

    public static void main(String[] args) {
        Stack<Integer> stack = new ArrayStack<>();

        stack.push(1);
        stack.push(2);
        stack.push(3);

        System.out.println(stack);

        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        //System.out.println(stack.peek());

        System.out.println(stack);

        System.out.println(SimpleBracketSyntax.isValid("()[]{}"));
        System.out.println(SimpleBracketSyntax.isValid("([)]"));
        System.out.println(SimpleBracketSyntax.isValid("{[]}"));
    }
}
