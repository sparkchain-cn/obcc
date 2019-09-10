package cn.obcc.driver;

import cn.obcc.driver.vo.ContractRec;
import cn.obcc.driver.vo.TokenRec;
import cn.obcc.vo.driver.TokenInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ITokenParse
 * @desc TODO
 * @date 2019/9/10 0010  8:57
 **/
public interface ITokenParse {
    public TokenRec parse(TokenInfo info, ContractRec rec) throws Exception;
}
