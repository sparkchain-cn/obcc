package cn.obcc.driver.eth.module.tech;

import cn.obcc.driver.memo.MemoParser;
import org.web3j.protocol.Web3j;

import cn.obcc.driver.tech.IMemoParser;

public class EthMemoParser extends MemoParser<Web3j> implements IMemoParser<Web3j> {

    @Override
    public Long getMaxSize() throws Exception {
        //28K
        return 28 * 1024 * 1024L;
    }

}
