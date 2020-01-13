#一、MyBatis简介

    MyBatis 是支持定制化 SQL、存储过程以及高级映射的持久层框架。
    
    MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。
    
    MyBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO（Plain Old Java 
    Objects，普通的Java对象）映射成数据库中的记录。
    
    https://github.com/mybatis/mybatis-3/
    
#二、MyBatis全局配置文件

    MyBatis 的配置文件包含了影响 MyBatis 行为的设置（settings）和属性（properties）信息。
    文档的顶层结构如下：
     
        configuration 配置
        properties 属性
        settings 设置
        typeAliases 类型命名
        typeHandlers 类型处理器
        objectFactory 对象工厂
        plugins 插件
        environments 环境
            environment 环境变量
                transactionManager 事务管理器
                dataSource 数据源
        databaseIdProvider 数据库厂商标识
        mappers 映射器
        
    properties属性
    
        如果属性在不只一个地方进行了配置，那么 MyBatis 将按照下面的顺序来加载： 
        
        – 在 properties 元素体内指定的属性首先被读取。
        – 然后根据 properties 元素中的 resource 属性读取类路径下
          属性文件或根据 url 属性指定的路径读取属性文件，并覆盖已读取的同名属性。 
        – 最后读取作为方法参数传递的属性，并覆盖已读取的同名属性。
        
    settings设置
    
    
    
    typeAliases别名处理器 
    
        类型别名是为 Java 类型设置一个短的名字，可以方便我们引用某个类。
        类很多的情况下，可以批量设置别名这个包下的每一个类创建一个默认的别名，就是简单类名小写。
        MyBatis已经为许多常见的 Java 类型内建了相应的类型别名。它们都是大小写不敏感的，在起
        别名的时候不要占用已有的别名。 
        
    typeHandlers类型处理器
    
        无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，
        还是从结果集中取出一个值时， 都会用类型处理器将获取的值以合适的方式转换成 Java 类型。 
        
        日期时间处理上，可以使用MyBatis基于JSR310（Date and Time API）编写的各种日期
        时间类型处理器。
        MyBatis3.4以前的版本需要手动注册这些处理器，以后的版本都是自动注册的
        
        <typeHandlers>
             <typeHandler handler= "org.apache.ibatis.type.InstantTypeHandler"/>
             ...
        </typeHandlers>
        
        自定义类型处理器
        
        重写类型处理器或创建类型处理器来处理不支持的或非标准的类型。
        
        步骤：
        1）、实现org.apache.ibatis.type.TypeHandler接口或者继承org.apache.ibatis.type.BaseTypeHandler
        2）、指定其映射某个JDBC类型（可选操作）
        3）、在mybatis全局配置文件中注册
        
    plugins插件
    
        插件是MyBatis提供的一个非常强大的机制，可以通过插件来修改MyBatis的一些核心行为。
        插件通过动态代理机制，可以介入四大对象的任何一个方法的执行。
        Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
        ParameterHandler (getParameterObject, setParameters) 
        ResultSetHandler (handleResultSets, handleOutputParameters) 
        StatementHandler (prepare, parameterize, batch, update, query)
        
    environments环境
    
        MyBatis可以配置多种环境，如开发、测试和生产环境需要有不同的配置。 
        
            每种环境使用一个environment标签进行配置并指定唯一标识符
            可以通过environments标签中的default属性指定一个环境的标识符来快速的切换环境
            
        environment-指定具体环境
        id：指定当前环境的唯一标识
        transactionManager、和dataSource都必须有
        
            transactionManager
            type： JDBC | MANAGED | 自定义
            – JDBC：使用了 JDBC 的提交和回滚设置，依赖于从数据源得到的连接来管理事务范围。JdbcTransactionFactory
            – MANAGED：不提交或回滚一个连接、让容器来管理事务的整个生命周期（比如 JEE 应用服务器的上下文）。 ManagedTransactionFactory
            – 自定义：实现TransactionFactory接口，type=全类名/别名
            
            dataSource
            type： UNPOOLED | POOLED | JNDI | 自定义
            – UNPOOLED：不使用连接池，UnpooledDataSourceFactory
            – POOLED：使用连接池， PooledDataSourceFactory
            – JNDI： 在EJB 或应用服务器这类容器中查找指定的数据源
            – 自定义：实现DataSourceFactory接口，定义数据源的获取方式。
            
    mapper映射  
    
        逐个注册SQL映射文件或者使用批量注册(批量注册要求SQL映射文件名必须和接口名相同并且在同一目录下)  
        
