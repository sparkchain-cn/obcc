package cn.obcc.def.token;

import cn.obcc.stragegy.IStragegy;
import cn.obcc.vo.contract.ContractRec;
import cn.obcc.vo.contract.TokenRec;
import cn.obcc.vo.driver.TokenInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ITokenParse
 * @desc TODO
 * @date 2019/9/10 0010  8:57
 **/
public interface ITokenParser <D> extends IStragegy<D> {

    public TokenRec parse(TokenInfo info, ContractRec rec) throws Exception;
}
