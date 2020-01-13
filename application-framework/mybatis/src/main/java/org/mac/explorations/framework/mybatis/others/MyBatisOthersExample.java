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

package org.mac.explorations.framework.mybatis.others;

import org.apache.ibatis.session.SqlSession;
import org.mac.explorations.framework.mybatis.BaseExample;
import org.mac.explorations.framework.mybatis.Department;
import org.mac.explorations.framework.mybatis.Employee;
import org.mac.explorations.framework.mybatis.EmployeeMapper;

import java.util.List;
import java.util.UUID;

/**
 * @auther mac
 * @date 2020-01-11
 */
public class MyBatisOthersExample extends BaseExample {

    @Override
    protected void doExample(SqlSession openSession) {
        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
        // 分页查询
        List<Employee> employees = mapper.getEmployeeByNameWithPage("a",1,10);
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    // 使用批量Executor执行批量保存
    private static void batchInsert(SqlSession openSession, boolean batchExecutor) {
        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
        Department department = new Department();
        department.setId(1);
        //department.setName("DEV");
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++){
            Employee emp = new Employee(UUID.randomUUID().toString().substring(0,8).toLowerCase(), 18, "1", "name"+i+"@163.com");
            emp.setDepartment(department);
            mapper.addEmployee(emp);
        }
        openSession.commit();

        System.err.println((batchExecutor ? "===> Batch executor cost:" : "===> Non batch executor cost:" )+((System.nanoTime()-startTime)/1000000000.00D) + " s");
    }

    public static void main(String[] args) {
        // 分页查询
        // new MyBatisOthersExample().execute();
        /**
         * 批量处理器
         *
         * @see org.apache.ibatis.executor.BatchExecutor#doUpdate(org.apache.ibatis.mapping.MappedStatement, java.lang.Object)
         *
         * <pre>
         *     if (sql.equals(currentSql) && ms.equals(currentStatement)) {
         *         int last = statementList.size() - 1;
         *         stmt = statementList.get(last);
         *         applyTransactionTimeout(stmt);
         *         handler.parameterize(stmt);//fix Issues 322
         *         BatchResult batchResult = batchResultList.get(last);
         *         batchResult.addParameterObject(parameterObject);
         *     } else {
         *         Connection connection = getConnection(ms.getStatementLog());
         *         stmt = handler.prepare(connection, transaction.getTimeout());
         *         handler.parameterize(stmt);    //fix Issues 322
         *         currentSql = sql;
         *         currentStatement = ms;
         *         statementList.add(stmt);
         *         batchResultList.add(new BatchResult(ms, sql, parameterObject));
         *     }
         *     handler.batch(stmt);
         * </pre>
         *
         */
       /*
       new BaseExample(true){
            @Override
            protected void doExample(SqlSession openSession) {
                batchInsert(openSession,this.batchExecutor);
            }
        }.execute();

        new BaseExample(false){
            @Override
            protected void doExample(SqlSession openSession) {
                batchInsert(openSession,this.batchExecutor);
            }
        }.execute();
        */
       // 存储过程调用
       /*
       new BaseExample(){

           @Override
           protected void doExample(SqlSession openSession) {
               EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
               ProducerCallParam param = new ProducerCallParam(0,2,0);
               // 有OUT参数的存储过程不支持缓存
               List<Employee> employees = mapper.callProcedure(param);
               System.out.println("count:"+param.getCount());
               for (Employee employee : employees) {
                   System.out.println(employee);
               }
           }
       }.execute();
       */

        // 自定义枚举处理器
        new BaseExample(){
            @Override
            protected void doExample(SqlSession openSession) {
                EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
                Employee employee = mapper.getEmployeeById(1);
                System.out.println(employee);

                mapper.addEmployee(new Employee("Alex", 34, "1", "Alex@gmail.com", EmployeeStatus.LEAVE, new Department(1)));
                openSession.commit();
            }
        }.execute();
    }

    public static class ProducerCallParam {

        private int start;
        private int end;
        private int count;

        public ProducerCallParam() {
        }

        public ProducerCallParam(int start, int end, int count) {
            this.start = start;
            this.end = end;
            this.count = count;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "ProducerCallParam{" +
                    "start=" + start +
                    ", end=" + end +
                    ", count=" + count +
                    '}';
        }
    }
}
