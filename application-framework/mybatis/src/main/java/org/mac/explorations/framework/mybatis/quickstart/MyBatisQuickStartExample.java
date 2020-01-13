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

package org.mac.explorations.framework.mybatis.quickstart;

import org.apache.ibatis.session.SqlSession;
import org.mac.explorations.framework.mybatis.BaseExample;
import org.mac.explorations.framework.mybatis.Employee;
import org.mac.explorations.framework.mybatis.EmployeeMapper;

/**
 * 1、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 2、SqlSession和connection一样都是非线程安全。每次使用都应该去获取新的对象。
 * 3、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		(将接口和xml进行绑定)
 * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
 *
 * 4、两个重要的配置文件：
 * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件：保存了每一个sql语句的映射信息：将sql抽取出来。
 *
 * @auther mac
 * @date 2020-01-08
 */
public class MyBatisQuickStartExample extends BaseExample{

    @Override
    protected void doExample(SqlSession openSession) {
        // mapper文件解析方式
        // Employee employee = openSession.selectOne("org.mac.explorations.framework.mybatis.quickstart.getEmpById", 1);
        // 接口方式
        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
        Employee employee = mapper.getEmployeeById(1);

        System.out.println(employee);
    }


    public static void main(String[] args) {
        new MyBatisQuickStartExample().execute();
    }
}
