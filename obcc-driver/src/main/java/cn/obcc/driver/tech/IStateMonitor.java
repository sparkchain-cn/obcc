package cn.obcc.driver.tech;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.config.ExConfig;
import cn.obcc.vo.BizState;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 链的状态，交易的状态（queue,pending)
 *
 * @author mgicode
 * @desc IMonitor
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:48:45
 */
public interface IStateMonitor<T> extends IDriverHandler<T> {

    //todo:采用redis进行
    //bizId->bizId
    public static Map<String, String> BizIdMap = new ConcurrentHashMap<String, String>();

    //hash->state
    public static ExpiringMap<String, BizState> StateMap = ExpiringMap.builder()
            .variableExpiration()
            .build();

    //bizId->state
    public static ExpiringMap<String, BizState> BizStateMap = ExpiringMap.builder()
            .variableExpiration()
            .build();




    public void checkAndSetBizId(String bizId) throws Exception;

    public void checkAndsupplyBizId(String bizId) throws Exception;

    public void checkBizId(String bizId) throws Exception;

    public void setBizId(String bizId) throws Exception;

    public boolean existBizId(String bizId) throws Exception;


    public void setBizState(String bizId, BizState bizState) throws Exception;

    public BizState getBizState(String bizId) throws Exception;

    public boolean existBizState(String bizId) throws Exception;

    public void delBizState(String bizId) throws Exception;


    public void setHashState(String hash, BizState status) throws Exception;

    public BizState getHashState(String hash) throws Exception;

    public boolean existHash(String hash) throws Exception;

    public void delHashState(String hash) throws Exception;


    /**
     * pengrk created or updated at 2019年3月29日 下午3:34:51
     *
     * @param config
     * @return
     * @throws Exception
     */
    public Long getBlockHeight(ExConfig config) throws Exception;
}
