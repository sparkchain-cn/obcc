//package cn.obcc.driver.utils;
//
//import com.alibaba.fastjson.JSON;
//
//import cn.obcc.driver.vo.BcMemo;
//import cn.obcc.utils.base.StringUtils;
//
//public class SpcMemoUtils {
//
//    public static String encode(String appid, String bizid, String data) {
//        if (data == null) {
//            data = "";
//        }
//        BcMemo memo = new BcMemo(appid, bizid, data);
//        return JSON.toJSONString(memo);
//    }
//
//    public static BcMemo buildMemo(String appid, String bizid, String data) {
//        if (data == null) {
//            data = "";
//        }
//        BcMemo memo = new BcMemo(appid, bizid, data);
//        return memo;
//    }
//
//    public static String encode(BcMemo spcMemo) {
//        if (spcMemo == null) {
//            return "";
//        }
//        return JSON.toJSONString(spcMemo);
//    }
//
//    public static BcMemo decode(String data) {
//        if (StringUtils.isNullOrEmpty(data)) {
//            return null;
//        }
//        try {
//            if (data.startsWith("{")) {
//                BcMemo memo = JSON.parseObject(data, BcMemo.class);
//                return memo;
//            }
//        } catch (Exception e) {
//            System.out.println(data);
//            // e.printStackTrace();
//        }
//        return null;
//
//    }
//
//    public static String decodeOrign(String data) {
//        if (StringUtils.isNullOrEmpty(data)) {
//            return null;
//        }
//        try {
//            if (data.startsWith("{")) {
//                BcMemo memo = JSON.parseObject(data, BcMemo.class);
//                if (memo != null) {
//                    return memo.getData();
//                }
//            } else {
//                return data;
//            }
//        } catch (Exception e) {
//            System.out.println(data);
//            e.printStackTrace();
//        }
//        return data;
//
//    }
//
//    public static String decodeOrign(String data, String dv) {
//        if (StringUtils.isNullOrEmpty(data)) {
//            return dv;
//        }
//        try {
//            if (data.startsWith("{")) {
//                BcMemo memo = JSON.parseObject(data, BcMemo.class);
//                if (memo != null && memo.getData() != null) {
//                    return memo.getData();
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(data);
//            e.printStackTrace();
//        }
//        return dv;
//
//    }
//
////	public static String decode(String data) {
////		try {
////			SpcMemo memo = JSON.parseObject(data, SpcMemo.class);
////
////			return memo.getData();
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return null;
////
////	}
//
//}
