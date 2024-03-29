package cn.obcc.driver.handler;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.listener.IStateListener;
import cn.obcc.vo.driver.SignTxParams;
import cn.obcc.vo.driver.TxParams;
import cn.obcc.vo.driver.SignTxData;
import cn.obcc.config.ExConfig;

/**
 * @author mgicode
 * @desc ISignedTx
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:06:21
 */
public interface ISignatureHandler<T> extends IDriverHandler<T> {

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
                                           String amount, ExConfig config) throws Exception;

    /**
     * @param transInfo
     * @param config
     * @return
     * @throws Exception
     */
    public SignTxData getData(TxParams transInfo, ExConfig config) throws Exception;

    /**
     * 签名的数据提交
     * <p>
     * pengrk created or updated at 2019年1月18日 下午3:04:31
     *
     * @param signedData
     * @param config
     * @return
     */
    public String sendData(String sourceAddress, String signedData, ExConfig config)
            throws Exception;

    /**
     * @param sourceAddress
     * @param signedData
     * @param config
     * @param callback
     * @return
     * @throws Exception
     */
    public String sendData(String sourceAddress, String signedData, ExConfig config,
                                    IStateListener callback) throws Exception;

}
