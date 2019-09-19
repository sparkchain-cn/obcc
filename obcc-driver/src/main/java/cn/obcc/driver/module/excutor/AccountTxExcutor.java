package cn.obcc.driver.module.excutor;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.module.base.BaseAccountHandler;
import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.RecordInfo;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountBaseTx
 * @desc TODO
 * @date 2019/9/7 0007  17:41
 **/
public class AccountTxExcutor {

    public static BizTxInfo getTxByHashs(@NotEmpty String hashs, ExConfig config, BaseAccountHandler baseAccountHandler) throws Exception {
        if (StringUtils.isNullOrEmpty(hashs)) {return null;}
        List<BlockTxInfo> list = new ArrayList<>();

        BizTxInfo bizTx = new BizTxInfo();
        bizTx.setHashs(hashs);
        StringBuffer sb = new StringBuffer();
        Arrays.stream(hashs.split("[,，]")).forEach((s) -> {
            try {
                BlockTxInfo tx = baseAccountHandler.getTxByHash(hashs, config);
                // BlockTxInfo tx = (BlockTxInfo) retetData();
                sb.append(tx.getMemosObj().getData());
                //todo:判断多个hash的流水的bizid是否相同
                bizTx.setBizId(tx.getMemosObj().getBizId());
                bizTx.setSourceAddress(tx.getSrcAddr());
                bizTx.setDestinationAddress(tx.getDestAddr());
                list.add(tx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        bizTx.setMemos(sb.toString());
        bizTx.setRecordInfos(list);
        bizTx.setState(1 + "");
        return bizTx;
    }


    public static BizTxInfo getTxByBizId(String bizId, ExConfig config, IChainDriver driver, BaseAccountHandler baseAccountHandler) throws Exception {
        RecordInfo recordInfo = driver.getLocalDb().getRecordInfoDao().get("biz_id=?", new Object[]{bizId});
        if (recordInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "根据BizId:" + bizId + "不能找到对应的hash");
        }
        BizTxInfo bizInfos = getTxByHashs(recordInfo.getHashes(), config, baseAccountHandler);
        return bizInfos;
    }
}
