#MySQL的常见命令 

	1.查看当前所有的数据库
	show databases;
	2.打开指定的库
	use 库名
	3.查看当前库的所有表
	show tables;
	4.查看其它库的所有表
	show tables from 库名;
	5.创建表
	create table 表名(

		列名 列类型,
		列名 列类型，
		。。。
	);
	6.查看表结构
	desc 表名;


	7.查看服务器的版本
	方式一：登录到mysql服务端
	select version();
	方式二：没有登录到mysql服务端
	mysql --version
	或
	mysql --V


#MySQL的语法规范

	1.不区分大小写,但建议关键字大写，表名、列名小写
	2.每条命令最好用分号结尾
	3.每条命令根据需要，可以进行缩进 或换行
	4.注释
		单行注释：#注释文字
		单行注释：-- 注释文字
		多行注释：/* 注释文字  */
		
#SQL的语言分类
 	DQL（Data Query Language）：数据查询语言
 		select 
 	DML(Data Manipulate Language):数据操作语言
 		insert 、update、delete
 	DDL（Data Define Languge）：数据定义语言
 		create、drop、alter
 	TCL（Transaction Control Language）：事务控制语言
 		commit、rollback
 		
#DQL语言

##1：基础查询
	语法：
	SELECT 查询列表【FROM 表名】;

	特点：
	通过select查询完的结果 ，是一个虚拟的表格，不是真实存在
	要查询的东西 可以是常量值、可以是表达式、可以是字段、可以是函数

##2：条件查询
	条件查询：根据条件过滤原始表的数据，查询到想要的数据
	语法：
	select 
		要查询的字段|表达式|常量值|函数
	from 
		表
	where 
		条件 ;

	分类：
	一、条件表达式
		示例：salary>10000
		条件运算符：
		> < >= <= = != <>
	
	二、逻辑表达式
	示例：salary>10000 && salary<20000
	
	逻辑运算符：

		and（&&）:两个条件如果同时成立，结果为true，否则为false
		or(||)：两个条件只要有一个成立，结果为true，否则为false
		not(!)：如果条件成立，则not后为false，否则为true

	三、模糊查询
	示例：last_name like 'a%'

##3：排序查询	
	
	语法：
	select
		要查询的东西
	from
		表
	where 
		条件
	
	order by 排序的字段|表达式|函数|别名 【asc|desc】

	
##4：常见函数
	一、单行函数
	1、字符函数
		concat拼接
		substr截取子串
		upper转换成大写
		lower转换成小写
		trim去前后指定的空格和字符
		ltrim去左边空格
		rtrim去右边空格
		replace替换
		lpad左填充
		rpad右填充
		instr返回子串第一次出现的索引
		length 获取字节个数
		
	2、数学函数
		round 四舍五入
		rand 随机数
		floor向下取整
		ceil向上取整
		mod取余
		truncate截断
	3、日期函数
		now当前系统日期+时间
		curdate当前系统日期
		curtime当前系统时间
		str_to_date 将字符转换成日期
		date_format将日期转换成字符
		
		    SELECT * FROM employees WHERE hiredate = STR_TO_DATE('4-3 1992','%c-%d %Y');
            
            
            #date_format 将日期转换成字符
            
            SELECT DATE_FORMAT(NOW(),'%y年%m月%d日') AS out_put;
            
            #查询有奖金的员工名和入职日期(xx月/xx日 xx年)
            SELECT last_name,DATE_FORMAT(hiredate,'%m月/%d日 %y年') 入职日期
            FROM employees
            WHERE commission_pct IS NOT NULL;
            
	4、流程控制函数
		if 处理双分支
		case语句 处理多分支
			情况1：处理等值判断
                SELECT salary 原始工资,department_id,
                CASE department_id
                WHEN 30 THEN salary*1.1
                WHEN 40 THEN salary*1.2
                WHEN 50 THEN salary*1.3
                ELSE salary
                END AS 新工资
                FROM employees;
			情况2：处理条件判断
                SELECT salary,
                CASE 
                WHEN salary>20000 THEN 'A'
                WHEN salary>15000 THEN 'B'
                WHEN salary>10000 THEN 'C'
                ELSE 'D'
                END AS 工资级别
                FROM employees;
		
	5、其他函数
		version版本
		database当前库
		user当前连接用户

    二、分组函数
    
    
            sum 求和
            max 最大值
            min 最小值
            avg 平均值
            count 计数
            
                SELECT SUM(DISTINCT salary),SUM(salary) FROM employees;              
                SELECT COUNT(DISTINCT salary),COUNT(salary) FROM employees;
                
                SELECT DATEDIFF(MAX(hiredate),MIN(hiredate)) DIFFRENCE FROM employees;
        
            特点：
            1、以上五个分组函数都忽略null值，除了count(*)
            2、sum和avg一般用于处理数值型
                max、min、count可以处理任何数据类型
            3、都可以搭配distinct使用，用于统计去重后的结果
            4、count的参数可以支持：
                字段、*、常量值，一般放1
        
               建议使用 count(*)
               
               MYISAM存储引擎下，COUNT(*)的效率高
               INNODB存储引擎下，COUNT(*)和COUNT(1)的效率差不多，比COUNT(字段)要高一些


##5：分组查询
	语法：
	select 查询的字段，分组函数
	from 表
	group by 分组的字段
	
	
	特点：
	1、可以按单个字段分组
	2、和分组函数一同查询的字段最好是分组后的字段
	3、分组筛选
			针对的表	位置			关键字
	分组前筛选：	原始表		group by的前面		where
	分组后筛选：	分组后的结果集	group by的后面		having
	
        #案例1：每个工种有奖金的员工的最高工资>12000的工种编号和最高工资
        
        SELECT job_id,MAX(salary)
        FROM employees
        WHERE commission_pct IS NOT NULL
        GROUP BY job_id
        HAVING MAX(salary)>12000;
        
        
        #案例2：领导编号>102的每个领导手下的最低工资大于5000的领导编号和最低工资
        
        SELECT manager_id,MIN(salary)
        FROM employees
        WHERE manager_id>102
        GROUP BY manager_id
        HAVING MIN(salary)>5000;
        
        
        #3.添加排序
        
        #案例：每个工种有奖金的员工的最高工资>6000的工种编号和最高工资,按最高工资升序
        
        SELECT job_id,MAX(salary) m
        FROM employees
        WHERE commission_pct IS NOT NULL
        GROUP BY job_id
        HAVING m>6000
        ORDER BY m ;
        
        #4.按多个字段分组
        
        #案例：查询每个工种每个部门的最低工资,并按最低工资降序
        
        SELECT MIN(salary),job_id,department_id
        FROM employees
        GROUP BY department_id,job_id
        ORDER BY MIN(salary) DESC;
        
	4、可以按多个字段分组，字段之间用逗号隔开
	5、可以支持排序
	6、having后可以支持别名

