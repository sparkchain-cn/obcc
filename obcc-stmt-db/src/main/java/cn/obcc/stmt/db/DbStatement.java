package cn.obcc.stmt.db;

import java.util.List;
import java.util.Map;

import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.stmt.IDbStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.module.db.Procedure;
import cn.obcc.stmt.module.db.Table;
import cn.obcc.stmt.module.db.TableDefine;
import cn.obcc.vo.Page;
import cn.obcc.vo.RetData;

public class DbStatement  extends BaseStatement implements IDbStatement {

	
	@Override
	public RetData<Table> CreateTable(String tableName, TableDefine define) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<Procedure> createProcedure(String procedureName, String code) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<Object> insert(String tableName, String id, String data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<Object> update(String tableName, String id, String data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetData<Object> exec(String procedureName, String method, Object... params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exist(String tableName, String id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exist(EDbOperaType type, String name) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Object> getRecordVersions(String tablename, String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Object> getRecordVersions(String tablename, String id, long start, long size) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, ?>> query(String sql, Object... params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> query(String sql, Class<T> clz, Object... params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Page<T> query(String sql, Class<T> clz, long start, long size, Object... params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

}
