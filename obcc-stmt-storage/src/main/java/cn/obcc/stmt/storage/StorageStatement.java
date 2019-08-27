package cn.obcc.stmt.storage;

import java.io.File;
import java.io.InputStream;

import cn.obcc.exception.enums.EStoreType;
import cn.obcc.stmt.IStorageStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.fn.IUploadFn;
import cn.obcc.stmt.fn.IVerifyFn;
import cn.obcc.vo.RetData;

public class StorageStatement extends BaseStatement implements IStorageStatement {


	@Override
	public void store(String bizid, String msg) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String bizid, String msg, EStoreType type) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String bizid, File file) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String bizid, InputStream stream) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String bizid, File file, EStoreType type) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void asyncStore(String bizid, String msg, IUploadFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void asyncStore(String bizid, String msg, EStoreType type, IUploadFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void asyncStore(String bizid, File file, IUploadFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void asyncStore(String bizid, InputStream stream, IUploadFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void asyncStore(String bizid, File file, EStoreType type, IUploadFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public RetData<Object> getState(String bizid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<String> view(String bizid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<File> download(String bizid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<Boolean> verify(String bizid, String msg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<Boolean> verify(String bizid, File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<Boolean> verify(String bizid, InputStream stream) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void asyncVerify(String bizid, String msg, IVerifyFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void asyncVerify(String bizid, File file, IVerifyFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void asyncVerify(String bizid, InputStream stream, IVerifyFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

}