##6：多表连接查询

	笛卡尔乘积：如果连接条件省略或无效则会出现
	解决办法：添加上连接条件
	
    一、传统模式下的连接 ：等值连接——非等值连接
    
    
        1.等值连接的结果 = 多个表的交集
        2.n表连接，至少需要n-1个连接条件
        3.多个表不分主次，没有顺序要求
        4.一般为表起别名，提高阅读性和性能
        
    二、sql99语法：通过join关键字实现连接
    
        含义：1999年推出的sql语法
        支持：
        等值连接、非等值连接 （内连接）
        外连接
        交叉连接
        
        语法：
        
        select 字段，...
        from 表1
        【inner|left outer|right outer|cross】join 表2 on  连接条件
        【inner|left outer|right outer|cross】join 表3 on  连接条件
        【where 筛选条件】
        【group by 分组字段】
        【having 分组后的筛选条件】
        【order by 排序的字段或表达式】
        
        好处：语句上，连接条件和筛选条件实现了分离，简洁明了！
        
        /*
        语法：
        	select 查询列表
        	from 表1 别名 【连接类型】
        	join 表2 别名 
        	on 连接条件
        	【where 筛选条件】
        	【group by 分组】
        	【having 筛选条件】
        	【order by 排序列表】

        分类：
        内连接（★）：inner
        外连接
        	左外(★):left 【outer】
        	右外(★)：right 【outer】
        	全外：full【outer】
        交叉连接：cross 
        
        */
        
        
        #一）内连接
        /*
        语法：
        
        select 查询列表
        from 表1 别名
        inner join 表2 别名
        on 连接条件;
        
        分类：
        等值
        非等值
        自连接
        
        特点：
        ①添加排序、分组、筛选
        ②inner可以省略
        ③ 筛选条件放在where后面，连接条件放在on后面，提高分离性，便于阅读
        ④inner join连接和sql92语法中的等值连接效果是一样的，都是查询多表的交集
        */
        
        
        #1、等值连接
        #案例1.查询员工名、部门名
        
        SELECT last_name,department_name
        FROM departments d
         JOIN  employees e
        ON e.`department_id` = d.`department_id`;
        
        
        
        #案例2.查询名字中包含e的员工名和工种名（添加筛选）
        SELECT last_name,job_title
        FROM employees e
        INNER JOIN jobs j
        ON e.`job_id`=  j.`job_id`
        WHERE e.`last_name` LIKE '%e%';
        
        
        
        #3. 查询部门个数>3的城市名和部门个数，（添加分组+筛选）
        
        #①查询每个城市的部门个数
        #②在①结果上筛选满足条件的
        SELECT city,COUNT(*) 部门个数
        FROM departments d
        INNER JOIN locations l
        ON d.`location_id`=l.`location_id`
        GROUP BY city
        HAVING COUNT(*)>3;
        
        
        
        
        #案例4.查询哪个部门的员工个数>3的部门名和员工个数，并按个数降序（添加排序）
        
        #①查询每个部门的员工个数
        SELECT COUNT(*),department_name
        FROM employees e
        INNER JOIN departments d
        ON e.`department_id`=d.`department_id`
        GROUP BY department_name
        
        #② 在①结果上筛选员工个数>3的记录，并排序
        
        SELECT COUNT(*) 个数,department_name
        FROM employees e
        INNER JOIN departments d
        ON e.`department_id`=d.`department_id`
        GROUP BY department_name
        HAVING COUNT(*)>3
        ORDER BY COUNT(*) DESC;
        
        #5.查询员工名、部门名、工种名，并按部门名降序（添加三表连接）
        
        SELECT last_name,department_name,job_title
        FROM employees e
        INNER JOIN departments d ON e.`department_id`=d.`department_id`
        INNER JOIN jobs j ON e.`job_id` = j.`job_id`
        
        ORDER BY department_name DESC;
        
        #二）非等值连接
        
        #查询员工的工资级别
        
        SELECT salary,grade_level
        FROM employees e
         JOIN job_grades g
         ON e.`salary` BETWEEN g.`lowest_sal` AND g.`highest_sal`;
         
         
         #查询工资级别的个数>20的个数，并且按工资级别降序
         SELECT COUNT(*),grade_level
        FROM employees e
         JOIN job_grades g
         ON e.`salary` BETWEEN g.`lowest_sal` AND g.`highest_sal`
         GROUP BY grade_level
         HAVING COUNT(*)>20
         ORDER BY grade_level DESC;
         
         
         #三）自连接
         
         #查询员工的名字、上级的名字
         SELECT e.last_name,m.last_name
         FROM employees e
         JOIN employees m
         ON e.`manager_id`= m.`employee_id`;
         
          #查询姓名中包含字符k的员工的名字、上级的名字
         SELECT e.last_name,m.last_name
         FROM employees e
         JOIN employees m
         ON e.`manager_id`= m.`employee_id`
         WHERE e.`last_name` LIKE '%k%';
         
         
         #二、外连接
         
         /*
         应用场景：用于查询一个表中有，另一个表没有的记录
         
         特点：
         1、外连接的查询结果为主表中的所有记录
        	如果从表中有和它匹配的，则显示匹配的值
        	如果从表中没有和它匹配的，则显示null
        	外连接查询结果=内连接结果+主表中有而从表没有的记录
         2、左外连接，left join左边的是主表
            右外连接，right join右边的是主表
         3、左外和右外交换两个表的顺序，可以实现同样的效果 
         4、全外连接=内连接的结果+表1中有但表2没有的+表2中有但表1没有的
         */
         #引入：查询男朋友 不在男神表的的女神名
         
         SELECT * FROM beauty;
         SELECT * FROM boys;
         
         #左外连接
         SELECT b.*,bo.*
         FROM boys bo
         LEFT OUTER JOIN beauty b
         ON b.`boyfriend_id` = bo.`id`
         WHERE b.`id` IS NULL;
         
         
         #案例1：查询哪个部门没有员工
         #左外
         SELECT d.*,e.employee_id
         FROM departments d
         LEFT OUTER JOIN employees e
         ON d.`department_id` = e.`department_id`
         WHERE e.`employee_id` IS NULL;
         
         
         #右外
         
          SELECT d.*,e.employee_id
         FROM employees e
         RIGHT OUTER JOIN departments d
         ON d.`department_id` = e.`department_id`
         WHERE e.`employee_id` IS NULL;
         
         
         #全外(MySQL不支持)
         
         USE girls;
         SELECT b.*,bo.*
         FROM beauty b
         FULL OUTER JOIN boys bo
         ON b.`boyfriend_id` = bo.id;
         
        
         #交叉连接
         
         SELECT b.*,bo.*
         FROM beauty b
         CROSS JOIN boys bo;
   
