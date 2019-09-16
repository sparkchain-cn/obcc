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
import cn.obcc.stmt.IDbStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.stmt.TableDefine;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbStatement extends BaseStatement implements IDbStatement {
    public static final Logger logger = LoggerFactory.getLogger(DbStatement.class);

    @Override
    public String createTable(@NonNull String tableName, TableDefine define) throws Exception {

        String bizId = config.getClientId() + "_" + tableName;
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

        return getDriver().getAccountHandler()
                .transfer(bizId, account, "0", d.getKey(), new ExProps(), fn);

    }

    protected String buildTable(String tableName, TableDefine define) {

        return null;
    }


    private String getBizId(String tableName, Long id) {
        return config.getClientId() + "_" + tableName + "_" + id;
    }

    @Override
    public String insert(@NonNull String tableName, @NonNull Long id, @NonNull Object data) throws Exception {

        String bizId = getBizId(tableName, id);
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

        return getDriver().getAccountHandler()
                .transfer(bizId, account, "0", d.getKey(), new ExProps(), fn);

    }

    protected Long getSize(String tableName, Long id) throws Exception {
        String bizId = getBizId(tableName, id);
        String v = getDriver().getLocalDb().getRecordInfoDao().
                queryForSingle("select count(1) from record_info where biz_id like '" + bizId + "%'");
        return Long.parseLong(v);
    }

    private String getUpdateBizId(String tableName, Long id) throws Exception {
        String bizId = getBizId(tableName, id);
        String v = getDriver().getLocalDb().getRecordInfoDao().
                queryForSingle("select count(1) from record_info where biz_id like '" + bizId + "%'");
        return bizId + "_" + v;
    }

    @Override
    public String update(String tableName, Long id, Object data) throws Exception {
        String bizId = getUpdateBizId(tableName, id);
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().findOneByProp("biz_id", bizId);
        if (recordInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "id:{}can not find the RecordInfo.", id);
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

        return getDriver().getAccountHandler()
                .transfer(bizId, account, "0", d.getKey(), new ExProps(), fn);

    }


    @Override
    public boolean exist(String tableName, Long id) throws Exception {
        String bizId = getBizId(tableName, id);
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().findOneByProp("biz_id", bizId);
        return recordInfo == null ? false : true;
    }

    @Override
    public boolean exist(EDbOperaType type, String name) throws Exception {
        String bizId = config.getClientId() + "_" + name;
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().findOneByProp("biz_id", bizId);
        return recordInfo == null ? false : true;
    }

    @Override
    public List<RecordInfo> getRecordVersions(String tableName, Long id) throws Exception {
        String bizId = getBizId(tableName, id);
        List<RecordInfo> list = getDriver().getLocalDb().getRecordInfoDao().
                query("biz_id like ?", new Object[]{bizId + "%"});
        return list;
    }


    @Override
    public void createProcedure(@NonNull String name,
                                @NonNull String content, List<String> params) throws Exception {
        String bizId = config.getClientId() + "_" + name;
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

        getDriver().getContractHandler().deploy(bizId, account, contractInfo, fn, new ExProps(), params);

    }


    @Override
    public String exec(String bizId, String procedureName, String method, List<String> params) throws Exception {
        ContractInfo contractInfo = driver.getContractHandler().getContract(bizId, procedureName);
        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {

        };
        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
        }};

        return getDriver().getContractHandler().invoke(bizId, contractInfo, account, new ExProps(), fn, method, params);

    }
}
