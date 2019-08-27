package cn.obcc.db.sqlite.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 9:21
 * @details
 */
public interface RowMapper<T> {

    T mapRow(ResultSet rs, int index) throws SQLException;
}