##7：子查询

    含义：
    
        一条查询语句中又嵌套了另一条完整的select语句，其中被嵌套的select语句，称为子查询或内查询
        在外面的查询语句，称为主查询或外查询
    
    特点：
    
        1、子查询都放在小括号内
        2、子查询可以放在from后面、select后面、where后面、having后面，但一般放在条件的右侧
        3、子查询优先于主查询执行，主查询使用了子查询的执行结果
        4、子查询根据查询结果的行数不同分为以下两类：
        单行子查询
            结果集只有一行
            一般搭配单行操作符使用：> < = <> >= <= 
            非法使用子查询的情况：
            a、子查询的结果为一组值
            b、子查询的结果为空
            
        多行子查询
            结果集有多行
            一般搭配多行操作符使用：any、all、in、not in
            in： 属于子查询结果中的任意一个就行
            any和all往往可以用其他查询代替
            
        #一、where或having后面
        /*
        1、标量子查询（单行子查询）
        2、列子查询（多行子查询）
        
        3、行子查询（多列多行）
        
        特点：
        ①子查询放在小括号内
        ②子查询一般放在条件的右侧
        ③标量子查询，一般搭配着单行操作符使用
        > < >= <= = <>
        
        列子查询，一般搭配着多行操作符使用
        in、any/some、all
        
        ④子查询的执行优先于主查询执行，主查询的条件用到了子查询的结果
        
        */
        #1.标量子查询★
        
        #案例1：谁的工资比 Abel 高?
        
        #①查询Abel的工资
        SELECT salary
        FROM employees
        WHERE last_name = 'Abel'
        
        #②查询员工的信息，满足 salary>①结果
        SELECT *
        FROM employees
        WHERE salary>(
        
        	SELECT salary
        	FROM employees
        	WHERE last_name = 'Abel'
        
        );
        
        #案例2：返回job_id与141号员工相同，salary比143号员工多的员工 姓名，job_id 和工资
        
        #①查询141号员工的job_id
        SELECT job_id
        FROM employees
        WHERE employee_id = 141
        
        #②查询143号员工的salary
        SELECT salary
        FROM employees
        WHERE employee_id = 143
        
        #③查询员工的姓名，job_id 和工资，要求job_id=①并且salary>②
        
        SELECT last_name,job_id,salary
        FROM employees
        WHERE job_id = (
        	SELECT job_id
        	FROM employees
        	WHERE employee_id = 141
        ) AND salary>(
        	SELECT salary
        	FROM employees
        	WHERE employee_id = 143
        
        );
        
        
        #案例3：返回公司工资最少的员工的last_name,job_id和salary
        
        #①查询公司的 最低工资
        SELECT MIN(salary)
        FROM employees
        
        #②查询last_name,job_id和salary，要求salary=①
        SELECT last_name,job_id,salary
        FROM employees
        WHERE salary=(
        	SELECT MIN(salary)
        	FROM employees
        );
        
        
        #案例4：查询最低工资大于50号部门最低工资的部门id和其最低工资
        
        #①查询50号部门的最低工资
        SELECT  MIN(salary)
        FROM employees
        WHERE department_id = 50
        
        #②查询每个部门的最低工资
        
        SELECT MIN(salary),department_id
        FROM employees
        GROUP BY department_id
        
        #③ 在②基础上筛选，满足min(salary)>①
        SELECT MIN(salary),department_id
        FROM employees
        GROUP BY department_id
        HAVING MIN(salary)>(
        	SELECT  MIN(salary)
        	FROM employees
        	WHERE department_id = 50
        
        
        );
        
        #非法使用标量子查询
        
        SELECT MIN(salary),department_id
        FROM employees
        GROUP BY department_id
        HAVING MIN(salary)>(
        	SELECT  salary
        	FROM employees
        	WHERE department_id = 250
        
        
        );
        
        
        
        #2.列子查询（多行子查询）★
        #案例1：返回location_id是1400或1700的部门中的所有员工姓名
        
        #①查询location_id是1400或1700的部门编号
        SELECT DISTINCT department_id
        FROM departments
        WHERE location_id IN(1400,1700)
        
        #②查询员工姓名，要求部门号是①列表中的某一个
        
        SELECT last_name
        FROM employees
        WHERE department_id  <>ALL(
        	SELECT DISTINCT department_id
        	FROM departments
        	WHERE location_id IN(1400,1700)
        
        
        );
        
        
        #案例2：返回其它工种中比job_id为‘IT_PROG’工种任一工资低的员工的员工号、姓名、job_id 以及salary
        
        #①查询job_id为‘IT_PROG’部门任一工资
        
        SELECT DISTINCT salary
        FROM employees
        WHERE job_id = 'IT_PROG'
        
        #②查询员工号、姓名、job_id 以及salary，salary<(①)的任意一个
        SELECT last_name,employee_id,job_id,salary
        FROM employees
        WHERE salary<ANY(
        	SELECT DISTINCT salary
        	FROM employees
        	WHERE job_id = 'IT_PROG'
        
        ) AND job_id<>'IT_PROG';
        
        #或
        SELECT last_name,employee_id,job_id,salary
        FROM employees
        WHERE salary<(
        	SELECT MAX(salary)
        	FROM employees
        	WHERE job_id = 'IT_PROG'
        
        ) AND job_id<>'IT_PROG';
        
        
        #案例3：返回其它部门中比job_id为‘IT_PROG’部门所有工资都低的员工   的员工号、姓名、job_id 以及salary
        
        SELECT last_name,employee_id,job_id,salary
        FROM employees
        WHERE salary<ALL(
        	SELECT DISTINCT salary
        	FROM employees
        	WHERE job_id = 'IT_PROG'
        
        ) AND job_id<>'IT_PROG';
        
        #或
        
        SELECT last_name,employee_id,job_id,salary
        FROM employees
        WHERE salary<(
        	SELECT MIN( salary)
        	FROM employees
        	WHERE job_id = 'IT_PROG'
        
        ) AND job_id<>'IT_PROG';
        
        
        
        #3、行子查询（结果集一行多列或多行多列）
        
        #案例：查询员工编号最小并且工资最高的员工信息
        
        
        
        SELECT * 
        FROM employees
        WHERE (employee_id,salary)=(
        	SELECT MIN(employee_id),MAX(salary)
        	FROM employees
        );
        
        #①查询最小的员工编号
        SELECT MIN(employee_id)
        FROM employees
        
        
        #②查询最高工资
        SELECT MAX(salary)
        FROM employees
        
        
        #③查询员工信息
        SELECT *
        FROM employees
        WHERE employee_id=(
        	SELECT MIN(employee_id)
        	FROM employees
        
        
        )AND salary=(
        	SELECT MAX(salary)
        	FROM employees
        
        );
        
        
        #二、select后面
        /*
        仅仅支持标量子查询
        */
        
        #案例：查询每个部门的员工个数
        
        
        SELECT d.*,(
        
        	SELECT COUNT(*)
        	FROM employees e
        	WHERE e.department_id = d.`department_id`
         ) 个数
         FROM departments d;
         
         
         #案例2：查询员工号=102的部门名
         
        SELECT (
        	SELECT department_name,e.department_id
        	FROM departments d
        	INNER JOIN employees e
        	ON d.department_id=e.department_id
        	WHERE e.employee_id=102
        	
        ) 部门名;
        
        
        
        #三、from后面
        /*
        将子查询结果充当一张表，要求必须起别名
        */
        
        #案例：查询每个部门的平均工资的工资等级
        #①查询每个部门的平均工资
        SELECT AVG(salary),department_id
        FROM employees
        GROUP BY department_id
        
        
        SELECT * FROM job_grades;
        
        
        #②连接①的结果集和job_grades表，筛选条件平均工资 between lowest_sal and highest_sal
        
        SELECT  ag_dep.*,g.`grade_level`
        FROM (
        	SELECT AVG(salary) ag,department_id
        	FROM employees
        	GROUP BY department_id
        ) ag_dep
        INNER JOIN job_grades g
        ON ag_dep.ag BETWEEN lowest_sal AND highest_sal;
        
        
        
        #四、exists后面（相关子查询）
        
        /*
        语法：
        exists(完整的查询语句)
        结果：
        1或0
        */
        
        SELECT EXISTS(SELECT employee_id FROM employees WHERE salary=300000);
        
        #案例1：查询有员工的部门名
        
        #in
        SELECT department_name
        FROM departments d
        WHERE d.`department_id` IN(
        	SELECT department_id
        	FROM employees
        
        )
        
        #exists
        
        SELECT department_name
        FROM departments d
        WHERE EXISTS(
        	SELECT *
        	FROM employees e
        	WHERE d.`department_id`=e.`department_id`
        
        
        );
        
        
        #案例2：查询没有女朋友的男神信息
        
        #in
        
        SELECT bo.*
        FROM boys bo
        WHERE bo.id NOT IN(
        	SELECT boyfriend_id
        	FROM beauty
        )
        
        #exists
        SELECT bo.*
        FROM boys bo
        WHERE NOT EXISTS(
        	SELECT boyfriend_id
        	FROM beauty b
        	WHERE bo.`id`=b.`boyfriend_id`
        );
	
##8：分页查询

    应用场景：
    
        实际的web项目中需要根据用户的需求提交对应的分页查询的sql语句
    
    语法：
    
        select 字段|表达式,...
        from 表
        【where 条件】
        【group by 分组字段】
        【having 条件】
        【order by 排序的字段】
        limit 【起始的条目索引，】条目数;
    
    特点：
    
        1.起始条目索引从0开始
        
        2.limit子句放在查询语句的最后
        
        3.公式：select * from  表 limit （page-1）*sizePerPage,sizePerPage
        假如:
        每页显示条目数sizePerPage
        要显示的页数 page

#9：联合查询
    
    引入：
        union 联合、合并
    
    语法：
    
        select 字段|常量|表达式|函数 【from 表】 【where 条件】 union 【all】
        select 字段|常量|表达式|函数 【from 表】 【where 条件】 union 【all】
        select 字段|常量|表达式|函数 【from 表】 【where 条件】 union  【all】
        .....
        select 字段|常量|表达式|函数 【from 表】 【where 条件】
    
    特点：
    
        1、多条查询语句的查询的列数必须是一致的
        2、多条查询语句的查询的列的类型几乎相同
        3、union代表去重，union all代表不去重
#DDL
    /*
    
    数据定义语言
    
    库和表的管理
    
    一、库的管理
    创建、修改、删除
    二、表的管理
    创建、修改、删除
    
    创建： create
    修改： alter
    删除： drop
    
    */

##一、库的管理
###1、库的创建
    /*
    语法：
    create database  [if not exists]库名;
    */


    案例：创建库Books

    CREATE DATABASE IF NOT EXISTS books ;


###2、库的修改

    RENAME DATABASE books TO 新库名;

    更改库的字符集

    ALTER DATABASE books CHARACTER SET gbk;


###3、库的删除

    DROP DATABASE IF EXISTS books;

##二、表的管理
###1.表的创建 ★

    /*
    语法：
    create table 表名(
        列名 列的类型【(长度) 约束】,
        列名 列的类型【(长度) 约束】,
        列名 列的类型【(长度) 约束】,
        ...
        列名 列的类型【(长度) 约束】
    
    
    )
    
    
    */
    案例：创建表Book
    
    CREATE TABLE book(
        id INT,#编号
        bName VARCHAR(20),#图书名
        price DOUBLE,#价格
        authorId  INT,#作者编号
        publishDate DATETIME#出版日期
    
    
    
    );
    
    
    DESC book;
    
    案例：创建表author
    CREATE TABLE IF NOT EXISTS author(
        id INT,
        au_name VARCHAR(20),
        nation VARCHAR(10)
    
    )
    DESC author;


###2.表的修改

    /*
    语法
    alter table 表名 add|drop|modify|change column 列名 【列类型 约束】;
    
    */

    #①修改列名
    
    ALTER TABLE book CHANGE COLUMN publishdate pubDate DATETIME;
    
    
    #②修改列的类型或约束
    ALTER TABLE book MODIFY COLUMN pubdate TIMESTAMP;
    
    #③添加新列
    ALTER TABLE author ADD COLUMN annual DOUBLE; 
    
    #④删除列
    
    ALTER TABLE book_author DROP COLUMN  annual;
    #⑤修改表名
    
    ALTER TABLE author RENAME TO book_author;
    
    DESC book;
    



