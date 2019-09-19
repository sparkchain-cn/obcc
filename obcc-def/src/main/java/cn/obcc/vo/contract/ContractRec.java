package cn.obcc.vo.contract;

import cn.obcc.vo.KeyValue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ContractExecRec
 * @desc
 * @data 2019/9/2 17:02
 **/
@Data
public class ContractRec {

    private String method;

    private List<KeyValue> params = new ArrayList<>();


}
