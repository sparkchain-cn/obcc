package cn.obcc.driver.module.base;

import cn.obcc.config.ExProps;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
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
public class AccountBaseTx {


    public static BizTxInfo getTxByHashs(@NotEmpty String hashs, ExProps config, AccountBaseHandler accountBaseHandler) throws Exception {
        if (StringUtils.isNullOrEmpty(hashs)) return null;
        List<BlockTxInfo> list = new ArrayList<>();

        BizTxInfo bizTx = new BizTxInfo();
        bizTx.setHashs(hashs);
        StringBuffer sb = new StringBuffer();
        Arrays.stream(hashs.split("[,，]")).forEach((s) -> {
            try {
                BlockTxInfo tx = accountBaseHandler.getTxByHash(hashs, config);
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


    public static BizTxInfo getTxByBizId(String bizId, ExProps config, IChainDriver driver, AccountBaseHandler accountBaseHandler) throws Exception {
        RecordInfo recordInfo = driver.getLocalDb().getRecordInfoDao().findOne("biz_id=?", new Object[]{bizId});
        if (recordInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "根据BizId:" + bizId + "不能找到对应的hash");
        }
        BizTxInfo bizInfos = getTxByHashs(recordInfo.getHashs(), config, accountBaseHandler);
        return bizInfos;
    }
}