###3.表的删除

    DROP TABLE IF EXISTS book_author;
    
    SHOW TABLES;


    通用的写法：
    
    DROP DATABASE IF EXISTS 旧库名;
    CREATE DATABASE 新库名;
    
    
    DROP TABLE IF EXISTS 旧表名;
    CREATE TABLE  表名();
    


###4.表的复制

    INSERT INTO author VALUES
    (1,'村上春树','日本'),
    (2,'莫言','中国'),
    (3,'冯唐','中国'),
    (4,'金庸','中国');
    
    SELECT * FROM Author;
    SELECT * FROM copy2;
    
    仅仅复制表的结构
    
    CREATE TABLE copy LIKE author;
    
    复制表的结构+数据
    CREATE TABLE copy2 
    SELECT * FROM author;

    只复制部分数据
    CREATE TABLE copy3
    SELECT id,au_name
    FROM author 
    WHERE nation='中国';


    仅仅复制某些字段
    
    CREATE TABLE copy4 
    SELECT id,au_name
    FROM author
    WHERE 0;
    
#DML语言
    /*
    数据操作语言：
    插入：insert
    修改：update
    删除：delete
    
    */

##一、插入语句
    方式一：经典的插入
    /*
    语法：
    insert into 表名(列名,...) values(值1,...);
    
    */
    SELECT * FROM beauty;
    1.插入的值的类型要与列的类型一致或兼容
    INSERT INTO beauty(id,NAME,sex,borndate,phone,photo,boyfriend_id)
    VALUES(13,'唐艺昕','女','1990-4-23','1898888888',NULL,2);
    
    2.不可以为null的列必须插入值。可以为null的列如何插入值？
    方式一：
    INSERT INTO beauty(id,NAME,sex,borndate,phone,photo,boyfriend_id)
    VALUES(13,'唐艺昕','女','1990-4-23','1898888888',NULL,2);
    
    方式二：
    
    INSERT INTO beauty(id,NAME,sex,phone)
    VALUES(15,'娜扎','女','1388888888');


    3.列的顺序是否可以调换
    INSERT INTO beauty(NAME,sex,id,phone)
    VALUES('蒋欣','女',16,'110');


    4.列数和值的个数必须一致
    
    INSERT INTO beauty(NAME,sex,id,phone)
    VALUES('关晓彤','女',17,'110');

    5.可以省略列名，默认所有列，而且列的顺序和表中列的顺序一致
    
    INSERT INTO beauty
    VALUES(18,'张飞','男',NULL,'119',NULL,NULL);

    方式二：
    /*
    
    语法：
    insert into 表名
    set 列名=值,列名=值,...
    */
    
    
    INSERT INTO beauty
    SET id=19,NAME='刘涛',phone='999';
    

    1、方式一支持插入多行,方式二不支持
    
    INSERT INTO beauty
    VALUES(23,'唐艺昕1','女','1990-4-23','1898888888',NULL,2)
    ,(24,'唐艺昕2','女','1990-4-23','1898888888',NULL,2)
    ,(25,'唐艺昕3','女','1990-4-23','1898888888',NULL,2);
    
    2、方式一支持子查询，方式二不支持
    
    INSERT INTO beauty(id,NAME,phone)
    SELECT 26,'宋茜','11809866';
    
    INSERT INTO beauty(id,NAME,phone)
    SELECT id,boyname,'1234567'
    FROM boys WHERE id<3;

##二、修改语句

    /*
    
    1.修改单表的记录★
    
    语法：
    update 表名
    set 列=新值,列=新值,...
    where 筛选条件;
    
    2.修改多表的记录【补充】
    
    语法：
    sql92语法：
    update 表1 别名,表2 别名
    set 列=值,...
    where 连接条件
    and 筛选条件;
    
    sql99语法：
    update 表1 别名
    inner|left|right join 表2 别名
    on 连接条件
    set 列=值,...
    where 筛选条件;
    
    
    */


    1.修改单表的记录
    案例1：修改beauty表中姓唐的女神的电话为13899888899
    
    UPDATE beauty SET phone = '13899888899'
    WHERE NAME LIKE '唐%';
    
    案例2：修改boys表中id好为2的名称为张飞，魅力值 10
    UPDATE boys SET boyname='张飞',usercp=10
    WHERE id=2;
    
    
    
    2.修改多表的记录
    
    案例 1：修改张无忌的女朋友的手机号为114
    
    UPDATE boys bo
    INNER JOIN beauty b ON bo.`id`=b.`boyfriend_id`
    SET b.`phone`='119',bo.`userCP`=1000
    WHERE bo.`boyName`='张无忌';
    
    
    
    案例2：修改没有男朋友的女神的男朋友编号都为2号
    
    UPDATE boys bo
    RIGHT JOIN beauty b ON bo.`id`=b.`boyfriend_id`
    SET b.`boyfriend_id`=2
    WHERE bo.`id` IS NULL;
    
    SELECT * FROM boys;


##三、删除语句
    /*
    
    方式一：delete
    语法：
    
    1、单表的删除【★】
    delete from 表名 where 筛选条件
    
    2、多表的删除【补充】
    
    sql92语法：
    delete 表1的别名,表2的别名
    from 表1 别名,表2 别名
    where 连接条件
    and 筛选条件;
    
    sql99语法：
    
    delete 表1的别名,表2的别名
    from 表1 别名
    inner|left|right join 表2 别名 on 连接条件
    where 筛选条件;
    
    
    
    方式二：truncate
    语法：truncate table 表名;
    
    */

    方式一：delete
    1.单表的删除
    案例：删除手机号以9结尾的女神信息
    
    DELETE FROM beauty WHERE phone LIKE '%9';
    SELECT * FROM beauty;
    
    
    2.多表的删除
    
    案例：删除张无忌的女朋友的信息
    
    DELETE b
    FROM beauty b
    INNER JOIN boys bo ON b.`boyfriend_id` = bo.`id`
    WHERE bo.`boyName`='张无忌';
    
    
    案例：删除黄晓明的信息以及他女朋友的信息
    DELETE b,bo
    FROM beauty b
    INNER JOIN boys bo ON b.`boyfriend_id`=bo.`id`
    WHERE bo.`boyName`='黄晓明';
    
    
    
    方式二：truncate语句
    
    案例：将魅力值>100的男神信息删除
    TRUNCATE TABLE boys ;
    
    
    
    delete VS truncate
    
    /*
    
    1.delete 可以加where 条件，truncate不能加
    
    2.truncate删除，效率高一丢丢
    3.假如要删除的表中有自增长列，
    如果用delete删除后，再插入数据，自增长列的值从断点开始，
    而truncate删除后，再插入数据，自增长列的值从1开始。
    4.truncate删除没有返回值，delete删除有返回值
    
    5.truncate删除不能回滚，delete删除可以回滚.
    
    */
    
    SELECT * FROM boys;
    
    DELETE FROM boys;
    TRUNCATE TABLE boys;
    INSERT INTO boys (boyname,usercp)
    VALUES('张飞',100),('刘备',100),('关云长',100);
    
