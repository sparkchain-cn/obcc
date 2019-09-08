package cn.obcc.exception.enums;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName EDriverHandlerType
 * @desc TODO
 * @date 2019/9/7 0007  15:11
 **/
public enum EDriverHandlerType {

    AccountHandler("account", "account"),
    TokenHandler("token", "token"),
    ContractHandler("contract", "contract"),
    BlockHandler("block", "block"),
    TxSignatureHandler("txsignature", "txsignature"),
    CallbackListener("CallbackListener", "CallbackListener"),
    StateMonitor("StateMonitor", "StateMonitor"),
    NonceCalculator("NonceCalculator", "NonceCalculator"),
    MemoParser("MemoParser", "MemoParser"),
    SpeedAdjuster("SpeedAdjuster", "SpeedAdjuster"),
    CallbackRegister("CallbackRegister", "CallbackRegister");


    private String name;
    private String desc;

    private EDriverHandlerType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
