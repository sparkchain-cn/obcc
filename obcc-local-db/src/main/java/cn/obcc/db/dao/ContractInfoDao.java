package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.JdbcTemplateDao;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.vo.driver.ContractInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ContractInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:50
 **/
public class ContractInfoDao  extends JdbcTemplateDao<ContractInfo, Long> implements JdbcDao<ContractInfo, Long>, java.io.Serializable  {


    ////todo:
    public String getCreateSql(){
        return " CREATE TABLE account_info" +
                " id bigint(80) NOT NULL," +
                " biz_id varchar(200) DEFAULT NULL," +
                " name varchar(200) DEFAULT NULL," +
                " abi varchar(200) DEFAULT NULL," +
                " bin varchar(200) DEFAULT NULL," +
                " hash varchar(200) DEFAULT NULL," +
                " contract_addr varchar(200) DEFAULT NULL," +
                " compile_result varchar(200) DEFAULT NULL," +
                " compile_exception varchar(200) DEFAULT NULL," +
                " state int DEFAULT NULL," +
                " PRIMARY KEY (`id`) )";
    }
    public String primaryKeyName() {
        //return JdbcUtil.findIdNameForClz(entityClass);
        return "id";
    }

    public String tableName() {
        return "contract_info";
        // return JdbcUtil.findTabelNameFromClz(entityClass);
    }

}
