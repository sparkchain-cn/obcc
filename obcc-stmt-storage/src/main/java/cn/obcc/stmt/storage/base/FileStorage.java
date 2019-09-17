package cn.obcc.stmt.storage.base;

import cn.obcc.config.ExProps;
import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.EMsgType;
import cn.obcc.exception.enums.EStmtType;
import cn.obcc.exception.enums.EStoreType;
import cn.obcc.stmt.storage.StorageStatement;
import cn.obcc.utils.FileSafeUtils;
import cn.obcc.utils.FileUtils;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.driver.RecordInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName FileStorage
 * @desc TODO
 * @date 2019/9/16 0016  8:25
 **/
public class FileStorage {
    public static final Logger logger = LoggerFactory.getLogger(StorageStatement.class);

    public static void store(String bizId, String msg, ObccConfig config, IChainDriver driver) throws Exception {
        InputStream is = new ByteArrayInputStream(msg.getBytes());
        store(bizId, is, config, driver);
    }


    public static void store(String bizId, InputStream stream, ObccConfig config, IChainDriver driver) throws Exception {
        FileUtils.save(stream, config.getLocalFilePath(), bizId);
        String sha1 = FileSafeUtils.getSha1(stream);
        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(sha1);
        }};

        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {

        };

        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.STORAGE);
                setMsgType(EMsgType.File);
                setStoreType(EStoreType.FileSystem);
            }});
        }};
        driver.getAccountHandler().transfer(bizId, account, "0", d.getKey(), exProps, fn);
    }

    public static File getFile(String path, String bizId, String sha1) throws Exception {
        String pathName = path + File.pathSeparator + bizId;

        File file = new File(pathName);

        if (!file.exists()) {
            logger.error("file:{} not exist.", pathName);
            return null;
        }

        String sha11 = FileSafeUtils.getSha1(new FileInputStream(file));
        if (sha11.equals(sha1)) {
            return file;
        }
        throw ObccException.create(EExceptionCode.FILE_SHA1_CHECK_ERROR,
                "File SHA1 {} and input sha1 {} check no agree", sha11, sha1);

    }
}
