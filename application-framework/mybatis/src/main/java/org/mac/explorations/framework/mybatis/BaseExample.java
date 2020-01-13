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

package org.mac.explorations.framework.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @auther mac
 * @date 2020-01-10
 */
public abstract class  BaseExample {

    private static final String GLOBAL_CONFIGURATION = "configuration/mybatis-config.xml";
    private static final SqlSessionFactory SQL_SESSION_FACTORY;
    protected final boolean batchExecutor;

    static {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(GLOBAL_CONFIGURATION);
            SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("can't found "+ GLOBAL_CONFIGURATION);
        } finally {
            Utils.release(inputStream);
        }
    }

    public BaseExample() {
        this(false);
    }

    public BaseExample(boolean batchExecutor) {
        this.batchExecutor = batchExecutor;
    }

    public void execute() {
        SqlSession openSession = null;
        try {
            /**
             * 1、mybatis允许增删改直接定义以下类型返回值
             * 		Integer、Long、Boolean、void
             * 2、提交数据
             * 		sqlSessionFactory.openSession();手动提交
             * 		sqlSessionFactory.openSession(true);自动提交
             */
            openSession = batchExecutor ? SQL_SESSION_FACTORY.openSession(ExecutorType.BATCH):SQL_SESSION_FACTORY.openSession();

            doExample(openSession);
        } finally {
            openSession.close();
        }
    }

    protected abstract void doExample(SqlSession openSession);
}
