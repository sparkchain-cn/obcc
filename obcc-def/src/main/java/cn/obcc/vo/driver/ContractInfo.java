package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Map;

//@Table(name = "contract_info")
@Data
public class ContractInfo extends Entity {
    @Id
    private long id;
    private String pkgName;

    private String name;

    private String content;
    private String abi;
    private String bin;
    //{"0x2323233":"transfer":,"03de3scdds":"burn"}
    private String methodIdNameMapStr;

    private String hash;
    private String contractAddr;

    private int state;

    private String compileResult;
    private String compileException;


    @Transient
    public Map<String, String> getMethodIdNameMap() {
        return (Map<String, String>) JSON.parse(getMethodIdNameMapStr());
    }
}
