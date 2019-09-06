package cn.obcc.driver.tech;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.params.SignTxParams;
import cn.obcc.driver.vo.params.TxParams;
import cn.obcc.driver.vo.SignTxData;
import cn.obcc.config.ExProps;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author mgicode
 * @desc ISignedTx
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:06:21
 */
public interface ITxSignatureHandler<T> extends IChainHandler<T> {

    /**
     * 获取签名的参数，这里就是返回sequence
     * <p>
     * pengrk created or updated at 2019年1月18日 下午3:04:02
     *
     * @param sourceAddress
     * @param config
     * @return
     */
    public SignTxParams getParams(String sourceAddress, String destinationAddress, String currency,
                                           String amount, ExProps config) throws Exception;

    /**
     * @param transInfo
     * @param config
     * @return
     * @throws Exception
     */
    public SignTxData getData(TxParams transInfo, ExProps config) throws Exception;

    /**
     * 签名的数据提交
     * <p>
     * pengrk created or updated at 2019年1月18日 下午3:04:31
     *
     * @param signedData
     * @param config
     * @return
     */
    public String sendData(String sourceAddress, String signedData, ExProps config)
            throws Exception;

    /**
     * @param sourceAddress
     * @param signedData
     * @param config
     * @param callback
     * @return
     * @throws Exception
     */
    public String sendData(String sourceAddress, String signedData, ExProps config,
                                    IUpchainFn<BlockTxInfo> callback) throws Exception;

}
