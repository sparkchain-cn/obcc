package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.BaseJdbcTemplateDao;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.driver.ContractInfo;

import javax.validation.constraints.NotEmpty;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ContractInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:50
 **/
public class ContractInfoDaoBase extends BaseJdbcTemplateDao<ContractInfo, Long> implements JdbcDao<ContractInfo, Long>, java.io.Serializable {


    public ContractInfo getContractByAddr(@NotEmpty String contractAddr) throws Exception {
        if (StringUtils.isNullOrEmpty(contractAddr)) {
            return null;
        }
        return get(" contract_addr = ? ", new Object[]{contractAddr});
    }

    ////todo:
    @Override
    public String getCreateSql() {
        return " CREATE TABLE " + this.tableName() + " (" +
                primaryKeyName() + "  bigint(80) NOT NULL," +
                " biz_id varchar(200) DEFAULT NULL," +
                " name varchar(200) DEFAULT NULL," +
                " abi varchar(200) DEFAULT NULL," +
                " bin varchar(200) DEFAULT NULL," +
                " hash varchar(200) DEFAULT NULL," +
                " contract_addr varchar(200) DEFAULT NULL," +
                " compile_result varchar(200) DEFAULT NULL," +
                " compile_exception varchar(200) DEFAULT NULL," +
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
        return "contract_info";
        // return JdbcUtil.findTabelNameFromClz(entityClass);
    }

}
