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

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @auther mac
 * @date 2020-01-12
 */
public class EmployeeStatusTypeHandler implements TypeHandler<EmployeeStatus>{

    @Override
    public void setParameter(PreparedStatement ps, int i, EmployeeStatus parameter, JdbcType jdbcType) throws SQLException {
        // 和数据库交互时使用枚举的code
        ps.setString(i,parameter.getCode());
    }

    @Override
    public EmployeeStatus getResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return EmployeeStatus.getEmployeeStatusByCode(code);
    }

    @Override
    public EmployeeStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return EmployeeStatus.getEmployeeStatusByCode(code);
    }

    @Override
    public EmployeeStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return EmployeeStatus.getEmployeeStatusByCode(code);
    }
}
