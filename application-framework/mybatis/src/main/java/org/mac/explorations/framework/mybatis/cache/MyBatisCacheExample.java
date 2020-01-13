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

package org.mac.explorations.framework.mybatis.cache;

import org.apache.ibatis.session.SqlSession;
import org.mac.explorations.framework.mybatis.BaseExample;
import org.mac.explorations.framework.mybatis.Employee;
import org.mac.explorations.framework.mybatis.EmployeeMapper;

/**
 *
 *
 * @auther mac
 * @date 2020-01-10
 */
public class MyBatisCacheExample extends BaseExample{
    /**
     * 两级缓存：
     * 一级缓存：（本地缓存）
     *      sqlSession级别的缓存。一级缓存是一直开启的；实际是SqlSession的一个Map
     * 		与数据库同一次会话期间查询到的数据会放在本地缓存中。以后如果需要获取相同的数据，直接从缓存中拿；
     *
     * 		一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，还需要再向数据库发出查询）：
     * 		1、sqlSession不同。
     * 		2、sqlSession相同，查询条件不同.(当前一级缓存中还没有这个数据)
     * 		3、sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前数据有影响)
     * 		4、sqlSession相同，手动清除了一级缓存（缓存清空）
     *
     * 二级缓存：（全局缓存）
     *     基于namespace级别的缓存：一个namespace对应一个二级缓存
     *
     * 	   1、一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     * 	   2、如果会话关闭；一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容；
     * 	   3、不同namespace查出的数据会放在自己对应的缓存中（map）数据会从二级缓存中获取
     *
     * 		  查出的数据都会被默认先放在一级缓存中。
     * 		  只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中
     *
     * 		使用：
     * 			1）、开启全局二级缓存配置：<setting name="cacheEnabled" value="true"/>
     * 			2）、mapper.xml中配置使用二级缓存：
     * 				<cache></cache>
     * 			3）、POJO需要实现序列化接口
     *
     * 和缓存有关的设置/属性：
     * 			1）、cacheEnabled=true：false：关闭缓存（二级缓存关闭）(一级缓存一直可用的)
     * 			2）、每个select标签都有useCache="true"：
     * 				false：不使用缓存（一级缓存依然使用，二级缓存不使用）
     *
     * 			3）、【每个增删改标签的：flushCache="true"：（一级二级都会清除）】
     * 					 增删改执行完成后就会清楚缓存；
     * 					 flushCache="true"：一级缓存就清空了；二级也会被清除；
     *
     * 					查询标签：flushCache="false"：
     * 					如果flushCache=true;每次查询之后都会清空缓存；缓存是没有被使用的；
     *
     * 			4）、sqlSession.clearCache();只是清楚当前session的一级缓存；
     *
     * 			5）、localCacheScope(全局配置属性)：本地缓存作用域：（一级缓存SESSION）；当前会话的所有数据保存在会话缓存中；
     * 								STATEMENT：可以禁用一级缓存；
     * @param openSession
     */
    @Override
    protected void doExample(SqlSession openSession) {

        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);

        Employee employee = mapper.getEmployeeById(1);
        System.out.println(employee);

        Employee queryAgain = mapper.getEmployeeById(1);
        System.out.println(queryAgain);

        System.out.println(employee == queryAgain);

        //每个增删改标签默认的：flushCache="true"：（一级二级都会清除）
        /**
         * @see org.apache.ibatis.session.defaults.DefaultSqlSession#dirty
         * session 的dirty(变脏)这个变量在"增删改"时会被标记为true
         * 若为true在session关闭时不会将一级缓存放入二级缓存
         * 调用 openSession.commit()/rollback();将"增删改"提交 dirty会恢复为false
         */
        mapper.addEmployee(new Employee("lily",8,"1","lily@163.com"));
        openSession.commit();
        System.out.println(employee == mapper.getEmployeeById(1));

    }

    private static class OtherCacheExample extends BaseExample {
        @Override
        protected void doExample(SqlSession otherOpenSession) {
            EmployeeMapper mapper = otherOpenSession.getMapper(EmployeeMapper.class);
            mapper.getEmployeeById(1);
        }
    }

    public static void main(String[] args) {
        new MyBatisCacheExample().execute();
        System.out.println("===========二级缓存=============");
        new OtherCacheExample().execute();
        new OtherCacheExample().execute();
        System.out.println("========================");
    }
}
