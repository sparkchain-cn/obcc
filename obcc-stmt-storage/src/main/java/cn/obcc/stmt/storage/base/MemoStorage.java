package cn.obcc.stmt.storage.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.vo.driver.FromAccount;
import cn.obcc.vo.KeyValue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName MemoStorage
 * @desc TODO
 * @date 2019/9/16 0016  8:32
 **/
public class MemoStorage {
    public static void store(final String bizId, String msg, ObccConfig config, IChainDriver driver) throws Exception {
        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        FromAccount account = new FromAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(msg);
        }};

        driver.getAccountHandler().text(bizId, account, d.getKey());
    }


    public static void store(String bizId, InputStream inputStream, ObccConfig config, IChainDriver driver) throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        String msg = result.toString("UTF-8");
        store(bizId, msg, config, driver);
    }
}