#三、MyBatis映射文件 

    映射文件指导着MyBatis如何进行数据库增删改查
    
         cache –命名空间的二级缓存配置
         cache-ref – 其他命名空间缓存配置的引用。
         resultMap – 自定义结果集映射
         parameterMap – 已废弃！老式风格的参数映射
         sql –抽取可重用语句块。
         insert – 映射插入语句
         update – 映射更新语句
         delete – 映射删除语句
         select – 映射查询语句 
         
    insert、update、delete元素
    
        id 命名空间中的唯一标识
        parameterType 将要传入语句的参数的完全限定类名或别名 这个属性是可选的 
                      mybatis可以根据typeHandler推断出具体类型 默认值unset
        flushCache 默认值true 若设置为true 只要语句被调用 二级缓存和本地缓存将被清空
        
        timeout
        
        statementType
        
        useGenerateKeys
        
        keyProperty
        
        keyColumn
        
        databaseId
        
    主键生成方式  
    
        数据库支持自动生成主键的字段（比如 MySQL 和 SQL Server），则可以设置
        useGeneratedKeys=”true”，然后再把keyProperty 设置到目标属性上。
        
        对于不支持自增型主键的数据库（例如Oracle），则可以使用 selectKey 子元素
        selectKey 元素将会首先运行，id 会被设置，然后插入语句会被调用  
        
    参数（Parameters）传递
    
        单个参数
        – 可以接受基本类型，对象类型，集合类型的值。这种情况MyBatis可直接使用这个参数，不需要经过任何处理。 
       
        多个参数
        – 任意多个参数，都会被MyBatis重新包装成一个Map传入。Map的key是param1，param2，…，值就是参数的值。 
        
        命名参数
        – 为参数使用@Param起一个名字，MyBatis就会将这些参数封装进map中，key就是我们自己指定的名字
        
        POJO
        – 当参数属于业务POJO时，直接传递POJO
       
        Map
        – 可以封装多个参数为map，直接传递
        
       如果参数是Collection（List、Set）类型或者是数组，mybatis将collections、list或者数组封装在map中。
       其key为：
       Collection（collection）,
       List还可以使用这个key(list)
       数组(array)
       
       #{}和${}
       
       区别：
       		#{}:是以预编译的形式，将参数设置到sql语句中；PreparedStatement；防止sql注入
       		${}:取出的值直接拼装在sql语句中；会有安全问题；
       		大多情况下，取参数的值都应该去使用#{}；
       		原生jdbc不支持占位符的地方我们就可以使用${}进行取值
       		比如分表、排序。。。；按照年份分表拆分
       			select * from ${year}_salary where xxx;
       			select * from tbl_employee order by ${f_name} ${order}
       			
       #{}:更丰富的用法：
       
       	    规定参数的一些规则：
       	    javaType、 jdbcType、 mode（存储过程）、 numericScale、
         	resultMap、 typeHandler、 jdbcTypeName、 expression（未来准备支持的功能）；
       
       	    jdbcType通常需要在某种特定的条件下被设置：
       	
       		在数据为null的时候，有些数据库可能不能识别mybatis对null的默认处理。比如Oracle（报错）:
       		
       		    JdbcType OTHER：无效的类型；因为mybatis对所有的null都映射的是原生Jdbc的OTHER类型，oracle不能正确处理;
       		
       		由于全局配置中：jdbcTypeForNull=OTHER；oracle不支持；两种方法可以解决
       		1、#{email,jdbcType=NULL};
       		2、全局配置中设置jdbcTypeForNull=NULL
       		<setting name="jdbcTypeForNull" value="NULL"/> 
    select元素
        定义查询操作。
        Id：唯一标识符。
        – 用来引用这条语句，需要和接口的方法名一致
        parameterType：参数类型。
         – 可以不传，MyBatis会根据TypeHandler自动推断
        resultType：返回值类型。 
        – 别名或者全类名，如果返回的是集合，定义集合中元素的类型。不能和resultMap同时使用   		
        
    自动映射
    
        全局setting设置
        – autoMappingBehavior默认是PARTIAL，开启自动映射的功能。唯一的要求是列名和javaBean属性名一致
        – 如果autoMappingBehavior设置为null则会取消自动映射
       
        – 数据库字段命名规范，POJO属性符合驼峰命名法，如
        A_COLUMNaColumn，可以开启自动驼峰命名规则映射功能，
        mapUnderscoreToCamelCase=true。
        
    resultMap
    
        
        constructor - 类在实例化时, 用来注入结果到构造方法中
        – idArg - ID 参数; 标记结果作为 ID 可以帮助提高整体效能
        – arg - 注入到构造方法的一个普通结果
        id – 一个 ID 结果; 标记结果作为 ID 可以帮助提高整体效能
        result – 注入到字段或 JavaBean 属性的普通结果
        association – 一个复杂的类型关联;许多结果将包成这种类型
        – 嵌入结果映射 – 结果映射自身的关联,或者参考一个
        collection – 复杂类型的集 – 嵌入结果映射 – 结果映射自身的集,或者参考一个
        discriminator – 使用结果值来决定使用哪个结果映射
        case – 基于某些值的结果映射
        嵌入结果映射 – 这种情形结果也映射它本身,因此可以包含很多相同的元
        素,或者它可以参照一个外部的结果映射。
        
            
