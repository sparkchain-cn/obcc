package cn.obcc.stmt.db.operate;

import cn.obcc.config.ExProps;
import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.exception.enums.EStmtType;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.driver.TableInfo;
import cn.obcc.vo.stmt.TableDefinition;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName TableBuilder
 * @desc TODO
 * @date 2019/9/17 0017  17:47
 **/
public class TableBuilder {

    public static void saveTableInfo(IChainDriver driver, String db, String tableName, TableDefinition define) throws Exception {
        driver.getLocalDb().getTableInfoDao().add(new TableInfo() {{
            setId(UuidUtils.get());
            setDbName(db);
            setType(EDbOperaType.Table);
            setName(tableName);
            setContents(define.toJson());
        }});
    }


    public static void saveProcInfo(IChainDriver driver, String db, String name, String content) throws Exception {
        //保存到数据库中，高并发会出现判断的并发问题
        driver.getLocalDb().getTableInfoDao().add(new TableInfo() {{
            setId(UuidUtils.get());
            setDbName(db);
            setType(EDbOperaType.Procedure);
            setName(name);
            setContents(content);
        }});
    }


    public static String doCreateTable(IChainDriver driver, ObccConfig config, String bizId, TableDefinition define) throws Exception {

        KeyValue<String> v = config.getStorageSrcAccount();
        KeyValue<String> d = config.getStorageDestAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
            setMemos(define.toJson());
        }};
        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.Db_CREATE_TABLE);
            }});
        }};
        return driver.getAccountHandler().transfer(bizId, account, "0", d.getKey(), exProps, null);
    }


    public static String doCreateProc(IChainDriver driver, ObccConfig config, String bizId, String name, String content, List<Object> params) throws Exception {

        driver.getContractHandler().compile(bizId, content, new ExProps());
        ContractInfo ci = driver.getContractHandler().getContract(bizId, name);

        KeyValue<String> v = config.getStorageSrcAccount();
        SrcAccount account = new SrcAccount() {{
            setSrcAddr(v.getKey());
            setSecret(v.getVal());
        }};

        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.Db_CREATE_PROC);
            }});
        }};
        return driver.getContractHandler().deploy(bizId, account, ci, null, exProps, params);
    }

}
