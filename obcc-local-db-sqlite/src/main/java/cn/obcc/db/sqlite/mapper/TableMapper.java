package cn.obcc.db.sqlite.mapper;

import cn.obcc.db.sqlite.SqliteHelper;

/**
 * TableMapper
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 14:12
 * @details
 */
public interface TableMapper {

    boolean createTable(SqliteHelper instance, String... tableNames);

}
