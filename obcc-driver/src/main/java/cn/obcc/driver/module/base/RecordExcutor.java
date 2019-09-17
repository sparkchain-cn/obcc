package cn.obcc.driver.module.base;

import cn.obcc.db.dao.RecordInfoDaoBase;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.driver.TxConSensus;
import cn.obcc.vo.driver.TxRecv;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName RecordExcutor
 * @desc TODO
 * @date 2019/9/17 0017  13:14
 **/
public class RecordExcutor {

    public static void saveRecord(ChainPipe pipe, IChainDriver driver) throws Exception {

        if (pipe.getConfig().getRecordInfo() == null) {
            pipe.getConfig().setRecordInfo(new RecordInfo());
        }
        RecordInfo recordInfo = pipe.getConfig().getRecordInfo();
        recordInfo.setId(UuidUtils.get());

        //ETransferStatus.STATE_WAIT
        recordInfo.setBizId(pipe.getBizId());
        recordInfo.setChainCode(pipe.getChainCode());
        recordInfo.setMemo(pipe.getSrcAccount().getMemos());

        recordInfo.setSrcAccount(pipe.getSrcAccount().getSrcAddr());
        recordInfo.setDestAccount(pipe.getDestAddr());
        recordInfo.setContractAddr(pipe.getContractAddr());
        recordInfo.setAmount(pipe.getAmount());
        recordInfo.setState(ETransferStatus.STATE_WAIT);

        driver.getLocalDb().getRecordInfoDao().add(recordInfo);

    }

    public static void chainRecvUpdate(RecordInfo r, IChainDriver driver) throws Exception {
        RecordInfoDaoBase recordInfoDao = driver.getLocalDb().getRecordInfoDao();
        if (r.getTxSize() == 1) {
            TxRecv txRecv = r.getTxRecvList().get(0);
            r.setGasLimit(txRecv.getGasLimit());
            r.setGasPrice(txRecv.getGasPrice());
            r.setNonce(txRecv.getNonce());
            r.setErrorCode(txRecv.getErrorCode());
            r.setErrorMsg(txRecv.getErrorMsg());

            r.setTxRecvJson(JSON.toJSONString(r.getTxRecvList()));

            recordInfoDao.update(r.getId(), new HashMap<String, Object>() {{
                put("txSize", 1);
                put("gasLimit", r.getGasLimit());
                put("gasPrice", r.getGasPrice());
                put("nonce", r.getNonce());
                put("errorCode", r.getErrorCode());
                put("errorMsg", r.getErrorMsg());
                put("txRecvJson", r.getTxRecvJson());
                put("state", ETransferStatus.STATE_CHAIN_ACCEPT);
            }});
        } else {
            r.setTxRecvJson(JSON.toJSONString(r.getTxRecvList()));
            recordInfoDao.update(r.getId(), new HashMap<String, Object>() {{
                put("txSize", r.getTxSize());
                put("txRecvJson", r.getTxRecvJson());
                put("state", ETransferStatus.STATE_CHAIN_ACCEPT);
            }});
        }

    }

    public static void consenseUpdate(BlockTxInfo txInfo, IChainDriver driver) throws Exception {

        String recId = txInfo.getRecId();
        RecordInfoDaoBase recordInfoDao = driver.getLocalDb().getRecordInfoDao();
        if (txInfo.isSingle()) {
            List<TxConSensus> list = new ArrayList<>();
            TxConSensus cs = new TxConSensus() {{
                setHash(txInfo.getHash());
                setGasUsed(txInfo.getGasUsed());
                setChainTxState(txInfo.getState());
                setBlockNumber(txInfo.getBlockNumber());
            }};
            list.add(cs);
            recordInfoDao.update(Long.parseLong(recId), new HashMap<String, Object>() {{
                put("gasUsed", cs.getGasUsed());
                put("chainTxState", cs.getChainTxState());
                put("blockNumber", cs.getBlockNumber());
                put("consensusJson", JSON.toJSONString(list));
                put("state", ETransferStatus.STATE_CHAIN_CONSENSUS);
            }});
        } else {
            List<TxConSensus> list = recordInfoDao.getById(Long.parseLong(recId)).getComputedConSensusList();
            TxConSensus cs = new TxConSensus() {{
                setHash(txInfo.getHash());
                setGasUsed(txInfo.getGasUsed());
                setChainTxState(txInfo.getState());
                setBlockNumber(txInfo.getBlockNumber());
            }};
            list.add(cs);
            if (txInfo.isLast()) {
                recordInfoDao.update(Long.parseLong(recId), new HashMap<String, Object>() {{
                    put("consensusJson", JSON.toJSONString(list));
                    put("state", ETransferStatus.STATE_CHAIN_CONSENSUS);
                }});
            } else {
                recordInfoDao.update(Long.parseLong(recId), new HashMap<String, Object>() {{
                    put("consensusJson", JSON.toJSONString(list));
                    put("state", ETransferStatus.STATE_CHAIN_HALF_CONSENSUS);
                }});
            }
        }
    }

}
