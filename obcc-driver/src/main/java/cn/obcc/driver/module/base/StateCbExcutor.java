package cn.obcc.driver.module.base;

import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.vo.BizState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizStateExcutor
 * @desc TODO
 * @date 2019/9/17 0017  16:44
 **/
public class StateCbExcutor {
    public static final Logger logger = LoggerFactory.getLogger(StateCbExcutor.class);

    public static void call(ChainPipe pipe, String hash, ETransferStatus status, Object resp) throws Exception {
        try {
            pipe.getCallbackFn().exec(pipe.getBizId(), hash,
                    pipe.getConfig().getUpchainType(), status, resp);
        } catch (Exception e) {
            logger.error("state callback error. {}", e.getMessage());
        }
    }

}
