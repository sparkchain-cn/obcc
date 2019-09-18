package cn.obcc.stmt.db.operate;

import cn.obcc.config.ExConfig;
import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.driver.RecordInfo;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName InsertHelper
 * @desc TODO
 * @date 2019/9/17 0017  18:10
 **/
public class InsertHelper {

    public static String doInsert(IChainDriver driver, ObccConfig config, @NonNull String tableName, String bizId, @NonNull Object data) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableName);
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


    public static RecordInfo getRecordInfo(IChainDriver driver, String bizId) throws Exception {
        RecordInfo recordInfo = driver.getLocalDb().getRecordInfoDao().getByProp("biz_id", bizId);
        if (recordInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "id:{} can not find the RecordInfo.", bizId);
        }
        return recordInfo;
    }

    public static Long getUpdateCount(IChainDriver driver, String bizId) throws Exception {
        return Long.parseLong(driver.getLocalDb().getRecordInfoDao().getBizIdPreLikeSize(bizId));
    }
}

