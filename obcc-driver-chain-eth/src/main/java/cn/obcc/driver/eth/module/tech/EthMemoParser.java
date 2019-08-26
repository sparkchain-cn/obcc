package cn.obcc.driver.eth.module.tech;

import cn.obcc.driver.vo.BcMemo;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.tech.IMemoParser;

public class EthMemoParser extends BaseHandler<Web3j> implements IMemoParser<Web3j> {



    @Override
    public RetData<String> encode(SrcAccount transInfo, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<BcMemo> decode(SrcAccount transInfo, ReqConfig<Web3j> config) throws Exception {
        return null;
    }
}
