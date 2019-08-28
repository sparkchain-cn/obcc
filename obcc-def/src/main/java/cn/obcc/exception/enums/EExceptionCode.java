package cn.obcc.exception.enums;


public enum EExceptionCode {

    SUCCESS_RETURN("200", "编号，正常"),
    ACCESS_FORBID("401", "无权限访问"),
    UNDEAL_ERROR("501", "未处理错误"),
    RUNTIME_EXCEPTION("502", "运行时错误"),

    NOT_IMPL("405", "未实现错误"),
    ACCESS_TOKEN_NEED("403", ""),
    APPID_NEED("404", ""),
    CHAIN_NEED("405", ""),
    TOKEN_NEED("406", ""),

    IP_DENY("410", "禁用该IP"),
    APPID_DENY("411", "禁用该APP"),
    CHAIN_DENY("412", "禁用该链"),
    TOKEN_DENY("413", "禁用该币种"),
    API_DENY("414", "该API地址禁用"),
    WALLET_DENY("415", "该WALLET地址禁用"),
    ACCOUNT_DENY("416", "该WALLET地址禁用"),

    CHAIN_APP_DENY("422", "针对传入的应用禁用该链"),
    TOKEN_APP_DENY("423", "针对传入的应用禁用该币种"),
    API_APP_DENY("424", "该API地址针对传入的应用禁用"),
    WALLET_APP_DENY("425", "针对传入的应用该WALLET地址禁用"),
    ACCOUNT_APP_DENY("426", "针对传入的应用该WALLET地址禁用"),

    CHAIN_NO_SUPPORT_AGAIN("432", "该链已经废用"),
    TOKEN_NO_SUPPORT_AGAIN("433", "该TOKEN已经废用"),
    API_NO_SUPPORT_AGAIN("434", "该API地址已废用"),
    TOKEN_NO_SUPPORT("435", "该TOKEN未注册"),

    PARAMETER_INVALID("1001", "参数错误"),
    BIZID_REPEAT("1002", "BIZID重复"),
    BIZID_EMPTY("1003", "BIZID为空"),

    SPRING_NOT_FINISH_INIT("1004", "Spring尚未初始化完成"),
    CAN_NOT_FIND_SERVER_INSTANCE_IP("1005", "Consul取不到实例的IP"),

    INPUT_NOT_RIGHT("3007","The accessToken has not been inputed or the value has expired."),
    VIEW_CHAIN_QUEUE_PENDING("5011","获取 penging or queue 队列失败"),

    // 2000系，网络类
    IO_EXCEPTION("2000", "IO 异常"),
    EMPTY_RESPONSE("2001", "IO为空响应"),
    MALFORMED_URL_EXCEPTION("2002", "URL格式不对"),

    // 3000系，业务类
    TASK_FAILED("30001", "任务失败"),


    //App
    APPID_SECRET_FAIL("31001", "appid及appsecret验证失败"),

    //WALLET
    SYSTEM_ERROR("32000", "系统异常"),
    PASSWORD_OR_KEY_NOT_MATCH("32038", "密码不匹配"),
    NOT_ENOUGH_MONEY("32039", "余额不足"),
    MEMO_TOO_LONG("32040", "备注信息过长"),
    ACCOUNT_ACTIVATED("32041","钱包已经被激活"),
    NOT_WALLET_REL_CHAIN("32042","钱包已经被激活"),

    //SIDE_CAR
    SIDE_CAR_NO_LOCAL_METHOD("40001", "边车本地调用没有找到本地的执行方法"),

    //State
    UPCHAIN_STATE_EXIST_ERROR_NONCE("41001", "帐户存在异常的交易记录，已经分配了Nonce."),
    UPCHAIN_STATE_EXIST_UNFINISHED_TRADE("41002", "帐户存在未完成交易记录，已经分配了Nonce."),
    UPCHAIN_STATE_NONCE_LOW_REDIS("41003", "链上取的nonce比本地的redis中低."),
    UPCHAIN_STATE_NONCE_NULL("41004", "链上取的nonce返回Null."),
    // 4000系，链上操作类

    ORIGN_CHAIN_ERROR("4000","原生链上的错误。"),

    NOT_FIND_CHAIN_CONFIG("4001",""),

    NOT_FIND_CHAIN_IP("4002","找不到链对应IP地址"),

    NOT_FIND_ANY_OPEN_CONNECTION("4003","找不到任何的打开的连接"),

    CREATE_ACCOUNT_FAIL("4004","创建账号失败"),

    ACCOUNT_NOT_ACTIVATED("4005","账号未激活，只有井通"),

    ACCOUNT_NOT_SUPPORT_TOKEN("4006","账号不支持该TOKEN"),

    ACCOUNT_ADDR_ILLEGAL("4007","账号地址是非法的，长度不符合要求或字符不符合要求"),

    ACCOUNT_SECRET_ILLEGAL("4008","账号私钥是非法的，长度不符合要求或字符不符合要"),

    ACCOUNT_NOT_IN_USE("4020","该账号暂停使用，可能是余额不足"),

    TRADE_HASH_ILLEGAL("4009","交易Hash格式不对，长度不符合要求或字符不符合要求"),

    ACCOUNT_SECRET_NOT_FOLLOW_ADDR("4010","账号私钥和公钥不相符"),

    AMOUNT_ILLEGAL("4011","金额格式不对"),

    FETCH_NONCE_FAIL("4012","取nonce失败"),

    FETCH_BALANCE_FAIL("4013","取余额失败"),

    FETCH_TX_FAIL("4014","取流水失败"),

    TRANSFER_FAIL("4015","交易失败"),

    TRANSFER_SIGN_ERROR("4016","交易签名失败"),

    DEPLOY_FAIL("4017","部署合约失败"),

    RETURN_NULL_OR_EMPTY("4018","链返回空或NULL"),

    MEMO_PARSE_ERROR("4019","解析Memo出错"),

    CONTRACT_COMPILE_ERROR("4020","合约编译失败"),

    //6000系，加密解密类
    NO_SUCH_ALGORITHM_EXCEPTION("6001",""),

    NO_SUCH_PROVIDER_EXCEPTION("6002",""),

    INVALID_KEYSPEC_EXCEPTION("6003",""),

    UNSUPPORTED_ENCODING_EXCEPTION("6004",""),

    ILLEGAL_ARGUMENT_EXCEPTION("6005",""),

    CONVERT_EXCEPTION("6006","转换出错，base64,hex");


    private String name;
    private String descr;

    private EExceptionCode(String name, String desc) {
        this.name = name;
        this.descr = desc;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
