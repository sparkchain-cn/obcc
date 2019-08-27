package cn.obcc.stmt;

import java.util.List;
import java.util.Map;

import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.vo.stmt.Procedure;
import cn.obcc.vo.stmt.Table;
import cn.obcc.vo.stmt.TableDefine;
import cn.obcc.vo.Page;
import cn.obcc.vo.RetData;

/**
 * 
 * @desc IDbManager
 * @author mgicode
 * @email 546711211@qq.com
 * @date 2019年8月22日 下午1:16:39
 *
 */
public interface IDbStatement extends IStatement {

	/*********************************************************************/

	public RetData<Table> createTable(String tableName, TableDefine define) throws Exception;

	public RetData<Procedure> createProcedure(String procedureName, String code) throws Exception;

	/*********************************************************************/

	public RetData<Object> insert(String tableName, String id, String data) throws Exception;

	public RetData<Object> update(String tableName, String id, String data) throws Exception;

	public RetData<Object> exec(String procedureName, String method, Object... params) throws Exception;


	/*******************************************************************/
	public boolean exist(String tableName, String id) throws Exception;

	public boolean exist(EDbOperaType type, String name) throws Exception;

	/******************************************************************************/

	public List<Object> getRecordVersions(String tablename, String id) throws Exception;

	public Page<Object> getRecordVersions(String tablename, String id, long start, long size) throws Exception;

	/*******************************************************************************************/
	public List<Map<String, ?>> query(String sql, Object... params) throws Exception;

	public <T> List<T> query(String sql, Class<T> clz, Object... params) throws Exception;

	public <T> Page<T> query(String sql, Class<T> clz, long start, long size, Object... params) throws Exception;

	/******************************************************************************/
}
