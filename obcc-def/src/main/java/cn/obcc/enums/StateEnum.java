package cn.obcc.enums;


public enum StateEnum {
    PLAF_RECEIVE("plafRecv", 0),
    PLAF_QUEUE("plafQueue", 2),
    PLAF_UPCHAIN("plafUpchain", 3),
    CHAIN_RECV("chainRecv", 4),
    CHAIN_QUEUE("chainQueue", 5),
    CHAIN_PENDING("chainPending", 6),
    BLOCK_RECV("blockRecv", 7),
    BLOCK_SUCC("blockSucc", 8),
    TRADE_SUCC("tradeSucc", 9),
    UPCHAIN_FAIL("upchainFail", -1),
    CHAIN_RECV_FAIL("chainRecvFail", -2),
    CHAIN_RECV_UNKNOWN("chainRecvUnknown", -3),
    CHAIN_DEAL_FAIL("chainDealFail", -4),
    CHAIN_TRADE_FAIL("chainTradeFail", -5);

    private String name;
    private Integer state;

    private StateEnum(String name, Integer state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
