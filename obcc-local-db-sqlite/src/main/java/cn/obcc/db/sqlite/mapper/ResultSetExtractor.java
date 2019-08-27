package cn.obcc.db.sqlite.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSetExtractor
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 9:19
 * @details
 */
public interface ResultSetExtractor<T> {

    T extractData(ResultSet rs) throws SQLException;

}
