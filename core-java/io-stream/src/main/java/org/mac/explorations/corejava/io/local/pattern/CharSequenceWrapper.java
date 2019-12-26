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

package org.mac.explorations.corejava.io.local.pattern;

import java.util.stream.IntStream;

/**
 *  装饰器模式/包装模式
 *
 * @auther mac
 * @date 2019-12-19
 */
public class CharSequenceWrapper implements CharSequence{

    private final CharSequence delegate;

    public CharSequenceWrapper(CharSequence delegate) {
        this.delegate = delegate;
    }

    public int length() {
        return delegate.length();
    }

    public char charAt(int index) {
        return delegate.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return delegate.subSequence(start, end);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    public IntStream chars() {
        return delegate.chars();
    }

    public IntStream codePoints() {
        return delegate.codePoints();
    }

    public String getDescription() {
        return toString();
    }
}