#常见的数据类型
    /*
    数值型：
        整型
        小数：
            定点数
            浮点数
    字符型：
        较短的文本：char、varchar
        较长的文本：text、blob（较长的二进制数据）
    
    日期型：
        
    
    
    */
    
    #一、整型
    /*
    分类：
    tinyint、smallint、mediumint、int/integer、bigint
    1	        2		3	           4		8
    
    特点：
    ① 如果不设置无符号还是有符号，默认是有符号，如果想设置无符号，需要添加unsigned关键字
    ② 如果插入的数值超出了整型的范围,会报out of range异常，并且插入临界值
    ③ 如果不设置长度，会有默认的长度
    长度代表了显示的最大宽度，如果不够会用0在左边填充，但必须搭配zerofill使用！
    
    */
    
    #1.如何设置无符号和有符号
    
    DROP TABLE IF EXISTS tab_int;
    CREATE TABLE tab_int(
        t1 INT(7) ZEROFILL,
        t2 INT(7) ZEROFILL 
    
    );
    
    DESC tab_int;
    
    
    INSERT INTO tab_int VALUES(-123456);
    INSERT INTO tab_int VALUES(-123456,-123456);
    INSERT INTO tab_int VALUES(2147483648,4294967296);
    
    INSERT INTO tab_int VALUES(123,123);
    
    
    SELECT * FROM tab_int;
    
    
    #二、小数
    /*
    分类：
    1.浮点型
    float(M,D)
    double(M,D)
    2.定点型
    dec(M，D)
    decimal(M,D)
    
    特点：
    
    ①
    M：整数部位+小数部位
    D：小数部位
    如果超过范围，则插入临界值
    
    ②
    M和D都可以省略
    如果是decimal，则M默认为10，D默认为0
    如果是float和double，则会根据插入的数值的精度来决定精度
    
    ③定点型的精确度较高，如果要求插入数值的精度较高如货币运算等则考虑使用
    
    
    */
    #测试M和D
    
    DROP TABLE tab_float;
    CREATE TABLE tab_float(
        f1 FLOAT,
        f2 DOUBLE,
        f3 DECIMAL
    );
    SELECT * FROM tab_float;
    DESC tab_float;
    
    INSERT INTO tab_float VALUES(123.4523,123.4523,123.4523);
    INSERT INTO tab_float VALUES(123.456,123.456,123.456);
    INSERT INTO tab_float VALUES(123.4,123.4,123.4);
    INSERT INTO tab_float VALUES(1523.4,1523.4,1523.4);
    
    
    
    #原则：
    /*
    所选择的类型越简单越好，能保存数值的类型越小越好
    
    */
    
    #三、字符型
    /*
    较短的文本：
    
    char
    varchar
    
    其他：
    
    binary和varbinary用于保存较短的二进制
    enum用于保存枚举
    set用于保存集合
    
    
    较长的文本：
    text
    blob(较大的二进制)
    
    特点：
    写法		             M的意思					            特点			        空间的耗费	       效率
    char char(M)		最大的字符数，可以省略，默认为1		固定长度的字符		比较耗费	           高
    
    varchar varchar(M)	最大的字符数，不可以省略		        可变长度的字符		比较节省	           低
    */
    
    
    
    CREATE TABLE tab_char(
        c1 ENUM('a','b','c')
    );
    
    
    INSERT INTO tab_char VALUES('a');
    INSERT INTO tab_char VALUES('b');
    INSERT INTO tab_char VALUES('c');
    INSERT INTO tab_char VALUES('m');
    INSERT INTO tab_char VALUES('A');
    
    SELECT * FROM tab_set;
    
    
    
    CREATE TABLE tab_set(
    
        s1 SET('a','b','c','d')
    
    
    
    );
    INSERT INTO tab_set VALUES('a');
    INSERT INTO tab_set VALUES('A,B');
    INSERT INTO tab_set VALUES('a,c,d');
    
    
    #四、日期型
    
    /*
    
    分类：
    date只保存日期
    time 只保存时间
    year只保存年
    
    datetime保存日期+时间
    timestamp保存日期+时间
    
    
    特点：
    
               字节		范围		                 时区等的影响
               
    datetime	8		1000——9999	                  不受
    timestamp	4	    1970-2038	                  受
    
    */
    
    
    CREATE TABLE tab_date(
        t1 DATETIME,
        t2 TIMESTAMP
    
    );
    
    
    
    INSERT INTO tab_date VALUES(NOW(),NOW());
    
    SELECT * FROM tab_date;
    
    
    SHOW VARIABLES LIKE 'time_zone';
    
    SET time_zone='+9:00';
    
 #常见约束
 
     /*
     
     
     含义：一种限制，用于限制表中的数据，为了保证表中的数据的准确和可靠性
     
     
     分类：六大约束
        NOT NULL：非空，用于保证该字段的值不能为空
        比如姓名、学号等
        DEFAULT:默认，用于保证该字段有默认值
        比如性别
        PRIMARY KEY:主键，用于保证该字段的值具有唯一性，并且非空
        比如学号、员工编号等
        UNIQUE:唯一，用于保证该字段的值具有唯一性，可以为空
        比如座位号
        CHECK:检查约束【mysql中不支持】
        比如年龄、性别
        FOREIGN KEY:外键，用于限制两个表的关系，用于保证该字段的值必须来自于主表的关联列的值
            在从表添加外键约束，用于引用主表中某列的值
        比如学生表的专业编号，员工表的部门编号，员工表的工种编号
  
     约束的添加分类：
        列级约束：
            六大约束语法上都支持，但外键约束没有效果
 		
 	    表级约束：
 		
 		除了非空、默认，其他的都支持
 			
     主键和唯一的对比：
     
        保证唯一性  是否允许为空    一个表中可以有多少个   是否允许组合
        主键	√		×		      至多有1个             √，但不推荐
        唯一	√		√		      可以有多个            √，但不推荐
        
     外键：
        1、要求在从表设置外键关系
        2、从表的外键列的类型和主表的关联列的类型要求一致或兼容，名称无要求
        3、主表的关联列必须是一个key（一般是主键或唯一）
        4、插入数据时，先插入主表，再插入从表
        删除数据时，先删除从表，再删除主表
     */
 
     CREATE TABLE 表名(
        字段名 字段类型 列级约束,
        字段名 字段类型,
        表级约束
     
     )
     CREATE DATABASE students;
     #一、创建表时添加约束
     
     #1.添加列级约束
     /*
     语法：
     
     直接在字段名和类型后面追加 约束类型即可。
     
     只支持：默认、非空、主键、唯一
     
     
     
     */
     
     USE students;
     DROP TABLE stuinfo;
     CREATE TABLE stuinfo(
        id INT PRIMARY KEY,#主键
        stuName VARCHAR(20) NOT NULL UNIQUE,#非空
        gender CHAR(1) CHECK(gender='男' OR gender ='女'),#检查
        seat INT UNIQUE,#唯一
        age INT DEFAULT  18,#默认约束
        majorId INT REFERENCES major(id)#外键
     
     );
     
     
     CREATE TABLE major(
        id INT PRIMARY KEY,
        majorName VARCHAR(20)
     );
     
     #查看stuinfo中的所有索引，包括主键、外键、唯一
     SHOW INDEX FROM stuinfo;
     
     
     #2.添加表级约束
     /*
     
     语法：在各个字段的最下面
      【constraint 约束名】 约束类型(字段名) 
     */
     
     DROP TABLE IF EXISTS stuinfo;
     CREATE TABLE stuinfo(
        id INT,
        stuname VARCHAR(20),
        gender CHAR(1),
        seat INT,
        age INT,
        majorid INT,
        
        CONSTRAINT pk PRIMARY KEY(id),#主键
        CONSTRAINT uq UNIQUE(seat),#唯一键
        CONSTRAINT ck CHECK(gender ='男' OR gender  = '女'),#检查
        CONSTRAINT fk_stuinfo_major FOREIGN KEY(majorid) REFERENCES major(id)#外键
        
     );

     SHOW INDEX FROM stuinfo;

     #通用的写法：★
     
     CREATE TABLE IF NOT EXISTS stuinfo(
        id INT PRIMARY KEY,
        stuname VARCHAR(20),
        sex CHAR(1),
        age INT DEFAULT 18,
        seat INT UNIQUE,
        majorid INT,
        CONSTRAINT fk_stuinfo_major FOREIGN KEY(majorid) REFERENCES major(id)
     
     );

     #二、修改表时添加约束
     
     /*
     1、添加列级约束
     alter table 表名 modify column 字段名 字段类型 新约束;
     
     2、添加表级约束
     alter table 表名 add 【constraint 约束名】 约束类型(字段名) 【外键的引用】;
     
     
     */
     DROP TABLE IF EXISTS stuinfo;
     CREATE TABLE stuinfo(
        id INT,
        stuname VARCHAR(20),
        gender CHAR(1),
        seat INT,
        age INT,
        majorid INT
     )
     DESC stuinfo;
     #1.添加非空约束
     ALTER TABLE stuinfo MODIFY COLUMN stuname VARCHAR(20)  NOT NULL;
     #2.添加默认约束
     ALTER TABLE stuinfo MODIFY COLUMN age INT DEFAULT 18;
     #3.添加主键
     #①列级约束
     ALTER TABLE stuinfo MODIFY COLUMN id INT PRIMARY KEY;
     #②表级约束
     ALTER TABLE stuinfo ADD PRIMARY KEY(id);
     
     #4.添加唯一
     
     #①列级约束
     ALTER TABLE stuinfo MODIFY COLUMN seat INT UNIQUE;
     #②表级约束
     ALTER TABLE stuinfo ADD UNIQUE(seat);
     
     
     #5.添加外键
     ALTER TABLE stuinfo ADD CONSTRAINT fk_stuinfo_major FOREIGN KEY(majorid) REFERENCES major(id); 
     
     #三、修改表时删除约束
     
     #1.删除非空约束
     ALTER TABLE stuinfo MODIFY COLUMN stuname VARCHAR(20) NULL;
     
     #2.删除默认约束
     ALTER TABLE stuinfo MODIFY COLUMN age INT ;
     
     #3.删除主键
     ALTER TABLE stuinfo DROP PRIMARY KEY;
     
     #4.删除唯一
     ALTER TABLE stuinfo DROP INDEX seat;
     
     #5.删除外键
     ALTER TABLE stuinfo DROP FOREIGN KEY fk_stuinfo_major;
     
     SHOW INDEX FROM stuinfo;
