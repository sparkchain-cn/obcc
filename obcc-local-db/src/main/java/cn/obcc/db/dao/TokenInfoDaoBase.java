package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.BaseJdbcTemplateDao;
import cn.obcc.vo.driver.TokenInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class TokenInfoDaoBase extends BaseJdbcTemplateDao<TokenInfo, Long> implements JdbcDao<TokenInfo, Long>, java.io.Serializable  {


//    ////todo:
//    @Override
//    public String getCreateSql(){
//        return " CREATE TABLE " + this.tableName() + " (" +
//                primaryKeyName() + "  bigint(80) NOT NULL," +
//                " biz_id varchar(200) DEFAULT NULL," +
//                " hash varchar(200) DEFAULT NULL," +
//                " contract varchar(200) DEFAULT NULL," +
//                " code varchar(200) DEFAULT NULL," +
//                " name varchar(200) DEFAULT NULL," +
//                " hash varchar(200) DEFAULT NULL," +
//                " supply bigint(80) DEFAULT NULL," +
//                " precisions int DEFAULT NULL," +
//                " contract_abi varchar(200) DEFAULT NULL," +
//                " contract_address varchar(200) DEFAULT NULL," +
//                " state int DEFAULT NULL," +
//                " PRIMARY KEY (`" + primaryKeyName() + "`) )";
//    }
//
//    @Override
//    public String primaryKeyName() {
//        //return JdbcUtil.findIdNameForClz(entityClass);
//        return "id";
//    }
//
//    @Override
//    public String tableName() {
//        return "token_info";
//        // return JdbcUtil.findTabelNameFromClz(entityClass);
//    }

}
