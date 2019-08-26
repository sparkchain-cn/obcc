package cn.obcc.driver.vo;

import com.alibaba.fastjson.JSON;

import cn.obcc.utils.HexStringUtils;
import cn.obcc.utils.base.StringUtils;

/**
 * <t>
 * userMems+&spc&+sparkHash(20位)+seq(2位）+moduleCode(3位)+operateCode(5位）
 * 38位
 * </t>
 * sparkHash就是流水表的ID
 * moduleCode 00:上链，01：资产
 * operateCode：业务自定义的编码(upchainType+其它)
 */
public class BcMemo {

    public static final String split = "&spc&";
    public static final String splitHex = HexStringUtils.str2HexStr("&spc&");

    public static final String MODULE_CODE_UPCHAIN = "000";
    public static final String MODULE_CODE_ASSETS = "001";
    public static final String FLOW_ORDER_MAIN = "00";

    /**
     * 用户自定义的数据 user Memo Info
     */
    private String data;
    private boolean isHexData = false;
    /**
     * 不用
     */
    private String appid;

    /**
     * sparkHash(20位)
     */
    private String bizid;

    /**
     * 3位
     * moduleCode 000:上链，001：资产
     */
    private String moduleCode = "000";

    /**
     * 5位
     * operateCode：业务自定义的编码(upchainType+其它)
     * 第一位是上链类型，中间三位是操作编码,最后位是解析类型
     */
    private String operateCode = "00000";


    /**
     * 2位
     * //为空就不显示
     * //顺序号，多条memos的时候使用
     * 默认为00
     * 控制为00，其它子从01开始
     */
    private String seq = "00";

    private String controlData;

    private boolean isHexControlData = false;

    public BcMemo() {
    }

    public BcMemo(String appid, String bizid) {
        super();
        this.appid = appid;
        this.bizid = bizid;

    }

    public BcMemo(String appid, String bizid, String data) {
        super();
        this.appid = appid;
        this.bizid = bizid;
        this.data = data;

    }

    public BcMemo(String appid, String bizid, String seq, String data) {
        super();
        this.appid = appid;
        this.bizid = bizid;
        this.seq = seq;
        this.data = data;
    }

    /**
     * * userMems+&spc&+sparkHash(20位)+seq(2位）+moduleCode(3位)+operateCode(5位）
     *
     * @param bizid
     * @param moduleCode
     * @return
     */
    public static BcMemo build(String bizid, String seq, String moduleCode, String operateCode) {
        BcMemo memo = new BcMemo();
        memo.setBizid(bizid);
        memo.setSeq(seq);
        memo.setModuleCode(moduleCode);
        memo.setOperateCode(operateCode);

        return memo;
    }

    public static BcMemo build(String bizid, String moduleCode, String operateCode) {
        BcMemo memo = new BcMemo();
        memo.setBizid(bizid);
        memo.setModuleCode(moduleCode);
        memo.setOperateCode(operateCode);
        return memo;
    }

    public BcMemo addData(String data, boolean isHex) {
        this.data = data;
        this.isHexData = isHex;
        return this;
    }

    public BcMemo addControlData(String data, boolean isHex) {
        this.controlData = data;
        this.isHexControlData = isHex;
        return this;
    }

    //获取解析码
    public String getParseCode() {
        return getOperateCode().substring(4);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }


    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }


    public String getControlData() {
        return controlData;
    }

    public void setControlData(String controlData) {
        this.controlData = controlData;
    }

    /**
     * userMems+&spc&+sparkHash(20位)+seq(2位）+moduleCode(3位)+operateCode(5位）
     *
     * @return
     */
    public String encodeHex() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotNullOrEmpty(data)) {
            //开头加上&spc&进行处理
            sb.append(HexStringUtils.str2HexStr(split));
            if (isHexData) {
                sb.append(data);
            } else {
                sb.append(HexStringUtils.str2HexStr(data));
            }
        }

        sb.append(HexStringUtils.str2HexStr(split));
        sb.append(HexStringUtils.str2HexStr(String.format("%020d", Long.parseLong(bizid))));
        sb.append(HexStringUtils.str2HexStr(String.format("%02d", Integer.parseInt(seq))));
        sb.append(HexStringUtils.str2HexStr(String.format("%03d", Integer.parseInt(moduleCode))));
        sb.append(HexStringUtils.str2HexStr(String.format("%05d", Integer.parseInt(operateCode))));

        if (StringUtils.isNotNullOrEmpty(controlData)) {
            if (isHexControlData) {
                sb.append(controlData);
            } else {
                sb.append(HexStringUtils.str2HexStr(controlData));
            }
        }

        return sb.toString();
    }

    public static BcMemo decode(String data) {
        if (data == null) {
            return null;
        }

        String next = data;
        BcMemo m = new BcMemo();
        if (data.contains(split)) {
        } else {
            String arr[] = data.split(split);
            m.setData(arr[0]);
            next = arr[1];
        }
        m.setBizid(next.substring(0, 20));
        m.setSeq(next.substring(20, 22));
        m.setModuleCode(next.substring(22, 25));
        m.setOperateCode(next.substring(25, 30));

        //有control Data
        if (next.length() > 30) {
            m.setControlData(next.substring(30));
        }

        return m;

    }

    public static boolean check(String hexData) {
        if (StringUtils.isNullOrEmpty(hexData)) {
            return false;
        }
        //必须要以&spc&开头
        if (!hexData.startsWith(splitHex)) {
            return false;
        }

        return true;
    }

    /**
     * no check ,先调用check方法
     *
     * @param hexData
     * @return
     */
    public static BcMemo decodeHex(String hexData) {
        //去掉前缀&spc&
        String data = HexStringUtils.hexStr2Str(hexData).substring(10);
        BcMemo memo = decode(data);
        return memo;
    }


    public static String buildOpCode(String upchainCode, String operateCode, String parseCode) {
        return upchainCode + operateCode + parseCode;
    }


    public static void main(String args[]) {

        String data = "3中甸223ae3232ebcde52233d在这晨";
        data = HexStringUtils.str2HexStr(data);
        BcMemo m = BcMemo.build("1002015320560369664", "02", "01", "1234");
        m.addData(data, true);

        String controlData = "{m:\"desdss\",d:true}";
        controlData = HexStringUtils.str2HexStr(controlData);
        m.addControlData(controlData, true);
        String ret = m.encodeHex();

        System.out.println(ret);
        if (BcMemo.check(ret)) {
            BcMemo memo = BcMemo.decodeHex(ret);
            System.out.println(JSON.toJSONString(memo));
            System.out.println(Long.parseLong(memo.getBizid()));
            System.out.println(memo.getControlData());
        }


    }

}
