package cn.obcc.config;

import cn.obcc.enums.*;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.pool.ChainNodeWeight;
import cn.obcc.vo.pool.ConnPoolConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ObccConfig {
    @NotBlank
    private String clientId = "obcc";
    @NotBlank
    private String localDbName = "cn.obcc.db.SqliteDb";
    @NotBlank
    private String speedAdjusterName = "cn.obcc.driver.adjuster.SpeedAdjuster";
    @NotBlank
    private String jdbcTemplateName = "cn.obcc.db.sqlite.SqliteJdbcTemplate";
    @NotBlank
    private String driverName = "cn.obcc.driver.eth.EthChainDriver";
    @NotBlank
    private String callbackNotfyName = "cn.obcc.driver.callback.notify.base.ProcessCallbackNotify";

    private long callbackWaitTime = 20 * 60 * 1000;//20分钟
    @NotBlank
    private ConnPoolConfig poolConfig = new ConnPoolConfig();

    private String nodeUrl = "localhost:8545";
    @NotBlank
    private EChainType chain = EChainType.ETHER;

    private String dbConn = "d:/obcc/obcc.dbf";
    private String dbUserName = "root";
    private String dbPassword = "123456";


    //private ENonceStrategy nonceStrategy = ENonceStrategy.Memory;


    private ECallbackStrategy callbackStrategy = ECallbackStrategy.Subscribe;
    private EContractType contractType = EContractType.SOLC;

    private String solcPath = "";
    private String tempPath = "";


    private String memoPre = "&&spc";
    private String memoPreHex = "0x2626737063";
    // HexUtils.str2Hex(config.getMemoPre())


    private ECallBackLevel callBackLevel = ECallBackLevel.SupportUpchain;

    private List<String> uuids = new ArrayList<>();


    private long adjustInterval = 300;//   MilliSecond
    private KeyValue<String> tokenCreateAccount = new KeyValue<String>() {
        {
            setKey("0x787172b7f5e6465a06a699b2a88bb1143a01e138");
            setVal("d66b74561cf0d96766e527122085bb93e777cc758a3a6f485598325cf315209b");
        }
    };

    private KeyValue<String> storageSrcAccount = new KeyValue<String>() {
        {
            setKey("0x787172b7f5e6465a06a699b2a88bb1143a01e138");
            setVal("d66b74561cf0d96766e527122085bb93e777cc758a3a6f485598325cf315209b");
        }
    };

    private KeyValue<String> storageDestAccount = new KeyValue<String>() {
        {
            setKey("0x787172b7f5e6465a06a699b2a88bb1143a01e138");
            setVal("d66b74561cf0d96766e527122085bb93e777cc758a3a6f485598325cf315209b");
        }
    };

    private String localFilePath = "c:/obcc/";
    private String ipfsPath = "http://localhost:6789";

    /**
     * 初始化账号(创世账号)
     */
    private String geneAccount = "0x787172b7f5e6465a06a699b2a88bb1143a01e138";
    /**
     * 账号私钥
     */
    private String genePrivateKey = "d66b74561cf0d96766e527122085bb93e777cc758a3a6f485598325cf315209b";


    private String stardardTokenParserClzName = "cn.obcc.token.stardard.StardardTokenParser";

    private String nonceStrategyClzName = "cn.obcc.nonce.proc.NonceProcStrategy";

    private String memoStrategyClzName = "cn.obcc.memo.json.MemoJsonStragegy";

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
        this.getPoolConfig().getNodeWeights().add(new ChainNodeWeight() {{
            setUrl(nodeUrl);
            setWeight(100);
        }});
    }


}
