package cn.obcc.driver.eth.module;



import cn.obcc.contract.solc.utils.AbiFuncUtils;
import cn.obcc.driver.eth.module.token.DefaultSolToken;
import cn.obcc.driver.handler.ITokenHandler;
import cn.obcc.driver.handler.base.BaseTokenHandler;
import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.vo.contract.FuncVo;
import cn.obcc.vo.db.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName EthTokenHandler
 * @desc TODO
 * @date 2019/8/24 0024  18:54
 **/
public class EthTokenHandler extends BaseTokenHandler<Web3j> implements ITokenHandler<Web3j> {
    public static final Logger logger = LoggerFactory.getLogger(EthTokenHandler.class);

    @Override
    public String getDefaultTokenContents() throws Exception {
        return DefaultSolToken.DEFAULT_TOKEN_STR;
    }

    @Override
    protected boolean check(TokenInfo token, String methodName, List<Object> params) throws Exception {
        if (token == null) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "token params is null");
            // return false;
        }

        boolean flag = AbiFuncUtils.exist(token.getContractAbi(), methodName);
        if (flag == false) {
            throw ObccException.create(EExceptionCode.CONTENT_NOT_FOUND,
                    "合约{0}中名称为{1}方法没有找到.", token.getContractAddress(), methodName);
            //return false;
        }

        FuncVo vo = AbiFuncUtils.getFuncVo(token.getContractAbi(), methodName);
        List<String> names = AbiFuncUtils.getInputNames(vo);
        // List<String> names = AbiFormatParser.getFunctionInputNames(token.getContractAbi(), methodName);
        //参数为空的check
        if ((names == null || names.size() == 0) && (params == null || params.size() == 0)) {
            return true;
        }

        if (names.size() != params.size()) {
            throw ObccException.create(EExceptionCode.CONTENT_NOT_FOUND,
                    "参数实际个数为{0}，而需要的个数为{1}.", params.size(), names.size());
        }
        return true;
    }

}
