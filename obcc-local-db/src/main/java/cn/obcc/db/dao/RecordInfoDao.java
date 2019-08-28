package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.JdbcTemplateDao;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.vo.driver.RecordInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class RecordInfoDao extends JdbcTemplateDao<RecordInfo, Long> implements JdbcDao<RecordInfo, Long>, java.io.Serializable  {


    ////todo:
    public String getCreateSql(){
        return " CREATE TABLE account_info" +
                " id bigint(80) NOT NULL," +
                " biz_id varchar(200) DEFAULT NULL," +
                " hashs varchar(200) DEFAULT NULL," +
                " user_name varchar(200) DEFAULT NULL," +
                " state int DEFAULT NULL," +
                " PRIMARY KEY (`id`) )";
    }
    public String primaryKeyName() {
        //return JdbcUtil.findIdNameForClz(entityClass);
        return "id";
    }

    public String tableName() {
        return "record_info";
        // return JdbcUtil.findTabelNameFromClz(entityClass);
    }

}
