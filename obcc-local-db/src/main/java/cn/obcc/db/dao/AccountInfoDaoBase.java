package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.BaseJdbcTemplateDao;
import cn.obcc.vo.db.AccountInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class AccountInfoDaoBase extends BaseJdbcTemplateDao<AccountInfo, Long> implements JdbcDao<AccountInfo, Long>, java.io.Serializable {


 ////todo:
//    @Override
//    public String getCreateSql(){
//        return " CREATE TABLE " + this.tableName() + " (" +
//                primaryKeyName() + "  bigint(80) NOT NULL," +
//                " biz_id varchar(200) DEFAULT NULL," +
//                " user_name varchar(200) DEFAULT NULL," +
//                " password varchar(200) DEFAULT NULL," +
//                " address varchar(200) DEFAULT NULL," +
//                " secret varchar(200) DEFAULT NULL," +
//                " PRIMARY KEY (`" + primaryKeyName() + "`) )";
//    }
//    @Override
//    public String primaryKeyName() {
//        //return JdbcUtil.findIdNameForClz(entityClass);
//        return "id";
//    }
//    @Override
//    public String tableName() {
//        return "account_info";
//        // return JdbcUtil.findTabelNameFromClz(entityClass);
//    }

}
