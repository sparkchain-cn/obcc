package cn.obcc.config;

import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.vo.driver.RecordInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
//@EqualsAndHashCode
public class ExProps implements Serializable {

    //是否是需要拆分
    private boolean needSplit = true;
    //是需要encode memo
    private boolean needHandleMemo = true;

    private EUpchainType upchainType = EUpchainType.Transfer;

    //传递过程，到transfer中保存到数据中去
    private RecordInfo recordInfo;

    private String appid;
    private String sparkHash;
    Map<String, Object> params;
    private List<String> uuids = new ArrayList<>();
    private String type;

}
