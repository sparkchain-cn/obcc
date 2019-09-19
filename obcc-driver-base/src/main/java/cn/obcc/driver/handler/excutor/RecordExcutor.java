package cn.obcc.driver.handler.excutor;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.dao.RecordInfoDaoBase;
import cn.obcc.driver.IChainDriver;
import cn.obcc.vo.driver.ChainPipe;
import cn.obcc.enums.ETransferState;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.db.BlockTxInfo;
import cn.obcc.vo.db.RecordInfo;
import cn.obcc.vo.db.TxConSensus;
import cn.obcc.vo.db.TxRecv;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Date;
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

    public static RecordInfo saveRecord(ChainPipe pipe, IChainDriver driver, ObccConfig config) throws Exception {

        if (pipe.getRecordInfo() == null) {
            pipe.setRecordInfo(new RecordInfo());
        }
        RecordInfo recordInfo = pipe.getRecordInfo();
        recordInfo.setId(UuidUtils.get());
        if (StringUtils.isNullOrEmpty(recordInfo.getToken())) {
            recordInfo.setToken(config.getChain().getToken());
        }
        //ETransferStatus.STATE_WAIT
        recordInfo.setMemo(pipe.getFromAccount().getMemos());

        recordInfo.setSrcAccount(pipe.getFromAccount().getSrcAddr());
        recordInfo.setDestAccount(pipe.getDestAddr());
        recordInfo.setContractAddr(pipe.getContractAddr());
        recordInfo.setAmount(pipe.getAmount());
        recordInfo.setState(ETransferState.STATE_WAIT);
        recordInfo.setCreateTime(new Date());
        driver.getLocalDb().getRecordInfoDao().add(recordInfo);
        return recordInfo;

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
                put("state", ETransferState.STATE_CHAIN_ACCEPT);
            }});
        } else {
            r.setTxRecvJson(JSON.toJSONString(r.getTxRecvList()));
            recordInfoDao.update(r.getId(), new HashMap<String, Object>() {{
                put("txSize", r.getTxSize());
                put("txRecvJson", r.getTxRecvJson());
                put("state", ETransferState.STATE_CHAIN_ACCEPT);
            }});
        }

    }

    public static void consenseUpdate(BizState bizState, BlockTxInfo txInfo, IChainDriver driver) throws Exception {

        String recId = txInfo.getRecId();
        RecordInfoDaoBase recordInfoDao = driver.getLocalDb().getRecordInfoDao();
        if (bizState.getTxSize() == 1) {
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
                put("state", ETransferState.STATE_CHAIN_CONSENSUS);
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
            //最后一条
            if (bizState.getTxSize() == bizState.getConsensusSize()) {
                recordInfoDao.update(Long.parseLong(recId), new HashMap<String, Object>() {{
                    put("consensusJson", JSON.toJSONString(list));
                    put("state", ETransferState.STATE_CHAIN_CONSENSUS);
                }});
            } else {
                recordInfoDao.update(Long.parseLong(recId), new HashMap<String, Object>() {{
                    put("consensusJson", JSON.toJSONString(list));
                    put("state", ETransferState.STATE_CHAIN_HALF_CONSENSUS);
                }});
            }
        }
    }

}