#TCL
    /*
    Transaction Control Language 事务控制语言
    
    事务：
    一个或一组sql语句组成一个执行单元，这个执行单元要么全部执行，要么全部不执行。
    
    
    事务的特性：
    ACID
    原子性：一个事务不可再分割，要么都执行要么都不执行
    一致性：一个事务执行会使数据从一个一致状态切换到另外一个一致状态
    隔离性：一个事务的执行不受其他事务的干扰
    持久性：一个事务一旦提交，则会永久的改变数据库的数据.
    
    
    
    事务的创建
    隐式事务：事务没有明显的开启和结束的标记
    比如insert、update、delete语句
    
    delete from 表 where id =1;
    
    显式事务：事务具有明显的开启和结束的标记
    前提：必须先设置自动提交功能为禁用
    
    set autocommit=0;
    
    步骤1：开启事务
    set autocommit=0;
    start transaction;可选的
    步骤2：编写事务中的sql语句(select insert update delete)
    语句1;
    语句2;
    ...
    
    步骤3：结束事务
    commit;提交事务
    rollback;回滚事务
    
    savepoint 节点名;设置保存点
    
    
    
    事务的隔离级别：
                     脏读		不可重复读	幻读
    read uncommitted：√		       √		 √
    read committed：  ×		       √		 √
    repeatable read： ×		       ×		 √
    serializable	  ×            ×         ×
    
    
    mysql中默认 第三个隔离级别 repeatable read
    oracle中默认第二个隔离级别 read committed
    查看隔离级别
    select @@tx_isolation;
    设置隔离级别
    set session|global transaction isolation level 隔离级别;
    
    
    
    
    开启事务的语句;
    update 表 set 张三丰的余额=500 where name='张三丰'
    
    update 表 set 郭襄的余额=1500 where name='郭襄' 
    结束事务的语句;
    
    
    
    */
    
    SHOW VARIABLES LIKE 'autocommit';
    SHOW ENGINES;
    
    #1.演示事务的使用步骤
    
    #开启事务
    SET autocommit=0;
    START TRANSACTION;
    #编写一组事务的语句
    UPDATE account SET balance = 1000 WHERE username='张无忌';
    UPDATE account SET balance = 1000 WHERE username='赵敏';
    
    #结束事务
    ROLLBACK;
    #commit;
    
    SELECT * FROM account;
    
    
    #2.事务对于delete和truncate的处理的区别
    
    SET autocommit=0;
    START TRANSACTION;
    
    DELETE FROM account;
    ROLLBACK;
    
    
    
    #3.savepoint 的使用
    SET autocommit=0;
    START TRANSACTION;
    DELETE FROM account WHERE id=25;
    SAVEPOINT a;#设置保存点
    DELETE FROM account WHERE id=28;
    ROLLBACK TO a;#回滚到保存点
    
    
    SELECT * FROM account;
    
#视图
    /*
    含义：虚拟表，和普通表一样使用
    mysql5.1版本出现的新特性，是通过表动态生成的数据
    
    比如：舞蹈班和普通班级的对比
        创建语法的关键字	是否实际占用物理空间	使用
    
    视图	create view		只是保存了sql逻辑	增删改查，只是一般不能增删改
    
    表	create table		保存了数据		增删改查
    
    
    */
    
    #案例：查询姓张的学生名和专业名
    SELECT stuname,majorname
    FROM stuinfo s
    INNER JOIN major m ON s.`majorid`= m.`id`
    WHERE s.`stuname` LIKE '张%';
    
    CREATE VIEW v1
    AS
    SELECT stuname,majorname
    FROM stuinfo s
    INNER JOIN major m ON s.`majorid`= m.`id`;
    
    SELECT * FROM v1 WHERE stuname LIKE '张%';
    
    
    #一、创建视图
    /*
    语法：
    create view 视图名
    as
    查询语句;
    
    */
    USE myemployees;
    
    #1.查询姓名中包含a字符的员工名、部门名和工种信息
    #①创建
    CREATE VIEW myv1
    AS
    
    SELECT last_name,department_name,job_title
    FROM employees e
    JOIN departments d ON e.department_id  = d.department_id
    JOIN jobs j ON j.job_id  = e.job_id;
    
    
    #②使用
    SELECT * FROM myv1 WHERE last_name LIKE '%a%';
    
    
    
    
    
    
    #2.查询各部门的平均工资级别
    
    #①创建视图查看每个部门的平均工资
    CREATE VIEW myv2
    AS
    SELECT AVG(salary) ag,department_id
    FROM employees
    GROUP BY department_id;
    
    #②使用
    SELECT myv2.`ag`,g.grade_level
    FROM myv2
    JOIN job_grades g
    ON myv2.`ag` BETWEEN g.`lowest_sal` AND g.`highest_sal`;
    
    
    
    #3.查询平均工资最低的部门信息
    
    SELECT * FROM myv2 ORDER BY ag LIMIT 1;
    
    #4.查询平均工资最低的部门名和工资
    
    CREATE VIEW myv3
    AS
    SELECT * FROM myv2 ORDER BY ag LIMIT 1;
    
    
    SELECT d.*,m.ag
    FROM myv3 m
    JOIN departments d
    ON m.`department_id`=d.`department_id`;
    
    
    
    
    #二、视图的修改
    
    #方式一：
    /*
    create or replace view  视图名
    as
    查询语句;
    
    */
    SELECT * FROM myv3 
    
    CREATE OR REPLACE VIEW myv3
    AS
    SELECT AVG(salary),job_id
    FROM employees
    GROUP BY job_id;
    
    #方式二：
    /*
    语法：
    alter view 视图名
    as 
    查询语句;
    
    */
    ALTER VIEW myv3
    AS
    SELECT * FROM employees;
    
    #三、删除视图
    
    /*
    
    语法：drop view 视图名,视图名,...;
    */
    
    DROP VIEW emp_v1,emp_v2,myv3;
    
    
    #四、查看视图
    
    DESC myv3;
    
    SHOW CREATE VIEW myv3;
    
    
    #五、视图的更新
    
    CREATE OR REPLACE VIEW myv1
    AS
    SELECT last_name,email,salary*12*(1+IFNULL(commission_pct,0)) "annual salary"
    FROM employees;
    
    CREATE OR REPLACE VIEW myv1
    AS
    SELECT last_name,email
    FROM employees;
    
    
    SELECT * FROM myv1;
    SELECT * FROM employees;
    #1.插入
    
    INSERT INTO myv1 VALUES('张飞','zf@qq.com');
    
    #2.修改
    UPDATE myv1 SET last_name = '张无忌' WHERE last_name='张飞';
    
    #3.删除
    DELETE FROM myv1 WHERE last_name = '张无忌';
    
    #具备以下特点的视图不允许更新
    
    
    #①包含以下关键字的sql语句：分组函数、distinct、group  by、having、union或者union all
    
    CREATE OR REPLACE VIEW myv1
    AS
    SELECT MAX(salary) m,department_id
    FROM employees
    GROUP BY department_id;
    
    SELECT * FROM myv1;
    
    #更新
    UPDATE myv1 SET m=9000 WHERE department_id=10;
    
    #②常量视图
    CREATE OR REPLACE VIEW myv2
    AS
    
    SELECT 'john' NAME;
    
    SELECT * FROM myv2;
    
    #更新
    UPDATE myv2 SET NAME='lucy';
    
    
    
    
    
    #③Select中包含子查询
    
    CREATE OR REPLACE VIEW myv3
    AS
    
    SELECT department_id,(SELECT MAX(salary) FROM employees) 最高工资
    FROM departments;
    
    #更新
    SELECT * FROM myv3;
    UPDATE myv3 SET 最高工资=100000;
    
    
    #④join
    CREATE OR REPLACE VIEW myv4
    AS
    
    SELECT last_name,department_name
    FROM employees e
    JOIN departments d
    ON e.department_id  = d.department_id;
    
    #更新
    
    SELECT * FROM myv4;
    UPDATE myv4 SET last_name  = '张飞' WHERE last_name='Whalen';
    INSERT INTO myv4 VALUES('陈真','xxxx');
    
    
    
    #⑤from一个不能更新的视图
    CREATE OR REPLACE VIEW myv5
    AS
    
    SELECT * FROM myv3;
    
    #更新
    
    SELECT * FROM myv5;
    
    UPDATE myv5 SET 最高工资=10000 WHERE department_id=60;
    
    
    
    #⑥where子句的子查询引用了from子句中的表
    
    CREATE OR REPLACE VIEW myv6
    AS
    
    SELECT last_name,email,salary
    FROM employees
    WHERE employee_id IN(
        SELECT  manager_id
        FROM employees
        WHERE manager_id IS NOT NULL
    );
    
    #更新
    SELECT * FROM myv6;
    UPDATE myv6 SET salary=10000 WHERE last_name = 'k_ing';
    
