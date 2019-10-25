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
        Stack<Integer> arrayStack = new ArrayStack<>();

        arrayStack.push(1);
        arrayStack.push(2);
        arrayStack.push(3);

        System.out.println(arrayStack);

        System.out.println(arrayStack.peek());
        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.pop());
        //System.out.println(stack.peek());

        System.out.println(arrayStack);

        System.out.println(SimpleBracketSyntax.isValid("()[]{}"));
        System.out.println(SimpleBracketSyntax.isValid("([)]"));
        System.out.println(SimpleBracketSyntax.isValid("{[]}"));

        Stack<Integer> linkedListStack = new LinkedListStack<>();

        linkedListStack.push(1);
        linkedListStack.push(2);
        linkedListStack.push(3);

        System.out.println(linkedListStack);

        System.out.println(linkedListStack.peek());
        System.out.println(linkedListStack.pop());
        System.out.println(linkedListStack.pop());
        System.out.println(linkedListStack.pop());
        //System.out.println(linkedListStack.pop());
    }
}
