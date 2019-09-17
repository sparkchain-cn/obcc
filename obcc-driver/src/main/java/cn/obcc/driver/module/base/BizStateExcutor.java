package cn.obcc.driver.module.base;

import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.vo.BizState;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizStateExcutor
 * @desc TODO
 * @date 2019/9/17 0017  16:44
 **/
public class BizStateExcutor {
    public static void setBizState(IChainDriver driver, ChainPipe pipe, String hash, ETransferStatus status) throws Exception {
        String recId = pipe.getConfig().getRecordInfo().getId() + "";
        BizState bizState = new BizState(pipe.getBizId(), hash, recId, status);
        driver.getStateMonitor().setBizState(pipe.getBizId(), bizState);
    }

    public static void setHashState(IChainDriver driver, ChainPipe pipe, String hash, ETransferStatus status) throws Exception {
        String recId = pipe.getConfig().getRecordInfo().getId() + "";
        BizState bizState = new BizState(pipe.getBizId(), hash, recId, status);
        driver.getStateMonitor().setBizState(hash, bizState);
    }

}
