package cn.obcc.driver.vo;


import cn.obcc.config.ExConfig;
import cn.obcc.listener.IStateListener;
import cn.obcc.vo.BizState;
import cn.obcc.vo.driver.RecordInfo;
import lombok.Data;

/**
 * 生命周期从请求到hash返回的上链通道
 *
 * @author mgicode
 * @version 1.0
 * @ClassName ChainPipe
 * @desc
 * @data 2019/9/5 17:57
 **/
@Data
public class ChainPipe {

    private BizState bizState = new BizState();

    private FromAccount fromAccount;
    private String destAddr;
    private String contractAddr;
    private String amount = "0";
    private ExConfig exConfig = new ExConfig();
    private RecordInfo recordInfo = new RecordInfo();

    private IStateListener stateListener = (bizState, resp) -> {
    };


    public void setStateListener(IStateListener stateListener) {
        if (stateListener == null) {
            return;
        }
        this.stateListener = stateListener;
    }

    public ChainPipe copy() {

        ChainPipe pipe = new ChainPipe();
        pipe.setBizState(this.getBizState());//没变

        pipe.setFromAccount(new FromAccount() {{//变化
            FromAccount now = ChainPipe.this.getFromAccount();
            setSrcAddr(now.getSrcAddr());
            setSecret(now.getSecret());
            setNonce(now.getNonce());
            setMemos(now.getMemos());
            setGasLimit(now.getGasLimit());
            setGasPrice(now.getGasPrice());
        }});
        pipe.setDestAddr(destAddr);
        pipe.setContractAddr(contractAddr);
        pipe.setAmount(amount);

        pipe.setExConfig(exConfig); //没变
        pipe.setStateListener(stateListener); //没变

        return pipe;
    }
}
