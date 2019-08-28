package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.JdbcTemplateDao;
import cn.obcc.vo.driver.AccountInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class RecordInfoDao extends JdbcTemplateDao<AccountInfo, Long> implements JdbcDao<AccountInfo, Long>, java.io.Serializable  {

    @Override
    public void createTable() {
        ////todo:
        String sql = "";
        getJdbcTemplate().update(sql);
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
