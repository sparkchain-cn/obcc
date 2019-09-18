package cn.obcc.stmt.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.obcc.config.ExConfig;
import cn.obcc.db.dao.TableInfoDaoBase;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.stmt.IDbStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.db.operate.BizIdExector;
import cn.obcc.stmt.db.operate.InsertHelper;
import cn.obcc.stmt.db.operate.TableBuilder;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.stmt.TableDefinition;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该类主要采用区块链来备份传统数据库中修改的履历数据
 *
 * @author mgicode
 * @desc DbStatement
 * @email 546711211@qq.com
 * @date 2019年9月16日 下午1:16:39
 */
public class DbStatement extends BaseStatement implements IDbStatement {

    public static final Logger logger = LoggerFactory.getLogger(DbStatement.class);

    @Override
    public String createTable(@NonNull String tableName, @NonNull TableDefinition define) throws Exception {

        String bizId = BizIdExector.getTableBizId(config.getClientId(), tableName);
        checkBizId(bizId);
        checkName(tableName);
        define.setName(tableName);
        TableBuilder.saveTableInfo(driver, config.getClientId(), tableName, define);

        return TableBuilder.doCreateTable(driver, config, bizId, define);
    }

    @Override
    public String createProcedure(@NonNull String name, @NonNull String content, List<Object> params) throws Exception {

        String bizId = BizIdExector.getTableBizId(config.getClientId(), name);
        checkBizId(bizId);
        checkName(name);
        TableBuilder.saveProcInfo(driver, config.getClientId(), name, content);

        return TableBuilder.doCreateProc(driver, config, bizId, name, content, params);
    }

    @Override
    public String insert(@NonNull String tableName, @NonNull Long id, @NonNull Object data) throws Exception {
        String bizId = BizIdExector.getRecordBizId(config.getClientId(), tableName, id);
        checkBizId(bizId);
        return InsertHelper.doInsert(driver, config, tableName, bizId, data);
    }


    @Override
    public String update(String tableName, Long id, Object data) throws Exception {
        String bizId = BizIdExector.getRecordBizId(config.getClientId(), tableName, id);
        RecordInfo orignRecordInfo = InsertHelper.getRecordInfo(driver, bizId);
        Long updateCount = InsertHelper.getUpdateCount(driver, bizId);
        bizId = bizId + "##" + updateCount;
        checkBizId(bizId);

        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("id", id);
        map.put("order", updateCount + 1);
        map.put("data", data);

        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        FromAccount account = new FromAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(JSON.toJSONString(map));
        }};

        return driver.getAccountHandler().text(bizId, account, d.getKey());
    }


    @Override
    public boolean exist(String tableName, Long id) throws Exception {
        String bizId = BizIdExector.getRecordBizId(config.getClientId(), tableName, id);
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().getByProp("biz_id", bizId);
        return recordInfo == null ? false : true;
    }

    @Override
    public boolean exist(EDbOperaType type, String name) throws Exception {
        String bizId = BizIdExector.getTableBizId(config.getClientId(), name);
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().getByProp("biz_id", bizId);
        return recordInfo == null ? false : true;
    }

    @Override
    public List<RecordInfo> getUpdateRecords(String tableName, Long id) throws Exception {
        String bizId = BizIdExector.getRecordBizId(config.getClientId(), tableName, id);
        List<RecordInfo> list = getDriver().getLocalDb().getRecordInfoDao().getBizIdPreLike(bizId);
        return list;
    }


    @Override
    public String exec(String bizId, String procedureName, String method, List<Object> params) throws Exception {

        checkBizId(bizId);
        ContractInfo ci = driver.getContractHandler().getContract(bizId, procedureName);

        KeyValue<String> v = config.getStorageSrcAccount();
        FromAccount account = new FromAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
        }};
        //只做特有的部分
        ExConfig exConfig = new ExConfig() {{
//            setRecordInfo(new RecordInfo() {{
//                setStmtType(EStmtType.Db_PROC_EXEC);
//            }});
        }};
        return driver.getContractHandler().invoke(bizId, ci, account, method, params, null, exConfig);

    }

    @Override
    public RecordInfo recordInfo(String obccHash) throws Exception {
        Long id = null;
        try {
            id = Long.parseLong(obccHash);
        } catch (NumberFormatException e) {
            logger.error("obccHash must number string ,your hash is {}", obccHash);
            return null;
        }
        if (id == null) {
            return null;
        }
        return driver.getLocalDb().getRecordInfoDao().getById(id);
    }

    private void checkName(String tableName) throws Exception {
        TableInfoDaoBase tableInfoDao = getDriver().getLocalDb().getTableInfoDao();
        if (tableInfoDao.exist(config.getClientId(), tableName)) {
            throw ObccException.create(EExceptionCode.EXIST_SAME_TABLE_NAME,
                    "存在相同的表名或存储过程名,db:{},tableName:{}.", config.getClientId(), tableName);
        }
    }


}
