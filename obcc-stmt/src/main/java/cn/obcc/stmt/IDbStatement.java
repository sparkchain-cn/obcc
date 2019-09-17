package cn.obcc.stmt;

import java.util.List;

import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.stmt.TableDefine;

/**
 * @author mgicode
 * @desc IDbManager
 * @email 546711211@qq.com
 * @date 2019年8月22日 下午1:16:39
 */
public interface IDbStatement extends IStatement {

    /*********************************************************************/

    public String createTable(String tableName, TableDefine define) throws Exception;

    public String createProcedure(String procedureName, String procedureContent, List<String> params) throws Exception;

    /*********************************************************************/

    public String insert(String tableName, Long id, Object data) throws Exception;

    public String update(String tableName, Long id, Object data) throws Exception;

    public String exec(String bizId, String procedureName, String method, List<String> params) throws Exception;


    /*******************************************************************/
    public boolean exist(String tableName, Long id) throws Exception;

    public boolean exist(EDbOperaType type, String name) throws Exception;

    /******************************************************************************/

    public List<RecordInfo> getRecordVersions(String tablename, Long id) throws Exception;

//    /*******************************************************************************************/
//    public List<Map<String, ?>> query(String sql, Object... params) throws Exception;
//
//    public <T> List<T> query(String sql, Class<T> clz, Object... params) throws Exception;
//
//    public <T> Page<T> query(String sql, Class<T> clz, long start, long size, Object... params) throws Exception;
//
//    /******************************************************************************/
}