#四、MyBatis动态SQL

    MyBatis 采用功能强大的基于 OGNL 的表达式来简化操作。
    – if
    – choose (when, otherwise)
    – trim (where, set)
    – foreach
    
    OGNL（ Object Graph Navigation Language ）对象图导航语言，这是一种强大的
    表达式语言，通过它可以非常方便的来操作对象属性。 类似于我们的EL，SpEL等
    访问对象属性： person.name
    调用方法： person.getName()
    调用静态属性/方法： @java.lang.Math@PI
    @java.util.UUID@randomUUID()
    调用构造方法： new com.atguigu.bean.Person(‘admin’).name
    运算符： +,-*,/,%
    逻辑运算符： in,not in,>,>=,<,<=,==,!=
        注意：xml中特殊符号如”,>,<等这些都需要使用转义字符
        
    类型               伪属性            伪属性对应的 Java 方法
    List、Set、Map     size、isEmpty     List/Set/Map.size(),List/Set/Map.isEmpty()
    List、Set          iterator          List.iterator()、Set.iterator()
    Map                keys、values      Map.keySet()、Map.values()
    Iterator           next、hasNext     Iterator.next()、Iterator.hasNext()

#五、MyBatis缓存机制

    MyBatis系统中默认定义了两级缓存。 
    一级缓存和二级缓存。 
    – 1、默认情况下，只有一级缓存（SqlSession级别的缓存，也称为本地缓存）开启。
    – 2、二级缓存需要手动开启和配置，基于namespace级别的缓存。
    – 3、为了提高扩展性。MyBatis定义了缓存接口Cache。可以通过实现Cache接口来自定义二级缓存

#六、MyBatis-Spring整合

#七、MyBatis逆向工程

#八、MyBatis工作原理

    @see org.mac.explorations.framework.mybatis.coreprinciple.MyBatisExecuteProcess

#九、MyBatis插件开发

#十、MyBatis实用场景

    1 PageHelper插件进行分页
    2 批量操作
    3 存储过程
    4 typeHandler处理枚举