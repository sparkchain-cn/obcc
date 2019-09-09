package cn.obcc.config;

import cn.obcc.exception.enums.EUpchainType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExProps {

    public static final String INIT_TYPE = "1";
    public static final String DYNC_TYPE = "2";
    public static final String MANUL_TYPE = "4";

    private String appid;

    private String sparkHash;

    Map<String, Object> params;

    private List<String> uuids = new ArrayList<>();

    private String type;

    //是否是需要拆分
    private boolean needSplit = true;
    //是需要encode memo
    private boolean needHandleMemo = true;

    EUpchainType upchainType = EUpchainType.Transfer;


    public ExProps() {
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSparkHash() {
        return sparkHash;
    }

    public void setSparkHash(String sparkHash) {
        this.sparkHash = sparkHash;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<String> getUuids() {
        return uuids;
    }

    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }

    public boolean isNeedSplit() {
        return needSplit;
    }

    public void setNeedSplit(boolean needSplit) {
        this.needSplit = needSplit;
    }


    public EUpchainType getUpchainType() {
        return upchainType;
    }

    public void setUpchainType(EUpchainType upchainType) {
        this.upchainType = upchainType;
    }

    public boolean isNeedHandleMemo() {
        return needHandleMemo;
    }

    public void setNeedHandleMemo(boolean needHandleMemo) {
        this.needHandleMemo = needHandleMemo;
    }
}
