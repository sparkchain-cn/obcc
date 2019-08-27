package cn.obcc.driver.eth.module.tech;

import cn.obcc.driver.memo.MemoParser;
import cn.obcc.driver.vo.BcMemo;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.tech.IMemoParser;

import java.util.List;

public class EthMemoParser extends MemoParser<Web3j> implements IMemoParser<Web3j> {

    @Override
    public Long getMaxSize() throws Exception {
        //28K
        return 28 * 1024 * 1024L;
    }

}
