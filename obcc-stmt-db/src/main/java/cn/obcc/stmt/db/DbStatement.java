package cn.obcc.stmt.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.obcc.config.ExProps;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.EMsgType;
import cn.obcc.exception.enums.EStmtType;
import cn.obcc.stmt.IDbStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.driver.TableInfo;
import cn.obcc.vo.stmt.TableDefine;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbStatement extends BaseStatement implements IDbStatement {
    public static final Logger logger = LoggerFactory.getLogger(DbStatement.class);

    @Override
    public String createTable(@NonNull String tableName, @NonNull TableDefine define) throws Exception {

        String bizId = config.getClientId() + "_" + tableName;
        getDriver().getStateMonitor().checkAndSetBizId(bizId);

        if (getDriver().getLocalDb().getTableInfoDao().exist(config.getClientId(), tableName)) {
            throw ObccException.create(EExceptionCode.EXIST_SAME_TABLE_NAME,
                    "存在相同的表名或存储过程名,db:{},tableName:{}.", config.getClientId(), tableName);
        }
        //保存到数据库中，高并发会出现判断的并发问题
        getDriver().getLocalDb().getTableInfoDao().add(new TableInfo() {{
            setId(UuidUtils.get());
            setDbName(config.getClientId());
            setType(EDbOperaType.Table);
            setName(tableName);
            setContents(define.toJson());
        }});


        String table = buildTable(tableName, define);

        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();

        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(table);
        }};

        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {

        };

        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.Db_CREATE_TABLE);
            }});
        }};
//        RecordInfo recordInfo = new RecordInfo();
//        // recordInfo.setBizId(bizId);
//        recordInfo.setStmtType(EStmtType.Db);
//        //recordInfo.setMsgType(EMsgType.Msg);
//        //  recordInfo.setChainCode(config.getChain().getName());
//        exProps.setRecordInfo(recordInfo);

        return getDriver().getAccountHandler()
                .transfer(bizId, account, "0", d.getKey(), exProps, fn);

    }

    protected String buildTable(@NonNull String tableName, @NonNull TableDefine define) {
        define.setName(tableName);
        return define.toJson();
    }


    private String getBizId(String tableName, Long id) {
        return config.getClientId() + "_" + tableName + "_" + id;
    }

    @Override
    public String insert(@NonNull String tableName, @NonNull Long id, @NonNull Object data) throws Exception {

        String bizId = getBizId(tableName, id);
        getDriver().getStateMonitor().checkAndSetBizId(bizId);

        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("data", data);

        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(JSON.toJSONString(map));

        }};

        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {
        };
        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.Db_TABLE_INSERT);
            }});
        }};
        return getDriver().getAccountHandler()
                .transfer(bizId, account, "0", d.getKey(), exProps, fn);

    }


    private String getUpdateBizId(String tableName, Long id) throws Exception {
        String bizId = getBizId(tableName, id);
        String v = getDriver().getLocalDb().getRecordInfoDao().getBizIdPreLikeSize(bizId);
        return bizId + "_" + v;
    }

    @Override
    public String update(String tableName, Long id, Object data) throws Exception {
        String bizId = getBizId(tableName, id);
        final Long updateCount = Long.parseLong(getDriver().getLocalDb().getRecordInfoDao().getBizIdPreLikeSize(bizId)) + 1;
        bizId = bizId + "_" + updateCount;

        getDriver().getStateMonitor().checkAndSetBizId(bizId);

        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().getByProp("biz_id", bizId);
        if (recordInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "id:{} can not find the RecordInfo.", id);
        }

        if (StringUtils.isNullOrEmpty(recordInfo.getHashs())) {
            throw ObccException.create(EExceptionCode.HASH_CAN_NOT_FOUND,
                    "id:{}‘s RecordInfo can not find the hash ,maybe the hash is not return,please wait update this record", id);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
        map.put("lastHash", recordInfo.getHashs());
        map.put("data", data);

        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(JSON.toJSONString(map));
        }};

        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {
        };

        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.Db_TABLE_UPDATE);
                setOrignRecordId(recordInfo.getId() + "");
                setOrignBizId(recordInfo.getBizId());
                setUpdateOrder(updateCount);
            }});
        }};
        return getDriver().getAccountHandler()
                .transfer(bizId, account, "0", d.getKey(), exProps, fn);

    }


    @Override
    public boolean exist(String tableName, Long id) throws Exception {
        String bizId = getBizId(tableName, id);
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().getByProp("biz_id", bizId);
        return recordInfo == null ? false : true;
    }

    @Override
    public boolean exist(EDbOperaType type, String name) throws Exception {
        String bizId = config.getClientId() + "_" + name;
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().getByProp("biz_id", bizId);
        return recordInfo == null ? false : true;
    }

    @Override
    public List<RecordInfo> getRecordVersions(String tableName, Long id) throws Exception {
        String bizId = getBizId(tableName, id);
        List<RecordInfo> list = getDriver().getLocalDb().getRecordInfoDao().getBizIdPreLike(bizId);
        return list;
    }


    @Override
    public String createProcedure(@NonNull String name,
                                  @NonNull String content, List<String> params) throws Exception {

        String bizId = config.getClientId() + "_" + name;
        getDriver().getStateMonitor().checkAndSetBizId(bizId);

        if (getDriver().getLocalDb().getTableInfoDao().exist(config.getClientId(), name)) {
            throw ObccException.create(EExceptionCode.EXIST_SAME_TABLE_NAME,
                    "存在相同的表名或存储过程名,db:{},tableName:{}.", config.getClientId(), name);
        }

        //保存到数据库中，高并发会出现判断的并发问题
        getDriver().getLocalDb().getTableInfoDao().add(new TableInfo() {{
            setId(UuidUtils.get());
            setDbName(config.getClientId());
            setType(EDbOperaType.Procedure);
            setName(name);
            setContents(content);
        }});


        driver.getContractHandler().compile(bizId, content, new ExProps());
        ContractInfo contractInfo = driver.getContractHandler().getContract(bizId, name);

        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
        }};


        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {

        };
        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.Db_CREATE_PROC);
            }});
        }};
        return getDriver().getContractHandler().deploy(bizId, account, contractInfo, fn, exProps, params);

    }


    @Override
    public String exec(String bizId, String procedureName, String method, List<String> params) throws Exception {

        getDriver().getStateMonitor().checkAndSetBizId(bizId);

        ContractInfo contractInfo = driver.getContractHandler().getContract(bizId, procedureName);
        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {

        };
        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
        }};

        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.Db_PROC_EXEC);
            }});
        }};
        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, exProps, fn, method, params);

    }
}
