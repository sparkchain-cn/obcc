package cn.obcc.driver.nonce;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.nonce.strategy.BizNonceStrategy;
import cn.obcc.driver.nonce.strategy.BaseChainNonceStrategy;
import cn.obcc.driver.nonce.strategy.BaseMemoryNonceStrategy;
import cn.obcc.driver.nonce.strategy.BaseRedisNonceStrategy;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.exception.enums.ENonceStrategy;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class BaseNonceCalculator<T> extends BaseHandler<T> implements INonceCalculator<T> {
    @Override
    public Long getNonce(String address, final ExConfig config) throws Exception {
        String chainCode = getObccConfig().getChain().getName();
        return getNonceStrategy().computNonce(chainCode, address);
    }

    @Override
    public Long adjustNonce(String address, Long num, ExConfig config) throws Exception {
        String chainCode = getObccConfig().getChain().getName();
        return getNonceStrategy().adjustNonce(chainCode, address, num);
    }


    protected BaseMemoryNonceStrategy baseMemoryNonceStrategy = new BaseMemoryNonceStrategy() {
        @Override
        public Long getNonceFromChain(String address) throws Exception {
            return (Long) BaseNonceCalculator.this.getNonceFromChain(address);
        }
    };

    protected BaseChainNonceStrategy baseChainNonceStrategy = new BaseChainNonceStrategy() {
        @Override
        public Long getNonceFromChain(String address) throws Exception {
            return (Long) BaseNonceCalculator.this.getNonceFromChain(address);
        }
    };


    public BaseRedisNonceStrategy getRedisNonceStrategy(BaseNonceCalculator nonceCalculator) throws Exception {
        throw new RuntimeException("你必须实现RedisNonceStrategy");
    }

    public BizNonceStrategy getBizNonceStrategy(BaseNonceCalculator nonceCalculator) throws Exception {
        throw new RuntimeException("你必须实现BizNonceStrategy");
    }

    public INonceStrategy getNonceStrategy() throws Exception {
        ENonceStrategy strategy = getObccConfig().getNonceStrategy();
        switch (strategy) {
            case Memory:
                return baseMemoryNonceStrategy;
            case Redis:
                return getRedisNonceStrategy(this);
            case Chain:
                return baseChainNonceStrategy;
            case Biz:
                return getBizNonceStrategy(this);
            default:
                return baseChainNonceStrategy;
        }

    }

}
