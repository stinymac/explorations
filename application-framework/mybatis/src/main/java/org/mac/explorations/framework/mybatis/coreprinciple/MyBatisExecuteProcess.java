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

package org.mac.explorations.framework.mybatis.coreprinciple;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.mac.explorations.framework.mybatis.EmployeeMapper;
import org.mac.explorations.framework.mybatis.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * MyBatis启动初始化及SQL执行流程
 *
 * 1.创建SqlSessionFactory
 *
 *     new SqlSessionFactoryBuilder() @see {@link org.apache.ibatis.session.SqlSessionFactoryBuilder}
 *     指定配置文件输入流给build方法
 *     new SqlSessionFactoryBuilder().build(inputStream); @see {@link org.apache.ibatis.session.SqlSessionFactoryBuilder#build(java.io.InputStream, java.lang.String, java.util.Properties)}
 *          配置文件输入流被传入XMLConfigBuilder用于解析配置信息
 *          @see org.apache.ibatis.builder.xml.XMLConfigBuilder
 *          @see org.apache.ibatis.parsing.XPathParser#XPathParser(java.io.InputStream, boolean, java.util.Properties, org.xml.sax.EntityResolver)
 *
 *          @see org.apache.ibatis.builder.xml.XMLConfigBuilder#XMLConfigBuilder(org.apache.ibatis.parsing.XPathParser, java.lang.String, java.util.Properties)
 *          <pre>
 *              // 创建一个Configuration对象 @see {@link org.apache.ibatis.builder.BaseBuilder}
 *              // @see  {@link org.apache.ibatis.session.Configuration#Configuration()}
 *              // Configuration对象中包含了运行时的各个参数已经配置解析后的注册容器等
 *              super(new Configuration());
 *              ErrorContext.instance().resource("SQL Mapper Configuration");
 *              this.configuration.setVariables(props);
 *              this.parsed = false;
 *              this.environment = environment;
 *              this.parser = parser; // XPathParser
 *          </pre>
 *          使用初始化后的XMLConfigBuilder解析配置文件 @see {@link org.apache.ibatis.builder.xml.XMLConfigBuilder#parse()}
 *          <pre>
 *              // XPathParser解析全局配置文件的configuration节点
 *              // 返回XNode 由XMLConfigBuilder解析详细配置 properties settings ...mappers
 *              // @see {@link org.apache.ibatis.builder.xml.XMLConfigBuilder#parseConfiguration(org.apache.ibatis.parsing.XNode)}
 *              parseConfiguration(parser.evalNode("/configuration"));
 *          </pre>
 *
 *          全局配置解析完成后最后解析mapper  @see {@link  org.apache.ibatis.builder.xml.XMLConfigBuilder#mapperElement(org.apache.ibatis.parsing.XNode)}
 *          一般使用package指定批量的mapper接口和文件(约定mapper文件和Mapper接口放置在同一包下)
 *          <pre>
 *               if ("package".equals(child.getName())) {
 *                   String mapperPackage = child.getStringAttribute("name");
 *                   configuration.addMappers(mapperPackage);
 *               }
 *          </pre>
 *          Mapper接口被注册到MapperRegistry对象
 *          @see org.apache.ibatis.binding.MapperRegistry#addMappers(java.lang.String, java.lang.Class)
 *          @see org.apache.ibatis.binding.MapperRegistry#addMapper(java.lang.Class)
 *          <pre>
 *              // 以Mapper接口类型为key 以其创建的代理工厂对象为value
 *              knownMappers.put(type, new MapperProxyFactory<>(type));
 *              // It's important that the type is added before the parser is run
 *              // otherwise the binding may automatically be attempted by the
 *              // mapper parser. If the type is already known, it won't try.
 *              // Mapper中的注解解析
 *              MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
 *              parser.parse();
 *          </pre>
 *          注册后解析Mapper接口绑定的Mapper XML文件或注解
 *          @see org.apache.ibatis.builder.annotation.MapperAnnotationBuilder#parse()
 *          使用XMLMapperBuilder解析Mapper XML文件
 *          @see org.apache.ibatis.builder.xml.XMLMapperBuilder#parse()
 *          @see org.apache.ibatis.builder.xml.XMLMapperBuilder#configurationElement(org.apache.ibatis.parsing.XNode)
 *          // 主要是解析SQL语句
 *          @see org.apache.ibatis.builder.xml.XMLMapperBuilder#buildStatementFromContext(java.util.List, java.lang.String)
 *          // 构建XMLStatementBuilder解析XML文件中的SQL语句
 *          @see org.apache.ibatis.builder.xml.XMLStatementBuilder#parseStatementNode()
 *          @see org.apache.ibatis.builder.MapperBuilderAssistant#addMappedStatement(java.lang.String, org.apache.ibatis.mapping.SqlSource, org.apache.ibatis.mapping.StatementType, org.apache.ibatis.mapping.SqlCommandType, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Class, java.lang.String, java.lang.Class, org.apache.ibatis.mapping.ResultSetType, boolean, boolean, boolean, org.apache.ibatis.executor.keygen.KeyGenerator, java.lang.String, java.lang.String, java.lang.String, org.apache.ibatis.scripting.LanguageDriver, java.lang.String)
 *          //Mapper中的SQL语句至此被解析并包装为MappedStatement对象(即一个SQL语句一个MappedStatement对象)
 *
 *    配置对象初始化后 将其传入 @see {@link  org.apache.ibatis.session.defaults.DefaultSqlSessionFactory}构建一个SqlSessionFactory对象
 *
 * 创建SqplSession总结:
 *                    加载配置文件解析全局配置和Mapper
 *                    Mapper接口注册到Configuration对象中 以Class<mapperType> 为key value为 MapperProxyFactory
 *                    "增删改查"语句解析后封装为MapedStatemet对象组成到Configuration对象中
 *                    传入配置对象到DefaultSqlSessionFactory构建SqlSessionFactory后返回
 *
 * 2.从SqlSessionFactory中打开一个SqlSession对象
 *
 *     @see org.apache.ibatis.session.defaults.DefaultSqlSessionFactory#openSessionFromDataSource(org.apache.ibatis.session.ExecutorType, org.apache.ibatis.session.TransactionIsolationLevel, boolean)
 *     <pre>
 *         final Environment environment = configuration.getEnvironment();
 *         final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
 *         // 从全局配置中获取dataSource和事务配置
 *         // dataSource类型@see {@link org.apache.ibatis.datasource.pooled.PooledDataSource}
 *         // 事务类型为@see {@link org.apache.ibatis.transaction.jdbc.JdbcTransaction}
 *         tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
 *         //创建执行器 执行器类型默认为SIMPLE
 *         final Executor executor = configuration.newExecutor(tx, execType);
 *         // 创建并返回SqlSession
 *         return new DefaultSqlSession(configuration, executor, autoCommit);
 *     </pre>
 *     创建执行器@see {@link org.apache.ibatis.session.Configuration#newExecutor(org.apache.ibatis.transaction.Transaction, org.apache.ibatis.session.ExecutorType)}
 *     <pre>
 *          executorType = executorType == null ? defaultExecutorType : executorType;
 *          executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
 *          Executor executor;
 *          if (ExecutorType.BATCH == executorType) {
 *              executor = new BatchExecutor(this, transaction);
 *          } else if (ExecutorType.REUSE == executorType) {
 *              executor = new ReuseExecutor(this, transaction);
 *          } else {
 *              executor = new SimpleExecutor(this, transaction);
 *          }
 *          // 如果开启了二级缓存 将新创建的执行器包装为CachingExecutor
 *          if (cacheEnabled) {
 *              executor = new CachingExecutor(executor);
 *          }
 *          // 在执行器上注册拦截器链@see {@link org.apache.ibatis.plugin.InterceptorChain}
 *          // 即所有插件注册到执行器上
 *          executor = (Executor) interceptorChain.pluginAll(executor);
 *          return executor;
 *     </pre>
 *
 *     最后创建并返回SqlSession //new DefaultSqlSession(configuration, executor, autoCommit);
 *
 * 3.获取Mapper接口类的实现实例
 *
 *    调用Configration的getMapper()
 *    @see org.apache.ibatis.session.Configuration#getMapper(java.lang.Class, org.apache.ibatis.session.SqlSession)
 *    // MapperRegistry在 创建SqlSessionFactory 阶段 初始化
 *    调用 @see {@link org.apache.ibatis.binding.MapperRegistry#getMapper(java.lang.Class, org.apache.ibatis.session.SqlSession)}
 *    <pre>
 *        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
 *        if (mapperProxyFactory == null) {
 *            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
 *        }
 *        try {
 *            // 创建代理实例
 *            return mapperProxyFactory.newInstance(sqlSession);
 *        } catch (Exception e) {
 *            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
 *        }
 *    </pre>
 *    @see org.apache.ibatis.binding.MapperProxyFactory#newInstance(org.apache.ibatis.session.SqlSession)
 *    <pre>
 *        // 动态代理的InvocationHandler
 *        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
 *        return newInstance(mapperProxy);
 *    </pre>
 *
 *    创建代理对象
 *    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
 *
 * 4.代理对象执行SQL(以查询为例)
 *
 *    调用SQL时进入代理对象的InvocationHandler的invoke方法
 *    @see org.apache.ibatis.binding.MapperProxy#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
 *
 *    先解析出@see {@link org.apache.ibatis.binding.MapperMethod.SqlCommand#resolveMappedStatement(java.lang.Class, java.lang.String, java.lang.Class, org.apache.ibatis.session.Configuration)}
 *    该调用方法对应的SQL(即Configuration中的MappedStatement) 包装为@see {@link org.apache.ibatis.binding.MapperMethod}
 *    缓存在InvocationHandler中(即MapperProxy中)
 *
 *    @see org.apache.ibatis.binding.MapperProxy#cachedMapperMethod(java.lang.reflect.Method)
 *
 *    执行该方法(MapperMethod)
 *
 *    //mapperMethod.execute(sqlSession, args);
 *    @see org.apache.ibatis.binding.MapperMethod#execute(org.apache.ibatis.session.SqlSession, java.lang.Object[])
 *
 *    执行时首先做参数转换
 *    @see org.apache.ibatis.binding.MapperMethod.MethodSignature#convertArgsToSqlCommandParam(java.lang.Object[])
 *    // 一个参数直接返回 多个参数放入map以指定的参数名为key 参数值为value
 *    @see org.apache.ibatis.reflection.ParamNameResolver#getNamedParams(java.lang.Object[])
 *
 *
 *    SQL的ID和参数传入sqlSession的对应方法中调用获取结果(这里是selectOne(sqlCommandId , param))
 *
 *    @see org.apache.ibatis.session.defaults.DefaultSqlSession#selectOne(java.lang.String, java.lang.Object)
 *    @see org.apache.ibatis.session.defaults.DefaultSqlSession#selectList(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds)
 *
 *    从Configuration中按sqlCommandId(namespace+"."+sqlStatementId)取出对应的MappedStatement
 *    调用执行器的对应方法(这里是Query)
 *
 *    // 参数为: SQL语句(包装对象MappedStatement), SQL语句的参数,RowBounds,结果处理器ResultHandler
 *    @see org.apache.ibatis.executor.CachingExecutor#query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler)
 *    <pre>
 *         // 获取SQL
 *         //@see {@link org.apache.ibatis.mapping.BoundSql#BoundSql(org.apache.ibatis.session.Configuration, java.lang.String, java.util.List, java.lang.Object)}
 *         //@see {@link org.apache.ibatis.mapping.MappedStatement#getBoundSql(java.lang.Object)}
 *
 *         BoundSql boundSql = ms.getBoundSql(parameterObject);
 *         // 创建缓存key @see {@link org.apache.ibatis.executor.BaseExecutor#createCacheKey(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.mapping.BoundSql)}
 *         CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
 *
 *         //@see {@link org.apache.ibatis.executor.CachingExecutor#query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.cache.CacheKey, org.apache.ibatis.mapping.BoundSql)}
 *         return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
 *    </pre>
 *
 *    查询时先获取MappedStatement的缓存(二级缓存) 缓存中没有 调用SimpleExcutor的Query方法查询
 *    @see org.apache.ibatis.executor.BaseExecutor#query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.cache.CacheKey, org.apache.ibatis.mapping.BoundSql)
 *    <pre>
 *        // 从以及缓存中取值 检查是否命中缓存 没有命中则查询数据库
 *        list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
 *        if (list != null) {
 *            handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
 *        } else {
 *            list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
 *        }
 *    </pre>
 *
 *    查询数据库获取结果
 *
 *    @see org.apache.ibatis.executor.BaseExecutor#queryFromDatabase(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.cache.CacheKey, org.apache.ibatis.mapping.BoundSql)
 *    @see org.apache.ibatis.executor.SimpleExecutor#doQuery(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.mapping.BoundSql)
 *    <pre>
 *        Configuration configuration = ms.getConfiguration();
 *
 *        //创建StatementHandler(这里实际是@see {@link org.apache.ibatis.executor.statement.PreparedStatementHandler})并在其上注册拦截器链
 *        //@see {@link org.apache.ibatis.session.Configuration#newStatementHandler(org.apache.ibatis.executor.Executor, org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.mapping.BoundSql)}
 *        StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
 *
 *        //预编译@see {@link org.apache.ibatis.executor.SimpleExecutor#prepareStatement(org.apache.ibatis.executor.statement.StatementHandler, org.apache.ibatis.logging.Log)}
 *        stmt = prepareStatement(handler, ms.getStatementLog());
 *        return handler.query(stmt, resultHandler);
 *    </pre>
 *
 *    预编译SQL语句并设置参数
 *
 *    <pre>
 *         Connection connection = getConnection(statementLog);
 *         stmt = handler.prepare(connection, transaction.getTimeout());
 *         handler.parameterize(stmt);
 *    </pre>
 *    // SQL预编译
 *    @see org.apache.ibatis.executor.statement.BaseStatementHandler#prepare(java.sql.Connection, java.lang.Integer)
 *    @see org.apache.ibatis.executor.statement.PreparedStatementHandler#instantiateStatement(java.sql.Connection)
 *
 *    // 参数设置
 *    @see org.apache.ibatis.scripting.defaults.DefaultParameterHandler#setParameters(java.sql.PreparedStatement)
 *    <pre>
 *        ......
 *        else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
 *            value = parameterObject;
 *        }
 *        ......
 *        typeHandler.setParameter(ps, i + 1, value, jdbcType);
 *        ......
 *    </pre>
 *    @see org.apache.ibatis.type.BaseTypeHandler#setParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
 *    @see org.apache.ibatis.type.IntegerTypeHandler#setNonNullParameter(java.sql.PreparedStatement, int, java.lang.Integer, org.apache.ibatis.type.JdbcType)
 *
 *    SQL编译并设置参数后执行SQL查询
 *    // 这里有日志代理
 *    @see org.apache.ibatis.logging.jdbc.PreparedStatementLogger#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
 *
 *    处理查询结果集
 *    @see org.apache.ibatis.executor.resultset.DefaultResultSetHandler#handleResultSets(java.sql.Statement)
 *    @see org.apache.ibatis.executor.resultset.DefaultResultSetHandler#handleRowValues(org.apache.ibatis.executor.resultset.ResultSetWrapper, org.apache.ibatis.mapping.ResultMap, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.session.RowBounds, org.apache.ibatis.mapping.ResultMapping)
 *    @see org.apache.ibatis.executor.resultset.DefaultResultSetHandler#handleRowValuesForSimpleResultMap(org.apache.ibatis.executor.resultset.ResultSetWrapper, org.apache.ibatis.mapping.ResultMap, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.session.RowBounds, org.apache.ibatis.mapping.ResultMapping)
 *
 * 代理对象执行SQL总结:
 * @see  /explorations/application-framework/mybatis/src/main/resources/images/Mybatis-SQL执行流程.png
 *
 * @auther mac
 * @date 2020-01-11
 */
public class MyBatisExecuteProcess {

    private static final String GLOBAL_CONFIGURATION = "configuration/mybatis-config.xml";

    public static void main(String[] args) {
        InputStream inputStream = null;
        SqlSession openSession = null;
        try {
            inputStream = Resources.getResourceAsStream(GLOBAL_CONFIGURATION);
            // 创建SqlSessionFactory
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            // 获取SqlSession对象
            openSession = sqlSessionFactory.openSession();
            // 获取接口类的实现实例
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            // 执行SQL
            mapper.getEmployeeById(1);
        } catch (IOException e) {
            throw new RuntimeException("can't found "+ GLOBAL_CONFIGURATION);
        } finally {
            Utils.release(inputStream,openSession);
        }
    }
}
