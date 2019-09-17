package cn.obcc.stmt;

import java.util.List;

import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.stmt.TableDefinition;

/**
 * 该类主要采用区块链来备份传统数据库中修改的履历数据
 *
 * @author mgicode
 * @desc IDbManager
 * @email 546711211@qq.com
 * @date 2019年8月22日 下午1:16:39
 */
public interface IDbStatement extends IStatement {


    /**
     * 采用区块链的Memo来定义数据表的结构
     *
     * @param tableName
     * @param define
     * @return obcc生成的Hash
     * @throws Exception
     */
    public String createTable(String tableName, TableDefinition define) throws Exception;

    /**
     * 采用合约来定义数据存储过程
     *
     * @param procedureName    存储过程名
     * @param procedureContent 存储过程文本内容
     * @param params           存储过程构建函数的参数
     * @return obcc生成的Hash
     * @throws Exception
     */
    public String createProcedure(String procedureName, String procedureContent, List<Object> params) throws Exception;


    /**
     * 把数据转成JSON或其它格式上传到区块链中对应的table中
     *
     * @param tableName 定义的表名
     * @param id        数据的id,对应数据库的表记录Id，更新采用其作为标识
     * @param data      数据
     * @return obcc生成的Hash
     * @throws Exception
     */
    public String insert(String tableName, Long id, Object data) throws Exception;

    /**
     * 更新指定ID的数据，在区块链上保存其修改的履历记录
     *
     * @param tableName 定义的表名
     * @param id        数据的id,对应数据库的表记录Id
     * @param data      需要更新的数据
     * @return obcc生成的Hash
     * @throws Exception
     */
    public String update(String tableName, Long id, Object data) throws Exception;

    /**
     * 执行存储过程
     *
     * @param bizId         确定业务唯一的编号，如订单号等，如随机生成
     * @param procedureName 存储过程
     * @param method        存储过程中方法名，对应的合约的方法名
     * @param params        存储过程中方法的参数
     * @return obcc生成的Hash
     * @throws Exception
     */
    public String exec(String bizId, String procedureName, String method, List<Object> params) throws Exception;


    /**
     * 判断表中指定Id的数据是否存在
     *
     * @param tableName
     * @param id
     * @return
     * @throws Exception
     */
    public boolean exist(String tableName, Long id) throws Exception;

    /**
     * 判断指定名称的表或存储过程是否存在
     *
     * @param type 表还是存储过程
     * @param name 表名或存储过程名
     * @return
     * @throws Exception
     */
    public boolean exist(EDbOperaType type, String name) throws Exception;

    /**
     * 表中记录修改的履历
     *
     * @param tableName 记录所在表
     * @param id        记录的标识
     * @return
     * @throws Exception
     */
    public List<RecordInfo> getUpdateRecords(String tableName, Long id) throws Exception;


    public RecordInfo recordInfo(String obccHash) throws Exception;

//    public List<Map<String, ?>> query(String sql, Object... params) throws Exception;
//
//    public <T> List<T> query(String sql, Class<T> clz, Object... params) throws Exception;
//
//    public <T> Page<T> query(String sql, Class<T> clz, long start, long size, Object... params) throws Exception;
//

}
