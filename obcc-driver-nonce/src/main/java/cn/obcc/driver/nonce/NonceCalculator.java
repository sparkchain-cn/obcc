package cn.obcc.driver.nonce;

import cn.obcc.config.ExProps;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.nonce.strategy.BizNonceStrategy;
import cn.obcc.driver.nonce.strategy.ChainNonceStrategy;
import cn.obcc.driver.nonce.strategy.MemoryNonceStrategy;
import cn.obcc.driver.nonce.strategy.RedisNonceStrategy;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.exception.enums.ENonceStrategy;
import cn.obcc.vo.RetData;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class NonceCalculator<T> extends BaseHandler<T> implements INonceCalculator<T> {
    @Override
    public Long getNonce(String address, final ExProps config) throws Exception {
        String chainCode = getObccConfig().getChain().getName();
        return getNonceStrategy().computNonce(chainCode, address);
    }

    public Long adjustNonce(String address, Long num, ExProps config) throws Exception {
        String chainCode = getObccConfig().getChain().getName();
        return getNonceStrategy().adjustNonce(chainCode, address, num);
    }


    protected MemoryNonceStrategy memoryNonceStrategy = new MemoryNonceStrategy() {
        @Override
        public Long getNonceFromChain(String address) throws Exception {
            return (Long) NonceCalculator.this.getNonceFromChain(address);
        }
    };

    protected ChainNonceStrategy chainNonceStrategy = new ChainNonceStrategy() {
        @Override
        public Long getNonceFromChain(String address) throws Exception {
            return (Long) NonceCalculator.this.getNonceFromChain(address);
        }
    };


    public RedisNonceStrategy getRedisNonceStrategy(NonceCalculator nonceCalculator) throws Exception {
        throw new RuntimeException("你必须实现RedisNonceStrategy");
    }

    public BizNonceStrategy getBizNonceStrategy(NonceCalculator nonceCalculator) throws Exception {
        throw new RuntimeException("你必须实现BizNonceStrategy");
    }

    public INonceStrategy getNonceStrategy() throws Exception {
        ENonceStrategy strategy = getObccConfig().getNonceStrategy();
        switch (strategy) {
            case Memory:
                return memoryNonceStrategy;
            case Redis:
                return getRedisNonceStrategy(this);
            case Chain:
                return chainNonceStrategy;
            case Biz:
                return getBizNonceStrategy(this);
            default:
                return chainNonceStrategy;
        }

    }

}
