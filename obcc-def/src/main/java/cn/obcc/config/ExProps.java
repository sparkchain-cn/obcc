package cn.obcc.config;

import cn.obcc.exception.enums.EUpchainType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
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

}
