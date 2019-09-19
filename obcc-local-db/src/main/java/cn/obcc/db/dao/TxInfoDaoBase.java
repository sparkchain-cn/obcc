package cn.obcc.db.dao;

import cn.obcc.db.base.JdbcDao;
import cn.obcc.db.base.BaseJdbcTemplateDao;
import cn.obcc.vo.db.BlockTxInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfoDao
 * @desc TODO
 * @date 2019/8/28 0028  17:44
 **/
public class TxInfoDaoBase extends BaseJdbcTemplateDao<BlockTxInfo, Long> implements JdbcDao<BlockTxInfo, Long>, java.io.Serializable  {

//    @Override
//    public String getCreateSql(){
//        return " CREATE TABLE " + this.tableName() + " (" +
//                primaryKeyName() + "  bigint(80) NOT NULL," +
//                " tx_type int DEFAULT NULL," +
//                " chain_code varchar(200) DEFAULT NULL," +
//                " block_hash varchar(200) DEFAULT NULL," +
//                " block_number varchar(200) DEFAULT NULL," +
//                " block_time bigint(80) DEFAULT NULL," +
//                " hash varchar(200) DEFAULT NULL," +
//                " state varchar(200) DEFAULT NULL," +
//                " src_addr varchar(200) DEFAULT NULL," +
//                " nonce bigint(80) DEFAULT NULL," +
//                " dest_addr varchar(200) DEFAULT NULL," +
//                " gas_price varchar(200) DEFAULT NULL," +
//                " gas_limit varchar(200) DEFAULT NULL," +
//                " token varchar(200) DEFAULT NULL," +
//                " gas_used varchar(200) DEFAULT NULL," +
//                " amount varchar(200) DEFAULT NULL," +
//                " memos varchar(2000) DEFAULT NULL," +
//                " details varchar(2000) DEFAULT NULL," +
//                " contract_address varchar(2000) DEFAULT NULL," +
//                " method varchar(2000) DEFAULT NULL," +
//                " method_params varchar(2000) DEFAULT NULL," +
//                " sharding_flag bigint(80) DEFAULT NULL," +
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
//        return "block_tx_info";
//        // return JdbcUtil.findTabelNameFromClz(entityClass);
//    }

}