#变量
    /*
    系统变量：
        全局变量
        会话变量
    
    自定义变量：
        用户变量
        局部变量
    
    */
    #一、系统变量
    /*
    说明：变量由系统定义，不是用户定义，属于服务器层面
    注意：全局变量需要添加global关键字，会话变量需要添加session关键字，如果不写，默认会话级别
    使用步骤：
    1、查看所有系统变量
    show global|【session】variables;
    2、查看满足条件的部分系统变量
    show global|【session】 variables like '%char%';
    3、查看指定的系统变量的值
    select @@global|【session】系统变量名;
    4、为某个系统变量赋值
    方式一：
    set global|【session】系统变量名=值;
    方式二：
    set @@global|【session】系统变量名=值;
    
    */
    #1》全局变量
    /*
    作用域：针对于所有会话（连接）有效，但不能跨重启
    */
    #①查看所有全局变量
    SHOW GLOBAL VARIABLES;
    #②查看满足条件的部分系统变量
    SHOW GLOBAL VARIABLES LIKE '%char%';
    #③查看指定的系统变量的值
    SELECT @@global.autocommit;
    #④为某个系统变量赋值
    SET @@global.autocommit=0;
    SET GLOBAL autocommit=0;
    
    #2》会话变量
    /*
    作用域：针对于当前会话（连接）有效
    */
    #①查看所有会话变量
    SHOW SESSION VARIABLES;
    #②查看满足条件的部分会话变量
    SHOW SESSION VARIABLES LIKE '%char%';
    #③查看指定的会话变量的值
    SELECT @@autocommit;
    SELECT @@session.tx_isolation;
    #④为某个会话变量赋值
    SET @@session.tx_isolation='read-uncommitted';
    SET SESSION tx_isolation='read-committed';
    
    #二、自定义变量
    /*
    说明：变量由用户自定义，而不是系统提供的
    使用步骤：
    1、声明
    2、赋值
    3、使用（查看、比较、运算等）
    */
    
    #1》用户变量
    /*
    作用域：针对于当前会话（连接）有效，作用域同于会话变量
    */
    
    #赋值操作符：=或:=
    #①声明并初始化
    SET @变量名=值;
    SET @变量名:=值;
    SELECT @变量名:=值;
    
    #②赋值（更新变量的值）
    #方式一：
        SET @变量名=值;
        SET @变量名:=值;
        SELECT @变量名:=值;
    #方式二：
        SELECT 字段 INTO @变量名
        FROM 表;
    #③使用（查看变量的值）
    SELECT @变量名;
    
    
    #2》局部变量
    /*
    作用域：仅仅在定义它的begin end块中有效
    应用在 begin end中的第一句话
    */
    
    #①声明
    DECLARE 变量名 类型;
    DECLARE 变量名 类型 【DEFAULT 值】;
    
    
    #②赋值（更新变量的值）
    
    #方式一：
        SET 局部变量名=值;
        SET 局部变量名:=值;
        SELECT 局部变量名:=值;
    #方式二：
        SELECT 字段 INTO 具备变量名
        FROM 表;
    #③使用（查看变量的值）
    SELECT 局部变量名;
    
    
    #案例：声明两个变量，求和并打印
    
    #用户变量
    SET @m=1;
    SET @n=1;
    SET @sum=@m+@n;
    SELECT @sum;
    
    #局部变量
    DECLARE m INT DEFAULT 1;
    DECLARE n INT DEFAULT 1;
    DECLARE SUM INT;
    SET SUM=m+n;
    SELECT SUM;
    
    
    #用户变量和局部变量的对比
    
            作用域			定义位置		语法
    用户变量	当前会话		会话的任何地方		加@符号，不用指定类型
    局部变量	定义它的BEGIN END中 	BEGIN END的第一句话	一般不用加@,需要指定类型
    
#存储过程和函数
    /*
    存储过程和函数：类似于java中的方法
    好处：
    1、提高代码的重用性
    2、简化操作
    */
    
    #存储过程
    /*
    含义：一组预先编译好的SQL语句的集合，理解成批处理语句
    1、提高代码的重用性
    2、简化操作
    3、减少了编译次数并且减少了和数据库服务器的连接次数，提高了效率    
    */
    
    #一、创建语法
    
    CREATE PROCEDURE 存储过程名(参数列表)
    BEGIN
    
        存储过程体（一组合法的SQL语句）
    END
    
    #注意：
    /*
    1、参数列表包含三部分
    参数模式  参数名  参数类型
    举例：
    in stuname varchar(20)
    
    参数模式：
    in：该参数可以作为输入，也就是该参数需要调用方传入值
    out：该参数可以作为输出，也就是该参数可以作为返回值
    inout：该参数既可以作为输入又可以作为输出，也就是该参数既需要传入值，又可以返回值
    
    2、如果存储过程体仅仅只有一句话，begin end可以省略
    存储过程体中的每条sql语句的结尾要求必须加分号。
    存储过程的结尾可以使用 delimiter 重新设置
    语法：
    delimiter 结束标记
    案例：
    delimiter $
    */
    
    
    #二、调用语法
    
    CALL 存储过程名(实参列表);
    
    #--------------------------------案例演示-----------------------------------
    #1.空参列表
    #案例：插入到admin表中五条记录
    
    SELECT * FROM admin;
    
    DELIMITER $
    CREATE PROCEDURE myp1()
    BEGIN
        INSERT INTO admin(username,`password`) 
        VALUES('john1','0000'),('lily','0000'),('rose','0000'),('jack','0000'),('tom','0000');
    END $
    
    
    #调用
    CALL myp1()$
    
    #2.创建带in模式参数的存储过程
    
    #案例1：创建存储过程实现 根据女神名，查询对应的男神信息
    
    CREATE PROCEDURE myp2(IN beautyName VARCHAR(20))
    BEGIN
        SELECT bo.*
        FROM boys bo
        RIGHT JOIN beauty b ON bo.id = b.boyfriend_id
        WHERE b.name=beautyName;
        
    
    END $
    
    #调用
    CALL myp2('柳岩')$
    
    #案例2 ：创建存储过程实现，用户是否登录成功
    
    CREATE PROCEDURE myp4(IN username VARCHAR(20),IN PASSWORD VARCHAR(20))
    BEGIN
        DECLARE result INT DEFAULT 0;#声明并初始化
        
        SELECT COUNT(*) INTO result#赋值
        FROM admin
        WHERE admin.username = username
        AND admin.password = PASSWORD;
        
        SELECT IF(result>0,'成功','失败');#使用
    END $
    
    #调用
    CALL myp3('张飞','8888')$
    
    
    #3.创建out 模式参数的存储过程
    #案例1：根据输入的女神名，返回对应的男神名
    
    CREATE PROCEDURE myp6(IN beautyName VARCHAR(20),OUT boyName VARCHAR(20))
    BEGIN
        SELECT bo.boyname INTO boyname
        FROM boys bo
        RIGHT JOIN
        beauty b ON b.boyfriend_id = bo.id
        WHERE b.name=beautyName ;
        
    END $
    
    
    #案例2：根据输入的女神名，返回对应的男神名和魅力值
    
    CREATE PROCEDURE myp7(IN beautyName VARCHAR(20),OUT boyName VARCHAR(20),OUT usercp INT) 
    BEGIN
        SELECT boys.boyname ,boys.usercp INTO boyname,usercp
        FROM boys 
        RIGHT JOIN
        beauty b ON b.boyfriend_id = boys.id
        WHERE b.name=beautyName ;
        
    END $
    
    
    #调用
    CALL myp7('小昭',@name,@cp)$
    SELECT @name,@cp$
    
    
    
    #4.创建带inout模式参数的存储过程
    #案例1：传入a和b两个值，最终a和b都翻倍并返回
    
    CREATE PROCEDURE myp8(INOUT a INT ,INOUT b INT)
    BEGIN
        SET a=a*2;
        SET b=b*2;
    END $
    
    #调用
    SET @m=10$
    SET @n=20$
    CALL myp8(@m,@n)$
    SELECT @m,@n$
    
    
    #三、删除存储过程
    #语法：drop procedure 存储过程名
    DROP PROCEDURE p1;
    DROP PROCEDURE p2,p3;#×
    
    #四、查看存储过程的信息
    DESC myp2;×
    SHOW CREATE PROCEDURE  myp2;
    
    #函数
    /*
    含义：一组预先编译好的SQL语句的集合，理解成批处理语句
    1、提高代码的重用性
    2、简化操作
    3、减少了编译次数并且减少了和数据库服务器的连接次数，提高了效率
    
    区别：
    
    存储过程：可以有0个返回，也可以有多个返回，适合做批量插入、批量更新
    函数：有且仅有1 个返回，适合做处理数据后返回一个结果
    
    */
    
    #一、创建语法
    CREATE FUNCTION 函数名(参数列表) RETURNS 返回类型
    BEGIN
    	函数体
    END
    /*
    
    注意：
    1.参数列表 包含两部分：
    参数名 参数类型
    
    2.函数体：肯定会有return语句，如果没有会报错
    如果return语句没有放在函数体的最后也不报错，但不建议
    
    return 值;
    3.函数体中仅有一句话，则可以省略begin end
    4.使用 delimiter语句设置结束标记
    
    */
    
    #二、调用语法
    SELECT 函数名(参数列表)
    
    
    #------------------------------案例演示----------------------------
    #1.无参有返回
    #案例：返回公司的员工个数
    CREATE FUNCTION myf1() RETURNS INT
    BEGIN
    
    	DECLARE c INT DEFAULT 0;#定义局部变量
    	SELECT COUNT(*) INTO c#赋值
    	FROM employees;
    	RETURN c;
    	
    END $
    
    SELECT myf1()$
    
    
    #2.有参有返回
    #案例1：根据员工名，返回它的工资
    
    CREATE FUNCTION myf2(empName VARCHAR(20)) RETURNS DOUBLE
    BEGIN
    	SET @sal=0;#定义用户变量 
    	SELECT salary INTO @sal   #赋值
    	FROM employees
    	WHERE last_name = empName;
    	
    	RETURN @sal;
    END $
    
    SELECT myf2('k_ing') $
    
    #案例2：根据部门名，返回该部门的平均工资
    
    CREATE FUNCTION myf3(deptName VARCHAR(20)) RETURNS DOUBLE
    BEGIN
    	DECLARE sal DOUBLE ;
    	SELECT AVG(salary) INTO sal
    	FROM employees e
    	JOIN departments d ON e.department_id = d.department_id
    	WHERE d.department_name=deptName;
    	RETURN sal;
    END $
    
    SELECT myf3('IT')$
    
    #三、查看函数
    
    SHOW CREATE FUNCTION myf3;
    
    #四、删除函数
    DROP FUNCTION myf3;
    
    #案例
    #一、创建函数，实现传入两个float，返回二者之和
    
    CREATE FUNCTION test_fun1(num1 FLOAT,num2 FLOAT) RETURNS FLOAT
    BEGIN
    	DECLARE SUM FLOAT DEFAULT 0;
    	SET SUM=num1+num2;
    	RETURN SUM;
    END $
    
    SELECT test_fun1(1,2)$
    
