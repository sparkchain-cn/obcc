package cn.obcc.def.memo;


import cn.obcc.stragegy.IStragegy;
import cn.obcc.vo.BcMemo;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName INonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  8:47
 **/
public interface IMemoStrategy  <D> extends IStragegy<D> {
    // @Override
    public Long getMaxSize() throws Exception;

    //  @Override
    public List<BcMemo> parse(String bizId, String memo) throws Exception;

    // @Override
    public String hex(BcMemo memo) throws Exception;

    //  @Override
    public List<String> encode(String bizId, String preHex, String memo) throws Exception;

    // @Override
    public String encodeOne(String bizId, String preHex, String memo) throws Exception;

    public BcMemo decode(String memoPre, String hex) throws Exception;
}
