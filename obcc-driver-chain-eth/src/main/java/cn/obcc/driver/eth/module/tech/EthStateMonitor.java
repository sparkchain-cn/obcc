package cn.obcc.driver.eth.module.tech;



import cn.obcc.driver.state.BaseStateMonitor;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.config.ExProps;
import org.web3j.protocol.Web3j;

public class EthStateMonitor extends BaseStateMonitor<Web3j> implements IStateMonitor<Web3j> {

    @Override
    public Long getBlockHeight(ExProps config) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
