package cn.obcc.driver.eth;

public class EthConstants {

    public static final String ETH_TOKEN = "ETH";
    public static final Integer DEFAULT_PRECISION = 18;

    public static final String EMPTY_DATA = "0x00";
    public static final String EMPTY_VALUE = "0x";
    public static final String EMPTY_ADDRESS = "0x";

    public final static String ERROR_MOAC_RTU = "replacement transaction underpriced";//在队列中，nonce存在，参数相同，说明要冲掉，但是冲掉费用过低
    public final static String ERROR_MOAC_KT = "known transaction";//在队列中，nonce存在，参数不同，说明不是冲掉
    public final static String ERROR_MOAC_NTL = "nonce too low";//在区块上，
    public final static int SafeFactor = 10;

    public static final String PARAM_TOKEN_FULL_NAME = "tokenFullName";
    public static final String PARAM_TRANSACTION_TYPE = "transactionType";
    public static final String PARAM_DECIMAL = "decimal";
    public static final String PARAM_VIA = "via";

    public static final String CHAINCODE_MOAC_MAINNET = "MOAC_MAINNET";
    public static final String CHAINCODE_MOAC_TESTNET = "MOAC_TESTNET";

    public static final String CONTRACT_METHOD_TRANSFER = "transfer";
    public static final String CONTRACT_METHOD_REGISTER_DAPP = "registerDapp";

    public static final String CONFIG_CONTRACT_METHOD = "contractMethod";
    public static final String CONFIG_PENDING = "pending";
    public static final String CONFIG_QUEUED = "queued";
}
