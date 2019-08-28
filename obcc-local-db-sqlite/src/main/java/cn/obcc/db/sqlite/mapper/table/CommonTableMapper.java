package cn.obcc.db.sqlite.mapper.table;

import cn.obcc.db.sqlite.SqliteHelper;
import cn.obcc.db.sqlite.mapper.TableMapper;
import cn.obcc.db.sqlite.vo.Column;

import static cn.obcc.db.sqlite.SqliteFactroy.TB_CONNECTOR;
import static cn.obcc.db.sqlite.SqliteFactroy.TB_UP_CHAIN_RECORD;

/**
 * TaurusSqliteTableMapper
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 14:15
 * @details
 */

public class CommonTableMapper implements TableMapper {
    @Override
    public boolean createTable(SqliteHelper instance, String... tableNames) {
        for (String tableName : tableNames) {
            switch (tableName) {
                case TB_CONNECTOR:
                    //1. 创建表 --> 连接记录表
                    if (!instance.isTableExist(TB_CONNECTOR)) {
                        instance.createTable(TB_CONNECTOR,
                                new Column("tag", "VARCHAR(50)", null),
                                new Column("jdbc", "VARCHAR(100)", null),
                                new Column("createTime", "DATETIME", "datetime(CURRENT_TIMESTAMP,'localtime')"),
                                new Column("standard", "VARCHAR(100)", null),
                                new Column("user", "VARCHAR(100)", null),
                                new Column("password", "VARCHAR(100)", null),
                                new Column("sql", "TEXT", null),
                                new Column("type", "int", null));
                        continue;
                    }
                    break;
                case TB_UP_CHAIN_RECORD:
                    //2. 创建表 --> 上链记录表
                    if (!instance.isTableExist(TB_UP_CHAIN_RECORD)) {
                        instance.createTable(TB_UP_CHAIN_RECORD,
                                new Column("connectorId", "BIGINT", null),
                                new Column("hash", "VARCHAR(100)", null),
                                new Column("upchain", "int", null),
                                new Column("result", "int", null),
                                new Column("lastUpChainPath", "VARCHAR(150)", null),
                                new Column("lastCheckTime", "DATETIME", null),
                                new Column("createTime", "DATETIME", "datetime(CURRENT_TIMESTAMP,'localtime')"));
                        continue;
                    }
                    break;
                default:
                    break;
            }

        }

        return true;

    }
}
