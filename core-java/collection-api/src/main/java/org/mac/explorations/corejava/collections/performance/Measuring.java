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

package org.mac.explorations.corejava.collections.performance;

/**
 * 容器性能测试
 *
 * @auther mac
 * @date 2019-10-18
 */
public abstract class Measuring<C> {

    private final String name;

    public Measuring(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract int execute(C container, Parameter parameter);

    public static class Parameter {

        public final int size;
        public final int loops;

        public Parameter(int size, int loops) {
            this.size = size;
            this.loops = loops;
        }
    }
}
