package cn.obcc.driver.eth.utils;


/**
 * AssetCreateEnum
 *
 * @author xuou
 * @version
 * @date 2019/3/14
 * @details Moac transaction type
 */
public enum TransactionType {

    /**
     * normal transaction in main chain
     */
    MAINCHAIN_TRANSFER(1),
    /**
     * normal transaction in main chain, erc20 交易
     */
    MAINCHAIN_CALL(2),
    /**
     * normal transaction in main chain
     */
    MAINCHAIN_DEPOLY(3);

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    TransactionType(Integer type) {
        this.type = type;
    }

    public static TransactionType checkType(Integer type) {
        for (TransactionType assetCreateEnum : TransactionType.values()) {
            if (assetCreateEnum.getType().equals(type)) {
                return assetCreateEnum;
            }
        }
        return null;
    }
}