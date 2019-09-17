package cn.obcc.db.dao;

import cn.obcc.db.base.BaseJdbcTemplateDao;
import cn.obcc.db.base.JdbcDao;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.TableInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class TableInfoDaoBase extends BaseJdbcTemplateDao<TableInfo, Long> implements JdbcDao<TableInfo, Long>, java.io.Serializable {


    public boolean exist(String db, String tablename) {
        String v = getValue("select count(1) from " + this.tableName() + "where db_name= ? and name =?", new Object[]{db, tablename});
        return Long.parseLong(v) > 0 ? true : false;
    }

    ////todo:
    @Override
    public String getCreateSql() {
        return " CREATE TABLE " + this.tableName() + " (" +
                primaryKeyName() + "  bigint(80) NOT NULL," +
                " type varchar(200) DEFAULT NULL," +
                " db_name varchar(200) DEFAULT NULL," +
                " name varchar(200) DEFAULT NULL," +
                " content varchar(2000) DEFAULT NULL," +
                " PRIMARY KEY (`" + primaryKeyName() + "`) )";
    }

    @Override
    public String primaryKeyName() {
        return "id";
    }

    @Override
    public String tableName() {
        return "table_info";
    }

}
