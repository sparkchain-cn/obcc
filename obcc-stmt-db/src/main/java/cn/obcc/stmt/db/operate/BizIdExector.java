package cn.obcc.stmt.db.operate;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizIdExector
 * @desc TODO
 * @date 2019/9/17 0017  18:03
 **/
public class BizIdExector {

    public static String getTableBizId(String db, String tableName) {
        return db + "_" + tableName;
    }

    public static String getRecordBizId(String db, String tableName, Long id) {
        return db + "_" + tableName + "_" + id;
    }


//    private String getUpdateBizId(String tableName, Long id) throws Exception {
//        String bizId = BizIdExector.getRecordBizId(config.getClientId(),tableName, id);
//        String v = getDriver().getLocalDb().getRecordInfoDao().getBizIdPreLikeSize(bizId);
//        return bizId + "_" + v;
//    }

}
