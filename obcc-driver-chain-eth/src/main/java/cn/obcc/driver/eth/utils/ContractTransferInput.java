//package cn.obcc.driver.eth.utils;
//
//
//import java.math.BigInteger;
//import java.nio.charset.StandardCharsets;
//
//import org.web3j.abi.datatypes.Address;
//import org.web3j.utils.Numeric;
//
//import cn.obcc.utils.base.StringUtils;
//
///**
// * no use
// */
//public class ContractTransferInput {
//    private String data;
//
//    public ContractTransferInput(String input) {
//        data = input;
//    }
//
//    private String destAddr;
//    private String amount;
//    private String memos;
//
//    public String getDestAddr() {
//        return destAddr;
//    }
//
//    public void setDestAddr(String destAddr) {
//        this.destAddr = destAddr;
//    }
//
//    public String getAmount() {
//        return amount;
//    }
//
//    public void setAmount(String amount) {
//        this.amount = amount;
//    }
//
//    public String getMemos() {
//        return memos;
//    }
//
//    public void setMemos(String memos) {
//        this.memos = memos;
//    }
//
//    // 0x56b8c724000000000000000000000000f334942393bdc01e00f55c766eaf745aa499a6050000000000000000000000000000000000000000000000003782dace9d9000000000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000008c7b226170706964223a2231303635323131303432303034343637373132222c2262697a6964223a22313036353231313034323030343436373731325f39363461623065612d383636382d343532652d616366382d633036663737366634323966222c2264617461223a226163636f756e74207472616e73666572203420207b7b6e6577546f6b656e7d7d227d0000000000000000000000000000000000000000
//    public static ContractTransferInput parse(String input, int decimal) {
//        return parse(input, 10, 64, 64, decimal);
//    }
//
//    public static ContractTransferInput parse(String input, int offset1, int offset2, int offset3, int decimal) {
//
//        ContractTransferInput cti = new ContractTransferInput(input);
//
//        if (StringUtils.isNullOrEmpty(input)) {
//            return cti;
//        }
//
//        //for moac: 0, 10, 64, 64
//        //for moac microchain: 0, 50, 64, 64
//        int index1 = 0;
//        int index2 = offset1;
//        int index3 = index2 + offset2;
//        int index4 = index3 + offset3;
//
//        // System.out.println("contract input data:" + input);
//        try {
//            String methodName = input.substring(0, index2);
//            // System.out.println(methodName);
//            String destAddr = input.substring(index2, index3);
//            // System.out.println(destAddr);
//            String destAddrTo = new Address(destAddr).getValue();
//            String value = input.substring(index3, index4);
//            // System.out.println(value);
//            BigInteger bigInteger = Numeric.toBigIntNoPrefix(value);
////			BigDecimal bigDecimal = new BigDecimal(bigInteger);
////			String valueTo = Convert.fromSha(bigDecimal, Unit.MC).stripTrailingZeros().toPlainString();
//            String valueTo = EthUtils.fromValueInMin(bigInteger, decimal).stripTrailingZeros().toPlainString();
//
//            cti.setAmount(valueTo);
//            cti.setDestAddr(destAddrTo);
//            cti.setMemos("");
//            try {
////				 System.out.println("test: " + input.substring(74 + 64, 74 + 64 + 64));
//                int memoStart = index3 + offset3 + offset3 + offset3;
//                if (memoStart < input.length()) {
//                    String memos = input.substring(index3 + offset3 + offset3 + offset3);
//                    String str = formatMemos(memos);
//                    cti.setMemos(str);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return cti;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return cti;
//    }
//
//    private static String formatMemos(String memos) {
//        byte[] bytes = Numeric.hexStringToByteArray(memos);
//
//        //20190417 by xuou: remove the useless /u0000 (byte = 0) in tail.  These /u0000 are added by smart contract method parameters.
//        int length = bytes.length;
//        for (int i = bytes.length - 1; i >= 0; i--) {
//            if (bytes[i] == 0) {
//                length = i;
//            } else {
//                i = -1;  //break
//            }
//        }
//        byte[] newBytes = new byte[length];
//        for (int i = 0; i < length; i++) {
//            newBytes[i] = bytes[i];
//        }
//
//        String newMemos = new String(newBytes, StandardCharsets.UTF_8);
//        return newMemos;
//    }
//}
