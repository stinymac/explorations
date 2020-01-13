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

package org.mac.explorations.storage.redis.bloom;

import java.util.List;
import java.util.Map;

/**
 * 布隆过滤器接口
 *
 * @auther mac
 * @date 2020-01-07
 */
public interface BloomFilter<T> {
    /**
     * 添加
     *
     * @param object
     * @return 是否添加成功
     */
    boolean add(T object);

    /**
     * 批量添加
     *
     * @param objects
     * @return
     */
    Map<Object,Boolean> batchAdd(List<T> objects);

    /**
     * 是否包含指定元素
     *
     * @param object
     * @return
     */
    boolean contains(T object);

    /**
     * 预期插入数量
     * @return
     */
    long getExpectedInsertions();

    /**
     * 预期错误概率
     * @return
     */
    double getErrorProbability();

    /**
     * 布隆过滤器总长度
     * @return
     */
    long getSize();

    /**
     * hash函数迭代次数
     *
     * @return
     */
    int getHashIterations();
}