#流程控制结构
    /*顺序、分支、循环*/
    
    #一、分支结构
    #1.if函数
    /*
    语法：if(条件,值1，值2)
    功能：实现双分支
    应用在begin end中或外面
    */
    
    #2.case结构
    /*
    语法：
    情况1：类似于switch
    case 变量或表达式
    when 值1 then 语句1;
    when 值2 then 语句2;
    ...
    else 语句n;
    end 
    
    情况2：
    case 
    when 条件1 then 语句1;
    when 条件2 then 语句2;
    ...
    else 语句n;
    end 
    
    应用在begin end 中或外面
    */
    
    #3.if结构
    
    /*
    语法：
    if 条件1 then 语句1;
    elseif 条件2 then 语句2;
    ....
    else 语句n;
    end if;
    功能：类似于多重if
    
    只能应用在begin end 中
    
    */
    
    #案例1：创建函数，实现传入成绩，如果成绩>90,返回A，如果成绩>80,返回B，如果成绩>60,返回C，否则返回D
    
    CREATE FUNCTION test_if(score FLOAT) RETURNS CHAR
    BEGIN
        DECLARE ch CHAR DEFAULT 'A';
        IF score>90 THEN SET ch='A';
        ELSEIF score>80 THEN SET ch='B';
        ELSEIF score>60 THEN SET ch='C';
        ELSE SET ch='D';
        END IF;
        RETURN ch;
        
        
    END $
    
    SELECT test_if(87)$
    
    #案例2：创建存储过程，如果工资<2000,则删除，如果5000>工资>2000,则涨工资1000，否则涨工资500
    
    
    CREATE PROCEDURE test_if_pro(IN sal DOUBLE)
    BEGIN
        IF sal<2000 THEN DELETE FROM employees WHERE employees.salary=sal;
        ELSEIF sal>=2000 AND sal<5000 THEN UPDATE employees SET salary=salary+1000 WHERE employees.`salary`=sal;
        ELSE UPDATE employees SET salary=salary+500 WHERE employees.`salary`=sal;
        END IF;
        
    END $
    
    CALL test_if_pro(2100)$
    
    #案例1：创建函数，实现传入成绩，如果成绩>90,返回A，如果成绩>80,返回B，如果成绩>60,返回C，否则返回D
    
    CREATE FUNCTION test_case(score FLOAT) RETURNS CHAR
    BEGIN 
        DECLARE ch CHAR DEFAULT 'A';
        
        CASE 
        WHEN score>90 THEN SET ch='A';
        WHEN score>80 THEN SET ch='B';
        WHEN score>60 THEN SET ch='C';
        ELSE SET ch='D';
        END CASE;
        
        RETURN ch;
    END $
    
    SELECT test_case(56)$
    
    
    
    #二、循环结构
    /*
    分类：
    while、loop、repeat
    
    循环控制：
    
    iterate类似于 continue，继续，结束本次循环，继续下一次
    leave 类似于  break，跳出，结束当前所在的循环
    
    */
    
    #1.while
    /*
    
    语法：
    
    【标签:】while 循环条件 do
        循环体;
    end while【 标签】;
    
    #2.loop
    /*
    
    语法：
    【标签:】loop
        循环体;
    end loop 【标签】;
    
    可以用来模拟简单的死循环 */
    
    #3.repeat
    /*
    语法：
    【标签：】repeat
        循环体;
    until 结束循环的条件
    end repeat 【标签】;
    */
    
    #1.没有添加循环控制语句
    #案例：批量插入，根据次数插入到admin表中多条记录
    DROP PROCEDURE pro_while1$
    CREATE PROCEDURE pro_while1(IN insertCount INT)
    BEGIN
        DECLARE i INT DEFAULT 1;
        WHILE i<=insertCount DO
            INSERT INTO admin(username,`password`) VALUES(CONCAT('Rose',i),'666');
            SET i=i+1;
        END WHILE;
        
    END $
    
    CALL pro_while1(100)$
    
    
    /*
    
    int i=1;
    while(i<=insertcount){
    
        //插入
        
        i++;
    
    }
    
    */
    
    
    #2.添加leave语句
    
    #案例：批量插入，根据次数插入到admin表中多条记录，如果次数>20则停止
    TRUNCATE TABLE admin$
    DROP PROCEDURE test_while1$
    CREATE PROCEDURE test_while1(IN insertCount INT)
    BEGIN
        DECLARE i INT DEFAULT 1;
        a:WHILE i<=insertCount DO
            INSERT INTO admin(username,`password`) VALUES(CONCAT('xiaohua',i),'0000');
            IF i>=20 THEN LEAVE a;
            END IF;
            SET i=i+1;
        END WHILE a;
    END $
    
    
    CALL test_while1(100)$
    
    
    #3.添加iterate语句
    
    #案例：批量插入，根据次数插入到admin表中多条记录，只插入偶数次
    TRUNCATE TABLE admin$
    DROP PROCEDURE test_while1$
    CREATE PROCEDURE test_while1(IN insertCount INT)
    BEGIN
        DECLARE i INT DEFAULT 0;
        a:WHILE i<=insertCount DO
            SET i=i+1;
            IF MOD(i,2)!=0 THEN ITERATE a;
            END IF;
            
            INSERT INTO admin(username,`password`) VALUES(CONCAT('xiaohua',i),'0000');
            
        END WHILE a;
    END $
    
    
    CALL test_while1(100)$
    
------------------------------------------------------------------------------------------------------------------------

