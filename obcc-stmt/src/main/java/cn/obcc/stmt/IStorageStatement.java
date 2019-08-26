package cn.obcc.stmt;

import java.io.File;
import java.io.InputStream;

import cn.obcc.exception.enums.EStoreType;
import cn.obcc.stmt.fn.IUploadFn;
import cn.obcc.stmt.fn.IVerifyFn;
import cn.obcc.vo.RetData;

/**
 * 
 * @desc IStorageManager
 * @author mgicode
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午11:58:11
 *
 */
public interface IStorageStatement extends IStatement {

	public void store(String bizid, String msg) throws Exception;

	public void store(String bizid, String msg, EStoreType type) throws Exception;

	public void store(String bizid, File file) throws Exception;

	public void store(String bizid, InputStream stream) throws Exception;

	public void store(String bizid, File file, EStoreType type) throws Exception;

	/******************************************************************/

	public void asyncStore(String bizid, String msg, IUploadFn fn) throws Exception;

	public void asyncStore(String bizid, String msg, EStoreType type, IUploadFn fn) throws Exception;

	public void asyncStore(String bizid, File file, IUploadFn fn) throws Exception;

	public void asyncStore(String bizid, InputStream stream, IUploadFn fn) throws Exception;

	public void asyncStore(String bizid, File file, EStoreType type, IUploadFn fn) throws Exception;

	/*******************************************************************/

	public RetData<Object> getState(String bizid) throws Exception;

	public RetData<String> view(String bizid) throws Exception;

	public RetData<File> download(String bizid) throws Exception;

	/*******************************************************************/

	public RetData<Boolean> verify(String bizid, String msg) throws Exception;

	public RetData<Boolean> verify(String bizid, File file) throws Exception;

	public RetData<Boolean> verify(String bizid, InputStream stream) throws Exception;

	public void asyncVerify(String bizid, String msg, IVerifyFn fn) throws Exception;

	public void asyncVerify(String bizid, File file, IVerifyFn fn) throws Exception;

	public void asyncVerify(String bizid, InputStream stream, IVerifyFn fn) throws Exception;

}
