package cn.obcc.stmt.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.enums.EMsgType;
import cn.obcc.enums.EStoreType;
import cn.obcc.exception.ObccException;

import cn.obcc.stmt.IStorageStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.storage.base.FileStorage;
import cn.obcc.stmt.storage.base.IpfsStorage;
import cn.obcc.stmt.storage.base.MemoStorage;
import cn.obcc.utils.FileSafeUtils;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.RecordInfo;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageStatement extends BaseStatement implements IStorageStatement {
    public static final Logger logger = LoggerFactory.getLogger(StorageStatement.class);


    @Override
    public void store(@NonNull String bizid, @NonNull String msg) throws Exception {
        store(bizid, msg, EStoreType.Memo);
    }

    @Override
    public void store(String bizid, File file) throws Exception {
        store(bizid, file, EStoreType.FileSystem);
    }

    @Override
    public void store(String bizid, InputStream stream) throws Exception {
        FileStorage.store(bizid, stream, config, getDriver());
    }

    @Override
    public void store(@NonNull String bizId, @NonNull String msg, EStoreType type) throws Exception {
        checkBizId(bizId);
        if (type == EStoreType.FileSystem) {
            FileStorage.store(bizId, msg, config, getDriver());

        } else if (type == EStoreType.Ipfs) {
            IpfsStorage.store(bizId, msg, config, getDriver());
        } else {
            MemoStorage.store(bizId, msg, config, getDriver());
        }
    }

    @Override
    public void store(String bizId, File file, EStoreType type) throws Exception {
        checkBizId(bizId);
        if (type == EStoreType.FileSystem) {
            FileStorage.store(bizId, new FileInputStream(file), config, getDriver());

        } else if (type == EStoreType.Ipfs) {
            IpfsStorage.store(bizId, new FileInputStream(file), config, getDriver());
        } else {
            MemoStorage.store(bizId, new FileInputStream(file), config, getDriver());
        }

    }


    @Override
    public BizTxInfo getState(String bizid) throws Exception {
        return getDriver().getAccountHandler().getTxByBizId(bizid, new ExConfig());
    }


    @Override
    public String view(String bizid) throws Exception {
        RecordInfo recordInfo = checkAndGet(bizid);

        if (!recordInfo.getMsgType().equals(EMsgType.Msg.getName())) {
            logger.error("bizId:{}  find recordInfo type is not msg type,is {}", bizid, recordInfo.getMsgType());
            return null;
        }

        BizTxInfo txInfos = getState(bizid);
        StringBuffer sb = new StringBuffer();
        txInfos.getRecordInfos().forEach((ri) -> {
            if (ri.getMemosObj() != null) {
                //todo:maybe adjust the order?
                sb.append(ri.getMemosObj().getData());
            }
        });
        return sb.toString();
    }

    private RecordInfo findRecord(String bizId) throws Exception {
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().getByProp("biz_id", bizId);
        return recordInfo;
    }

    private RecordInfo checkAndGet(String bizId) throws Exception {
        RecordInfo recordInfo = findRecord(bizId);
        if (recordInfo == null) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "bizId:{} can not find recordInfo", bizId);
        }
        if (recordInfo.getMsgType() == null) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "bizId:{}  find recordInfo type is null", bizId);
        }

        if (recordInfo.getStoreType() == null) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "bizId:{}  find recordInfo type is null", bizId);
        }
        return recordInfo;
    }

    @Override
    public InputStream download(String bizId) throws Exception {
        RecordInfo recordInfo = checkAndGet(bizId);

        if (!recordInfo.getMsgType().equals(EMsgType.File.getName())) {
            logger.error("bizId:{}  find recordInfo type is not file type,is {}", bizId, recordInfo.getMsgType());
            return null;
        }

        BizTxInfo txInfos = getState(bizId);
        // StringBuffer sb = new StringBuffer();
        if (txInfos == null || txInfos.getRecordInfos() == null || txInfos.getRecordInfos().size() == 0) {
            logger.error("bizId:{}  can not find tx from the chain", bizId);
            return null;
        }
        BlockTxInfo ri = txInfos.getRecordInfos().get(0);
        //hashs
        StringBuffer sb = new StringBuffer();
        txInfos.getRecordInfos().forEach((r) -> {
            if (ri.getMemosObj() != null) {
                //todo:maybe adjust the order?
                sb.append(ri.getMemosObj().getData());
            }
        });

        if (recordInfo.getStoreType().equals(EStoreType.Ipfs)) {
            //ipfs hashs
            return IpfsStorage.getFile(sb.toString(), config, driver);
        } else if (recordInfo.getStoreType().equals(EStoreType.FileSystem)) {
            //sha1s
            return new FileInputStream(FileStorage.getFile(config.getLocalFilePath(), bizId, sb.toString()));

        }

        logger.error("bizId:{}  downfile occur unkown exception. storage tyep  is {}", bizId, recordInfo.getStoreType());
        return null;
    }


    @Override
    public boolean verifyMsg(@NonNull String bizId, @NonNull String msg) throws Exception {
        String oldMsg = view(bizId);
        if (msg.equals(oldMsg)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean verifyFile(@NonNull String bizId, @NonNull String sha1) throws Exception {
        InputStream file = download(bizId);
        String newSha1 = FileSafeUtils.getSha1(file);
        return sha1.equals(newSha1);
    }


}
