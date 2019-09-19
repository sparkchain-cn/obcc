package cn.obcc.def.contract;

import cn.obcc.stragegy.IStragegy;
import cn.obcc.vo.contract.CompileVo;
import cn.obcc.vo.db.BlockTxInfo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ICallBackNotify
 * @desc
 * @data 2019/9/3 14:45
 **/
public interface IContractParseStragegy<D> extends IStragegy<D> {

    public CompileVo compile(String sourceCode, String solcPath,
                             String tempPath, Boolean deleteTemp) throws Exception;
}
