package cn.obcc.exception.enums;

/**
 * TransferStatusEnum
 *
 * @author ecasona
 * @version 1.4
 * @date 2018/12/13 9:46
 * @details: 上链状态(交易状态)
 */
public enum ETransferState {

    STATE_NONE("spc_none", -100),

    /**
     * 区块链共识失败
     */
    STATE_CHAIN_CONSENSUS_FAILURE("chain_consensus_failure", -1),
    /**
     * 区块链明确失败
     */
    STATE_CHAIN_DEFINITE_FAILURE("chain_definite_failure", -2),
    /**
     * 区块链非明确失败
     */
    STATE_CHAIN_IN_DEFINITE_FAILURE("chain_in_definite_failure", -3),
    /**
     * 接入已受理
     */
    STATE_SPC_ACCEPT("spc_accept", 0),
    /**
     * 等待
     */
    STATE_WAIT("wait", 10),
    /**
     * 进入接入队列
     */
    STATE_SPC_QUEUE("spc_queue", 20),
    /**
     * 写链，这里拆分成多条记录
     */
    STATE_WRITE_CHAIN("write_chain", 30),

    /**
     * 多条记录时部分接收
     */
    STATE_CHAIN_ACCEPT_HALF("chain_half_accept", 35),
    /**
     * 区块链已受理
     */
    STATE_CHAIN_ACCEPT("chain_accept", 40),

    /**
     * 区块链排队
     */
    STATE_CHAIN_QUEUE("chain_queue", 50),
    /**
     * 区块链全网广播
     */
    STATE_CHAIN_BROAD("chain_broad", 60),
    /**
     * 区块链收纳
     */
    STATE_CHAIN_RECEIPT("chain_receipt", 70),

    /**
     * 多条部分共识成功
     */
    STATE_CHAIN_HALF_CONSENSUS("chain_half_consensus", 75),
    /**
     * 区块链共识成功
     */
    STATE_CHAIN_CONSENSUS("chain_consensus", 80),

    /**
     * 接入成功
     */
    STATE_SPC_SUCCESS("spc_success", 90);


    private String name;

    private Integer status;

    private ETransferState(String name, Integer status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static String getName(Integer status) {
        for (ETransferState e : ETransferState.values()) {
            if (e.getStatus().equals(status)) {
                return e.getName();
            }
        }
        return null;
    }

    public static Integer getStatus(String name) {
        for (ETransferState e : ETransferState.values()) {
            if (e.getName().equals(name)) {
                return e.getStatus();
            }
        }
        return null;
    }


}
