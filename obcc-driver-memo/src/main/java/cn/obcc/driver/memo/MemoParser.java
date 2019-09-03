package cn.obcc.driver.memo;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.memo.strategy.JsonMemoStragegy;
import cn.obcc.driver.tech.IMemoParser;
import cn.obcc.vo.BcMemo;
import cn.obcc.exception.enums.EMemoStrategy;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class MemoParser<T> extends BaseHandler<T> implements IMemoParser<T> {
    @Override
    public abstract Long getMaxSize() throws Exception;

    @Override
    public List<BcMemo> parse(String bizId, String memo) throws Exception {
        return getMemoStrategy().parse(bizId, memo);
    }

    @Override
    public String hex(BcMemo memo) throws Exception {
        return getMemoStrategy().hex(memo);
    }

    @Override
    public List<String> encode(String bizId, String memo) throws Exception {
        return getMemoStrategy().encode(bizId, memo);
    }

    @Override
    public String encodeOne(String bizId, String memo) throws Exception {
        return getMemoStrategy().encodeOne(bizId, memo);
    }

    @Override
    public BcMemo decode(String hex) throws Exception {
        return getMemoStrategy().decode(getObccConfig().getMemoPre(), hex);
    }

    public IMemoStrategy getMemoStrategy() throws Exception {
        EMemoStrategy strategy = getObccConfig().getMemoStrategy();
        switch (strategy) {
            case JSON:
                return jsonStragegy;

            default:
                return jsonStragegy;
        }

    }

    protected JsonMemoStragegy jsonStragegy = new JsonMemoStragegy() {
        @Override
        public Long getMaxSize() throws Exception {
            return this.getMaxSize();
        }
    };

}
