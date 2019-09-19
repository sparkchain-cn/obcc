package cn.obcc.token.stardard;

import cn.obcc.def.token.ITokenParser;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.utils.ConvertUtils;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.vo.contract.ContractRec;
import cn.obcc.vo.contract.TokenRec;
import cn.obcc.vo.driver.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName DefaultTokenParse
 * @desc TODO
 * @date 2019/9/10 0010  9:04
 **/
public class StardardTokenParser extends BaseStrategy<IChainDriver> implements ITokenParser<IChainDriver> {

    public static final Logger logger = LoggerFactory.getLogger(StardardTokenParser.class);

    @Override
    public TokenRec parse(TokenInfo info, ContractRec rec) throws Exception {
        TokenRec tokenRec = new TokenRec();
        switch (rec.getMethod()) {
            case "transfer":
                tokenRec.setMethod(rec.getMethod());
                if (rec.getParams() != null && rec.getParams().size() > 1) {
                    tokenRec.setDestAddr((String) rec.getParams().get(0).getVal());
                    BigInteger amountBint = (BigInteger) rec.getParams().get(1).getVal();

                    //tokenRec.setAmount(ConvertUtils.toBigStr(amountBint, info.getPrecisions()));
                    tokenRec.setAmount(ConvertUtils.toBigStr(amountBint, info.getPrecisions()));
                }

                break;
            case "burn":
                logger.error("no impl.");
                break;
            case "supply":
                logger.error("no impl.");
                break;
            case "approve":
                logger.error("no impl.");
                break;
            case "transferFrom":
                logger.error("no impl.");
                break;
        }

        return tokenRec;

    }


}
