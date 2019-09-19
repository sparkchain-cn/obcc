package cn.obcc.state.redis;

import cn.obcc.def.state.IBizIdCheckStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.vo.db.RecordInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName RedisNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  9:37
 **/
public class BizIdCheckRedisStrategy extends BaseStrategy<IChainDriver> implements IBizIdCheckStrategy<IChainDriver> {
    public static final Logger logger = LoggerFactory.getLogger(BizIdCheckRedisStrategy.class);
    //todo:采用redis进行
    //bizId->bizId
    public static Map<String, String> BizIdMap = new ConcurrentHashMap<String, String>();


    @Override
    public void setBizId(String bizId) throws Exception {
        BizIdMap.put(bizId, bizId);
    }

    @Override
    public boolean existBizId(String bizId) throws Exception {
        return BizIdMap.containsKey(bizId);
    }

    @Override
    public void onAfterInit() throws Exception {
        RecordInfo r;
        List<String> list = driver.getLocalDb().
                getRecordInfoDao().getValues(" select biz_id from record_info ", new Object[]{});
        list.stream().forEach((s) -> {
            BizIdMap.put(s, s);
        });
    }

}
