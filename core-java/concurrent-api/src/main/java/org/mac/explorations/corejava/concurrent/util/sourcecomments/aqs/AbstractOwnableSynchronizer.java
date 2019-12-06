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

package org.mac.explorations.corejava.concurrent.util.sourcecomments.aqs;

/**
 * 源码注释
 *
 *     可被线程独占的同步器,该类为创建锁或所有权相关的同步器提供了顶层抽象
 *     子类可以通过维护值(exclusiveOwnerThread)来实现监视器访问或提供诊断
 *     (to help control and monitor access and provide diagnostics.)
 *
 * @date 2019-12-01
 */
public abstract class AbstractOwnableSynchronizer implements java.io.Serializable {

    private static final long serialVersionUID = 3737899427754241961L;

    protected AbstractOwnableSynchronizer() { }

    /**
     * 独占模式同步器的当前持有线程.
     *
     */
    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
