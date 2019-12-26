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

/**
 * 代理对象和被代理对象不一定存在层次关系或者不在同一个继承层次
 * 被代理的功能是被代理对象的子集
 *
 * @auther mac
 * @date 2019-12-19
 */
public class CharSequenceProxy {

    private final CharSequence delegate;

    public CharSequenceProxy(CharSequence delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
