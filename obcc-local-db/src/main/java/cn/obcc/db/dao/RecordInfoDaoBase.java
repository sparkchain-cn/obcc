package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.BaseJdbcTemplateDao;
import cn.obcc.vo.driver.RecordInfo;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class RecordInfoDaoBase extends BaseJdbcTemplateDao<RecordInfo, Long> implements JdbcDao<RecordInfo, Long>, java.io.Serializable {


    public String getBizIdPreLikeSize(String bizId) throws Exception {
        String v = getValue("select count(1) from record_info where biz_id like ?", new Object[]{bizId + "%"});
        return v;
    }

    public List<RecordInfo> getBizIdPreLike(String bizId) throws Exception {
        List<RecordInfo> list = query("biz_id like ?", new Object[]{bizId + "%"});
        return list;
    }

    public boolean existBizId(String bizId) {
        String v = getValue("select count(1) from record_info where biz_id= ? ", new Object[]{bizId + "%"});
        return Long.parseLong(v) > 0 ? true : false;
    }

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
