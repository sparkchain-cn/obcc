package cn.obcc.def.nonce;

import cn.obcc.stragegy.IStragegy;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName INonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  8:47
 **/
public interface INonceStrategy <D> extends IStragegy<D> {

    public Long getNonceFromChain(String address) throws Exception;

    /**
     * chainCode这里需要注意：一个链多个Client,也必须共用一个client
     *
     * @param chainCode
     * @param address
     * @return
     * @throws Exception
     */
    public Long computNonce(String chainCode, String address) throws Exception;

    public Long adjustNonce(String chainCode, String address,Long num) throws Exception;
}
