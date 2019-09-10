package cn.obcc.stmt.storage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import cn.obcc.config.ExProps;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EStoreType;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.stmt.IStorageStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.fn.IUploadFn;
import cn.obcc.stmt.fn.IVerifyFn;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.RetData;
import lombok.NonNull;

public class StorageStatement extends BaseStatement implements IStorageStatement {

    @Override
    public void store(@NonNull String bizid, @NonNull String msg, EStoreType type) throws Exception {
        if (type == EStoreType.FileSystem) {
            InputStream is = new ByteArrayInputStream(msg.getBytes());
            store(bizid, is);
            throw new RuntimeException("un impl.");

        } else if (type == EStoreType.Ipfs) {
            throw new RuntimeException("un impl.");
        } else {
            store(bizid, msg);
        }
    }

    @Override
    public void store(String bizid, File file, EStoreType type) throws Exception {

        if (type == EStoreType.Memo) {
            store(bizid, StringUtils.inputStream2String(new FileInputStream(file)));

        } else if (type == EStoreType.Ipfs) {
            throw new RuntimeException("un impl.");
        } else {
            store(bizid, file);
        }

    }


    @Override
    public void store(@NonNull String bizid, @NonNull String msg) throws Exception {
        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
        }};

        IUpchainFn fn = (bizId, hash, upchainType, state, resp) -> {

        };

        getDriver().getAccountHandler().transfer(bizid, account, "0", d.getKey(), new ExProps(), fn);
    }


    @Override
    public void store(String bizid, File file) throws Exception {
        InputStream input = new FileInputStream(file);
        store(bizid, input);
    }

    @Override
    public void store(String bizid, InputStream stream) throws Exception {
        // TODO Auto-generated method stub

    }


    @Override
    public Object getState(String bizid) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String view(String bizid) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public File download(String bizid) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean verify(String bizid, String msg) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean verify(String bizid, File file) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean verify(String bizid, InputStream stream) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }


}
