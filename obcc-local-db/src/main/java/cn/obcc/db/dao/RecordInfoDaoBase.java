package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.BaseJdbcTemplateDao;
import cn.obcc.vo.driver.RecordInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class RecordInfoDaoBase extends BaseJdbcTemplateDao<RecordInfo, Long> implements JdbcDao<RecordInfo, Long>, java.io.Serializable {


    ////todo:
    @Override
    public String getCreateSql() {
        return " CREATE TABLE " + this.tableName() + " (" +
                primaryKeyName() + "  bigint(80) NOT NULL," +
                " biz_id varchar(200) DEFAULT NULL," +
                " hashs varchar(200) DEFAULT NULL," +
                " user_name varchar(200) DEFAULT NULL," +
                " state int DEFAULT NULL," +
                " PRIMARY KEY (`" + primaryKeyName() + "`) )";
    }

    @Override
    public String primaryKeyName() {
        //return JdbcUtil.findIdNameForClz(entityClass);
        return "id";
    }

    @Override
    public String tableName() {
        return "record_info";
        // return JdbcUtil.findTabelNameFromClz(entityClass);
    }

}
