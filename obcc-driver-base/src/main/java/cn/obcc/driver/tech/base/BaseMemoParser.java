package cn.obcc.driver.tech.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.utils.BeanUtils;
import cn.obcc.def.memo.IMemoStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IDriverHandler;
import cn.obcc.driver.base.BaseDriverHandler;
import cn.obcc.driver.tech.IMemoParser;
import cn.obcc.vo.BcMemo;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class BaseMemoParser<T> extends BaseDriverHandler<T> implements IMemoParser<T> {
    @Override
    public abstract Long getMaxSize() throws Exception;

    IMemoStrategy memoStrategy;

    @Override
    public IDriverHandler init(ObccConfig config, IChainDriver<T> driver) throws Exception {
        super.init(config, driver);
        memoStrategy = (IMemoStrategy) BeanUtils.newInstance(config.getMemoStrategyClzName());
        return this;
    }

    @Override
    public List<BcMemo> parse(String bizId, String memo) throws Exception {
        return memoStrategy.parse(bizId, memo);
    }

    @Override
    public String hex(BcMemo memo) throws Exception {
        return memoStrategy.hex(memo);
    }

    @Override
    public List<String> encode(String bizId, String memo) throws Exception {
        String preHex = getConfig().getMemoPreHex();
        return memoStrategy.encode(bizId, preHex, memo);
    }

    @Override
    public String encodeOne(String bizId, String memo) throws Exception {
        String preHex = getConfig().getMemoPreHex();
        return memoStrategy.encodeOne(bizId, preHex, memo);
    }

    @Override
    public BcMemo decode(String hex) throws Exception {
        return memoStrategy.decode(getConfig().getMemoPre(), hex);
    }


}
