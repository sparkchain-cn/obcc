package cn.obcc.driver.module.excutor;

import cn.obcc.driver.vo.ChainPipe;
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

    public static void call(ChainPipe pipe, Object resp) throws Exception {
        try {
            pipe.getStateListener().exec(pipe.getBizState(), resp);
        } catch (Exception e) {
            logger.error("state callback error. {}", e.getMessage());
        }
    }

}
