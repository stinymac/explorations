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

package org.mac.explorations.framework.mybatis.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Properties;

/**
 * MyBatis在SQL执行的流程中创建4个对象
 *
 * @see org.apache.ibatis.executor.Executor
 * @see org.apache.ibatis.executor.statement.StatementHandler
 * @see org.apache.ibatis.executor.parameter.ParameterHandler
 * @see org.apache.ibatis.executor.resultset.ResultSetHandler
 *
 * 初始化这4个对象时都会调用@see {@link org.apache.ibatis.plugin.InterceptorChain#pluginAll(java.lang.Object)}
 * (多个插件将产生多层嵌套代理)
 * 即 @see {@link org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)} 用拦截器包装对应的对象(代理)
 * 默认使用@see {@link org.apache.ibatis.plugin.Plugin#wrap(java.lang.Object, org.apache.ibatis.plugin.Interceptor)}
 * 这就是MyBatis插件扩展的原理
 *
 * @auther mac
 * @date 2020-01-11
 */
@Intercepts({
        //指定拦截那个对象的那个方法
        @Signature(type=org.apache.ibatis.executor.statement.StatementHandler.class,
                method = "parameterize",
                args = java.sql.Statement.class)
})
public class SimplePlugin implements Interceptor {
    /**
     * 拦截目标方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        System.out.println("SimplePlugin intercept target:"+statementHandler);
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        System.out.println("SQL:"+sql);
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        Object value = metaObject.getValue("parameterHandler.parameterObject");
        System.out.println("SQL parameter:"+value);
        // 执行目标方法
        return invocation.proceed();
    }

    /*
    @see org.apache.ibatis.plugin.Interceptor.plugin
    @Override
    public Object plugin(Object target) {
        return null;
    }
    */

    @Override
    public void setProperties(Properties properties) {
        System.out.println("plugin's properties:"+properties);
    }
}
