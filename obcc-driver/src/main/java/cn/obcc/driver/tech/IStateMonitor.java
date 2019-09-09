package cn.obcc.driver.tech;

import cn.obcc.driver.IChainHandler;
import cn.obcc.config.ExProps;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.vo.BizState;
import cn.obcc.vo.RetData;
import net.jodah.expiringmap.ExpiringMap;

/**
 * 链的状态，交易的状态（queue,pending)
 *
 * @author mgicode
 * @desc IMonitor
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:48:45
 */
public interface IStateMonitor<T> extends IChainHandler<T> {

    //hash->state
    public static ExpiringMap<String, BizState> StateMap = ExpiringMap.builder()
            .variableExpiration()
            .build();

    public static ExpiringMap<String, BizState> BizStateMap = ExpiringMap.builder()
            .variableExpiration()
            .build();

    public void setBizState(String bizId, BizState bizState) throws Exception;

    public BizState getBizState(String bizId) throws Exception;

    public boolean existBizState(String bizId) throws Exception;

    public void delBizState(String bizId) throws Exception;

    public void setState(String hash, BizState status) throws Exception;

    public BizState getState(String hash) throws Exception;

    public boolean exist(String hash) throws Exception;

    public void delState(String hash) throws Exception;


    /**
     * pengrk created or updated at 2019年3月29日 下午3:34:51
     *
     * @param config
     * @return
     * @throws Exception
     */
    public Long getBlockHeight(ExProps config) throws Exception;
}
