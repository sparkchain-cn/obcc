package cn.obcc.stmt;

import java.io.File;
import java.io.InputStream;

import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.exception.enums.EStoreType;
import cn.obcc.stmt.fn.IUploadFn;
import cn.obcc.stmt.fn.IVerifyFn;
import cn.obcc.vo.RetData;

/**
 * @author mgicode
 * @desc IStorageManager
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午11:58:11
 */
public interface IStorageStatement extends IStatement {

    public void store(String bizid, String msg) throws Exception;

    public void store(String bizid, String msg, EStoreType type) throws Exception;

    public void store(String bizid, File file) throws Exception;

    public void store(String bizid, InputStream stream) throws Exception;

    public void store(String bizid, File file, EStoreType type) throws Exception;


    /*******************************************************************/

    public BizTxInfo getState(String bizid) throws Exception;

    public String view(String bizid) throws Exception;

    public InputStream download(String bizid) throws Exception;

    /*******************************************************************/

    public boolean verifyMsg(String bizid, String msg) throws Exception;

    public boolean verifyFile(String bizid, String sha1) throws Exception;


}
